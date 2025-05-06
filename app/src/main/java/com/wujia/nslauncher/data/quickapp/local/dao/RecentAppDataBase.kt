package com.wujia.nslauncher.data.quickapp.local.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [QuickApp::class], version = 1, exportSchema = false)
abstract class QuickAppDataBase : RoomDatabase() {

    abstract fun QuickAppDao(): QuickAppDao

}