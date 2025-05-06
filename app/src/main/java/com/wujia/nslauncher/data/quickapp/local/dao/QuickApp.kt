package com.wujia.nslauncher.data.quickapp.local.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "quickApp"
)
data class QuickApp(
    @PrimaryKey val pkg: String,
)