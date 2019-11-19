package com.github.frayeralex.worker

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.github.frayeralex.worker.workers.PrimeCalculationWorker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val wm by lazy { WorkManager.getInstance(this) }
    private var primeList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState is Bundle && savedInstanceState.containsKey(LAST_PRIME_INT)) {
            Toast.makeText(
                this,
                "LAST PRIME NUMBER :" + savedInstanceState.getInt(LAST_PRIME_INT).toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (primeList.isNotEmpty()) {
            outState.putInt(LAST_PRIME_INT, primeList.get(primeList.lastIndex))
        }
        super.onSaveInstanceState(outState)
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
    }

    private fun updateUi() {
        if (primeList.isNotEmpty()) {
            displayImage(primeList[primeList.lastIndex].toString())
        }
    }

    private fun displayImage(text: String) {
        textView.text = text

    }

    companion object {
        private val TAG = "CALC_VALUE"
        private val DEFAULT_VALUE = 1
        private val LAST_PRIME_INT = "LAST_PRIME_INT"
    }
}
