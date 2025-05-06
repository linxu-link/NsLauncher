package com.wujia.nslauncher.data.allapp.remote

import com.wujia.launcher.core.HiLauncher
import com.wujia.launcher.core.entity.AppInfo
import com.wujia.toolkit.HiAppGlobal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AllAppResource(
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun fetchAllApps(ignore: List<String> = listOf(HiAppGlobal.getApplication().packageName)): List<AppInfo> =
        withContext(dispatcher) {
            val list = HiLauncher.getInstalledApps().toMutableList()
            list.removeIf { appInfo->
                // 过滤掉在忽略列表中的包名
                ignore.contains(appInfo.packageName)
            }
            list
        }
}