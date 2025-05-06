package com.wujia.nslauncher.data.quickapp

import com.wujia.launcher.core.HiLauncher
import com.wujia.nslauncher.entity.NsAppInfo
import kotlinx.coroutines.CoroutineScope

class QuickAppRepository(
    private val externalScope: CoroutineScope // This could be CoroutineScope(SupervisorJob() + Dispatchers.Default).
) {

    suspend fun getQuickApps(): List<NsAppInfo> {
        val appList = mutableListOf<NsAppInfo>()
        HiLauncher.getAppInfo("com.android.phone")?.let {
            appList.add(NsAppInfo(it.name, it.packageName, it.iconResource, it.launcherActivity))
        }
        HiLauncher.getAppInfo("com.android.settings")?.let {
            appList.add(NsAppInfo(it.name, it.packageName, it.iconResource, it.launcherActivity))
        }
        HiLauncher.getAppInfo("com.android.camera")?.let {
            appList.add(NsAppInfo(it.name, it.packageName, it.iconResource, it.launcherActivity))
        }
        HiLauncher.getAppInfo("com.android.gallery3d")?.let {
            appList.add(NsAppInfo(it.name, it.packageName, it.iconResource, it.launcherActivity))
        }
        HiLauncher.getAppInfo("com.android.mms")?.let {
            appList.add(NsAppInfo(it.name, it.packageName, it.iconResource, it.launcherActivity))
        }
        return appList
    }

}