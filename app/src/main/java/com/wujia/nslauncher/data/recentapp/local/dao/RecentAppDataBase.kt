package com.wujia.nslauncher.data.recentapp.local.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RecentApp::class], version = 1, exportSchema = false)
abstract class RecentAppDataBase : RoomDatabase() {

    abstract fun RecentAppDao(): RecentAppDao

}