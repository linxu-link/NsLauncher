package com.wujia.nslauncher.data.recentapp.local

interface IRecentApp {

    suspend fun getRecentlyUsedApps(): List<String>

}