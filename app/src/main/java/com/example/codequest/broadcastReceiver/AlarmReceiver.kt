package com.example.codequest.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.codequest.services.AlarmService

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Start the service to play alarm sound
        val serviceIntent = Intent(context, AlarmService::class.java)
        context.startForegroundService(serviceIntent)
    }
}
