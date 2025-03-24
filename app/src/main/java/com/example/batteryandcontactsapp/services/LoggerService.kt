package com.example.batteryandcontactsapp.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.IBinder
import android.util.Log
import java.util.Date
import java.util.Locale

const val EXTRA_LOG_MESSAGE = "extra_log_message"
const val UNKNOWN_LOG_MESSAGE = "Unknown log message"

class LoggerService: Service(){

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        logAction("Logger started")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.getStringExtra(EXTRA_LOG_MESSAGE) ?: UNKNOWN_LOG_MESSAGE
        logAction(action)
        return START_STICKY
    }

    override fun onDestroy() {
        logAction("Service stopped")
        super.onDestroy()
    }

    private fun logAction(action: String) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val logMessage = "$timestamp - $action\n"

        Log.d("AppLoggerService", logMessage)
    }

    companion object {
        fun log(context: Context, message: String) {
            val intent = Intent(context, LoggerService::class.java)
            intent.putExtra(EXTRA_LOG_MESSAGE, message)
            context.startService(intent)
        }
    }



}