package com.wujia.nslauncher.data.quickapp.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface QuickAppDao {

    @Query("SELECT * FROM quickApp")
    fun observeAll(): Flow<List<QuickApp>>

    @Query("SELECT * FROM quickApp")
    suspend fun getAll(): List<QuickApp>

    @Upsert
    suspend fun upsert(task: QuickApp)

    @Upsert
    suspend fun upsertAll(tasks: List<QuickApp>)
}