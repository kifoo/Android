package com.example.lab4a

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BatteryBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_BATTERY_LOW) {
            Toast.makeText(context, "Battery low", Toast.LENGTH_SHORT).show()
        } else if (action == Intent.ACTION_BATTERY_OKAY) {
            Toast.makeText(context, "Battery okay", Toast.LENGTH_SHORT).show()
        } else if (action == Intent.ACTION_POWER_CONNECTED) {
            Toast.makeText(context, "Power connected", Toast.LENGTH_SHORT).show()
        } else if (action == Intent.ACTION_POWER_DISCONNECTED) {
            Toast.makeText(context, "Power disconnected", Toast.LENGTH_SHORT).show()
        }
    }
}