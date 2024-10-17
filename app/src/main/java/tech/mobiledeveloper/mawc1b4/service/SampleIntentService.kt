package tech.mobiledeveloper.mawc1b4.service

import android.app.IntentService
import android.content.Intent
import android.util.Log

class SampleIntentService : IntentService("SampleIntentService") {

    private var counter: Int = 0

    override fun onHandleIntent(intent: Intent?) {
        Log.e("TAG", "Intent service handled")
        Thread {
            while (counter < 1_000_000) {
                try {
                    Thread.sleep(1000)
                    counter++
                    Log.e("TAG", "Counter $counter")
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }
}