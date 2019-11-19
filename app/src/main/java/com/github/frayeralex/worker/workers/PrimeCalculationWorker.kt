package com.github.frayeralex.worker.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class PrimeCalculationWorker (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        var currentCheckInt = 1
        while (!this.isStopped) {
            if (isPrime(currentCheckInt)) {
                setProgressAsync(Data.Builder().putInt(PROGRESS, currentCheckInt).build())
                Log.d("FOO", currentCheckInt.toString())
            }
            TimeUnit.MILLISECONDS.sleep(100)
            currentCheckInt++;
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