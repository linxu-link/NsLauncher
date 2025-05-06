package com.wujia.nslauncher.data.allapp

import androidx.work.WorkManager
import com.wujia.launcher.core.HiLauncher
import com.wujia.launcher.core.entity.AppInfo
import com.wujia.nslauncher.entity.NsAppInfo
import com.wujia.nslauncher.data.allapp.remote.AllAppResource
import com.wujia.nslauncher.data.allapp.remote.AllAppTaskResource
import com.wujia.toolkit.HiAppGlobal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * All App
 * 使用内存缓存实现
 */
class AllAppRepository(
    private val allResources: AllAppResource,
    private val externalScope: CoroutineScope// This could be CoroutineScope(SupervisorJob() + Dispatchers.Default).
) {

    private val allAppTaskResources =
        AllAppTaskResource(WorkManager.getInstance(HiAppGlobal.getApplication()))

    // Mutex to make writes to cached values thread-safe.
    private val latestAllAppsMutex = Mutex()

    // Cache of the latest news got from the network.
    private var latestAllApps: List<NsAppInfo> = emptyList()

    suspend fun getLatestAllApps(refresh: Boolean = false): List<NsAppInfo> {
        return if (refresh) {
            externalScope.async {
                val remoteResult = allResources.fetchAllApps()
                val appList = remoteResult.map { appInfo ->
                    NsAppInfo(
                        appInfo.name,
                        appInfo.packageName,
                        appInfo.iconResource,
                        appInfo.launcherActivity
                    )
                }.toMutableList()
                latestAllAppsMutex.withLock {
                    latestAllApps = appList
                }
                appList // 返回转换后的列表
            }.await()
            // async 用于在外部作用域内启动协程。
            // await 在新的协程上调用，以便在网络请求返回结果并且结果保存到缓存中之前，一直保持挂起状态。
            // 如果届时用户仍位于屏幕上，就会看到最新新闻；如果用户已离开屏幕，await 将被取消，但 async 内部的逻辑将继续执行。
        } else {
            return latestAllAppsMutex.withLock { this.latestAllApps }
        }
    }

    suspend fun refreshAllApps() {
        getLatestAllApps(true)
    }

}