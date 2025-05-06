package com.wujia.nslauncher.domain

import com.wujia.nslauncher.data.allapp.AllAppRepository
import com.wujia.nslauncher.data.recentapp.RecentAppRepository
import com.wujia.nslauncher.entity.NsAppInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetRecentlyAppListUseCase(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val allAppRepository: AllAppRepository,
    private val recentAppRepository: RecentAppRepository
) {

    suspend operator fun invoke(size: Int): List<NsAppInfo> {
        return withContext(dispatcher) {
            val recentlyApp = recentAppRepository.getRecentlyApp()
            val latestAllApps = allAppRepository.getLatestAllApps(true)
            val latestAppsMap = latestAllApps.associateBy { it.packageName }
            if (recentlyApp.isEmpty()) {
                var tempSize = latestAllApps.size
                if (tempSize > size) {
                    tempSize = size
                }
                latestAllApps.subList(0, tempSize)
            } else {
                val filteredApps = recentlyApp
                    .asSequence()
                    .mapNotNull { latestAppsMap[it] }
                    .take(size)
                    .toList()
                filteredApps
            }
        }
    }

}