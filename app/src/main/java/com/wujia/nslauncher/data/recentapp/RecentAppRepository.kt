package com.wujia.nslauncher.data.recentapp

import com.wujia.nslauncher.data.recentapp.local.RecentAppResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

class RecentAppRepository(
    private val recentAppResource: RecentAppResource,
    private val externalScope: CoroutineScope // This could be CoroutineScope(SupervisorJob() + Dispatchers.Default).
) {

    suspend fun getRecentlyApp(): List<String> {
        return externalScope.async {
            recentAppResource.getRecentApps()
        }.await()
    }

}