package tech.mobiledeveloper.mawc1b4.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class SampleAlarm : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "ALARM!", Toast.LENGTH_SHORT).show()
        Log.e("TAG", "Run event")
    }
}