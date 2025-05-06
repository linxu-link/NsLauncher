package com.wujia.nslauncher.data.recentapp.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentAppDao {

    @Query("SELECT * FROM recentApp")
    fun observeAll(): Flow<List<RecentApp>>

    @Query("SELECT * FROM recentApp")
    suspend fun getAll(): List<RecentApp>

    @Upsert
    suspend fun upsert(task: RecentApp)

    @Upsert
    suspend fun upsertAll(tasks: List<RecentApp>)
}