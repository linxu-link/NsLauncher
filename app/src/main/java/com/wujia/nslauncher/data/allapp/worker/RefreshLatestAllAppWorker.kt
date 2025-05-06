package com.wujia.nslauncher.data.allapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.wujia.nslauncher.data.allapp.AllAppRepository

class RefreshLatestAllAppWorker(
    private val allAppRepository: AllAppRepository,
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = try {
        allAppRepository.refreshAllApps()
        Result.success()
    } catch (error: Throwable) {
        Result.failure()
    }

}