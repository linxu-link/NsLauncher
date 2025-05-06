package com.wujia.nslauncher.data.recentapp.local.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "recentApp"
)
data class RecentApp(
    @PrimaryKey val pkg: String,
)