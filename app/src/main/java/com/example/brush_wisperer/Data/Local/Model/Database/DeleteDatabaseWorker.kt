package com.example.brush_wisperer.Data.Local.Model.Database

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteDatabaseWorker(appContext: Context,workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {

        override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
            try {
                val database = ColourDatabaseInstance.getDatabase(applicationContext)
                database.dao.deleteAllColours()
                Result.success()
            } catch (e: Exception) {
                Result.retry()
            }
        }
}