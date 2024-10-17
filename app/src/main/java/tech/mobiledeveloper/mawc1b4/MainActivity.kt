package tech.mobiledeveloper.mawc1b4

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import tech.mobiledeveloper.mawc1b4.alarm.SampleAlarm
import tech.mobiledeveloper.mawc1b4.job.SampleJob
import tech.mobiledeveloper.mawc1b4.service.SampleIntentService
import tech.mobiledeveloper.mawc1b4.service.SampleService
import tech.mobiledeveloper.mawc1b4.service.ServiceType
import tech.mobiledeveloper.mawc1b4.ui.theme.MAWC1B4Theme
import tech.mobiledeveloper.mawc1b4.workmanager.SampleWorkManager
import java.util.Calendar


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MAWC1B4Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            RegularButton("Start Regular Service") {
                                val intent = Intent(applicationContext, SampleService::class.java)
                                val bundle = Bundle()
                                bundle.putInt(SampleService.SERVICE_TYPE, ServiceType.Regular.value)
                                intent.putExtras(bundle)
                                startService(intent)
                            }
                            Spacer(modifier = Modifier.padding(top = 20.dp))
                            RegularButton("Start Regular Service") {
                                val intent = Intent(applicationContext, SampleService::class.java)
                                val bundle = Bundle()
                                bundle.putInt(SampleService.SERVICE_TYPE, ServiceType.Foreground.value)
                                intent.putExtras(bundle)
                                startService(intent)
                            }
                            Spacer(modifier = Modifier.padding(top = 20.dp))
                            RegularButton("Start Intent Service") {
                                val intent = Intent(applicationContext, SampleIntentService::class.java)
                                startService(intent)
                            }
                            Spacer(modifier = Modifier.padding(top = 20.dp))
                            RegularButton("Start Alarm Manager") {
                                val calendar = Calendar.getInstance()
                                calendar.add(Calendar.SECOND, 10)
                                val intent = Intent(applicationContext, SampleAlarm::class.java)
                                val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                                val alarmManager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager
                                alarmManager?.let {
                                    try {
                                        it.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                                    } catch (e: SecurityException) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.padding(top = 20.dp))
                            RegularButton("Start Work Manager") {
                                val constraints = Constraints.Builder()
                                    .setRequiresCharging(false)
                                    .setRequiredNetworkType(NetworkType.CONNECTED)
                                    .setRequiresBatteryNotLow(true)
                                    .setRequiresStorageNotLow(true)
                                    .setRequiresDeviceIdle(true)
                                    .build()

                                val worker = OneTimeWorkRequest.Builder(SampleWorkManager::class.java)
                                    .setConstraints(constraints)
                                    .build()

                                WorkManager.getInstance(applicationContext).enqueue(worker)
                            }

                            Spacer(modifier = Modifier.padding(top = 20.dp))
                            RegularButton("Start Job Scheduler") {
                                val componentName = ComponentName(applicationContext, SampleJob::class.java)
                                val jobInfo = JobInfo.Builder(1, componentName)
                                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                                    .setPersisted(true)
                                    .setPeriodic((15 * 60 * 1000).toLong())
                                    .build()

                                val jobScheduler =
                                    getSystemService(JOB_SCHEDULER_SERVICE) as? JobScheduler
                                jobScheduler?.let {
                                    val result = jobScheduler.schedule(jobInfo)
                                    if (result == JobScheduler.RESULT_SUCCESS) {
                                        Log.d("TAG", "Job scheduled successfully")
                                    } else {
                                        Log.d("TAG", "Job scheduling failed")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun RegularButton(text: String, onClick: () -> Unit) {
        Button(onClick = onClick) {
            Text(text = text)
        }
    }
}
