package com.wujia.launcher.core.manager

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.icu.util.Calendar
import com.wujia.launcher.core.entity.AppInfo
import com.wujia.toolkit.HiAppGlobal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class AppManager {

    companion object {

        // 获取当前系统安装的所有可以在桌面上显示的应用
        internal fun getInstalledApps(): List<AppInfo> {
            val packageManager = HiAppGlobal.getApplication().packageManager
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val resolveInfoList = packageManager.queryIntentActivities(intent, 0)
            val appList = mutableListOf<AppInfo>()
            for (resolveInfo in resolveInfoList) {
                val appInfo = AppInfo(
                    resolveInfo.loadLabel(packageManager).toString(),
                    resolveInfo.activityInfo.packageName,
                    resolveInfo.activityInfo.iconResource,
                    resolveInfo.activityInfo.name
                )
                appList.add(appInfo)
            }
            // 去重
            return appList.distinctBy { it.packageName }
        }

        // 启动应用
        internal fun startApp(packageName: String) {
            CoroutineScope(Dispatchers.Default).launch {
                runCatching {
                    val context = HiAppGlobal.getApplication()
                    val intent = context.packageManager.getLaunchIntentForPackage(packageName)
                    context.startActivity(intent)
                }.onFailure {
                    it.printStackTrace()
                }
            }
        }

        // 杀死应用
        internal fun killApp(packageName: String) {
            CoroutineScope(Dispatchers.Default).launch {
                runCatching {
                    val context = HiAppGlobal.getApplication()
                    val am =
                        context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
                    am.killBackgroundProcesses(packageName)
                }.onFailure {
                    it.printStackTrace()
                }
            }
        }

        // 获取应用信息
        internal fun getAppInfo(packageName: String): AppInfo? {
            val packageManager = HiAppGlobal.getApplication().packageManager
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val resolveInfoList = packageManager.queryIntentActivities(intent, 0)
            for (resolveInfo in resolveInfoList) {
                if (resolveInfo.activityInfo.packageName == packageName) {
                    return AppInfo(
                        resolveInfo.loadLabel(packageManager).toString(),
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.iconResource,
                        resolveInfo.activityInfo.name
                    )
                }
            }
            return null
        }

        internal fun getRecentlyUsedApps(): List<String> {
            val context = HiAppGlobal.getApplication()
            val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val calendar = Calendar.getInstance()
            val endTime = calendar.timeInMillis
            calendar.add(Calendar.HOUR, -24) // 查询最近24小时
            val startTime = calendar.timeInMillis
            // 查询使用记录
            val stats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                startTime,
                endTime
            )
            // 按最后使用时间排序
            val sortedStats = stats.sortedByDescending { it.lastTimeUsed }

            // 提取包名（过滤系统应用）
            return sortedStats
                .filter { it.packageName != context.packageName } // 排除自身
                .map { it.packageName }
        }
    }
}