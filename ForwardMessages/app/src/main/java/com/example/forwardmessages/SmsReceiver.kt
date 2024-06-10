package com.example.forwardmessages

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            var smsSender = ""
            var smsBody = ""
            // Process the received SMS messages
            applicationScope.launch {
                for (smsMessage in smsMessages) {
                    smsSender = smsMessage.displayOriginatingAddress ?: "" // Handle potential null
                    smsBody += smsMessage.messageBody
                }
                if (smsSender.first() == '+') {
                    smsSender = smsSender.substring(3)
                }
                processIncomingMessage(smsBody, smsSender, context)
            }
        }
    }
    private suspend fun processIncomingMessage(messageBody: String, senderNumber: String, context: Context) {
        // Calling the processIncomingMessage function from MainActivity
        (context as MainActivity).processIncomingMessage(messageBody, senderNumber)
    }
}