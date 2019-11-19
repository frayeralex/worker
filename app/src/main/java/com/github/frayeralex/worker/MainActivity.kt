package com.github.frayeralex.worker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.work.*
import com.github.frayeralex.worker.workers.PrimeCalculationWorker
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {
    private val wm by lazy { WorkManager.getInstance(this) }
    private var primeList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startCalculation(v: View) {
        primeList.clear()
        val work = OneTimeWorkRequestBuilder<PrimeCalculationWorker>()
            .addTag(TAG)
            .build()

        wm.enqueue(work)

        wm.getWorkInfoByIdLiveData(work.id).observe(this, Observer {
            it?.progress?.getInt(PrimeCalculationWorker.PROGRESS, DEFAULT_VALUE)?.apply {
                if (this != DEFAULT_VALUE && !primeList.contains(this)) {
                    primeList.add(this)
                    updateUi()
                }
            }
        })
    }

    fun stopCalculation(v: View) {
        wm.cancelAllWorkByTag(TAG)
        primeList.clear()
        updateUi()
    }

    override fun onDestroy() {
        wm.cancelAllWork()
        super.onDestroy()
    }

    private fun updateUi() {
        if (primeList.isNotEmpty()) {
            displayImage(primeList[primeList.size - 1].toString())
        } else {
            displayImage(resources.getString(R.string.app_name))
        }
    }

    private fun displayImage(text: String) {
        textView.text = text;

    }

    companion object {
        private val TAG = "CALC_VALUE"
        private val DEFAULT_VALUE = 1
    }
}
