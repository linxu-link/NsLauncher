package com.wujia.nslauncher.data.allapp.remote

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.wujia.launcher.core.HiLauncher
import com.wujia.launcher.core.interfaces.InstalledAppChangeListener
import com.wujia.nslauncher.data.allapp.worker.RefreshLatestAllAppWorker

private const val FETCH_LATEST_ALL_APPS_TASK = "FetchLatestAllAppsTask"
private const val TAG_FETCH_LATEST_ALL_APPS = "FetchLatestAllAppsTaskTag"

/**
 * 如果任务需要在应用启动时触发，建议使用从 Initializer 调用存储库的 App Startup 库触发 WorkManager 请求。
 */
class AllAppTaskResource(private val workManager: WorkManager) {

    private val installedListener = object : InstalledAppChangeListener {

        override fun onInstalledAppChanged() {
            fetchAllAppsImmediately()
        }

    }

    init {
        HiLauncher.registerAppInstallListener(installedListener)
    }

    // 新增方法：触发立即刷新
    fun fetchAllAppsImmediately() {
        val refreshRequest = OneTimeWorkRequestBuilder<RefreshLatestAllAppWorker>()
            .addTag(TAG_FETCH_LATEST_ALL_APPS)

        workManager.enqueueUniqueWork(
            FETCH_LATEST_ALL_APPS_TASK,
            ExistingWorkPolicy.REPLACE,
            refreshRequest.build()
        )
    }

    fun cancelFetchingAllApps() {
        workManager.cancelAllWorkByTag(TAG_FETCH_LATEST_ALL_APPS)
    }

    fun destroy(){
        HiLauncher.unregisterAppInstallListener(installedListener)
    }
}