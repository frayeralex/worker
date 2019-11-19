package com.github.frayeralex.worker.workers

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class PrimeCalculationWorker (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        var currentCheckInt = 1
        while (!this.isStopped) {
            if (isPrime(currentCheckInt)) {
                setProgressAsync(Data.Builder().putInt(PROGRESS, currentCheckInt).build())
                TimeUnit.MILLISECONDS.sleep(500)
            }
            currentCheckInt++
        }
        return Result.success()
    }


    private fun isPrime(int: Int): Boolean {
        if (int <= 1) return false

        for (i in 2 until int) {
            if (int % i == 0) {
                return false
            }
        }
        return true
    }

    companion object {
        val PROGRESS = "PROGRESS"
    }
}