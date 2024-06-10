package forward_messages.main_code

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.GnssAntennaInfo
import android.os.Bundle
import android.provider.Telephony
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
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
            var smsPhoneNumber = ""
            // Process the received SMS messages
            applicationScope.launch {
                for (smsMessage in smsMessages) {
                    smsSender = smsMessage.displayOriginatingAddress ?: "" // Handle potential null
                    smsBody += smsMessage.messageBody
                }
                processIncomingMessage(smsBody, smsSender, context)
            }
        }
    }

    private suspend fun processIncomingMessage(messageBody: String, senderNumber: String, context: Context) {
        // Assuming MainActivity has a method to handle incoming messages
        (context as? MainActivity)?.processIncomingMessage(messageBody, senderNumber)
    }

}