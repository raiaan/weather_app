package com.example.weatherapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp.MainActivity
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.network.RetrofitService
import com.example.weatherapp.repositories.OnlineRepository
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit


class AlertWorker(val appContext: Context, workerParams: WorkerParameters):Worker (appContext, workerParams){
    lateinit var w: WeatherResponse
    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        Log.i("TAG", "doWork: ")
        if (true) {
            sendNotification()
        } else {
            sendNotification()
        }
        return Result.success()
    }
    fun getOnlineWeatherData(){
        var job: Job? = null
        val mainRepository = OnlineRepository(RetrofitService.getInstance(),appContext)
        job = CoroutineScope(Dispatchers.IO ).launch {
            val response = mainRepository. getWeatherData()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    w = response.body()!!
                }
            }
        }
    }
    private fun createNotificationChannel() {
        Log.i("TAG", "createNotificationChannel: ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id: String ="notificationChannelID"
            val description: String ="notificationChannelDescription"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, description, importance)
            /*Register the channel with the system; you can't change the importance
             or other notification behaviors after this*/
            val notificationManager = applicationContext.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID", "Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val nm = applicationContext.getSystemService(
                NotificationManager::class.java
            )
            nm.createNotificationChannel(channel)
        }
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pi = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val builder= NotificationCompat.Builder(applicationContext, "CHANNEL_ID_123")
        builder.setContentTitle("Medicine Time")
            .setContentText("test contetn")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pi)
        val nmc = NotificationManagerCompat.from(applicationContext)
        nmc.notify(1, builder.build())
    }

}