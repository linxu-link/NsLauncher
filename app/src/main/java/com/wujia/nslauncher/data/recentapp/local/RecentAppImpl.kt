package com.wujia.nslauncher.data.recentapp.local

import com.wujia.nslauncher.data.recentapp.local.dao.RecentAppDao

class RecentAppImpl(
    private val recentAppDao: RecentAppDao
) : IRecentApp {

    override suspend fun getRecentlyUsedApps(): List<String> {
        val recentApps = recentAppDao.getAll()
        val list = mutableListOf<String>()
        recentApps.forEach {
            list.add(it.pkg)
        }
        return list
    }

}