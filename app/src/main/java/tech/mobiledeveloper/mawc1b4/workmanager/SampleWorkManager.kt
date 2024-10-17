package tech.mobiledeveloper.mawc1b4.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class SampleWorkManager(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        var counter = 0
        while (counter < 3000) {
            counter++
            Log.e("Worker", "Counter $counter")
        }
        return Result.success()
    }
}