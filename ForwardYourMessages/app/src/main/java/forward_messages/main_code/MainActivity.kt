package forward_messages.main_code

import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import forward_messages.main_code.ui.theme.ForwardYourMessagesTheme
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    private lateinit var smsReceiver: SmsReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ForwardYourMessagesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        smsReceiver = SmsReceiver()
        registerSmsReceiver()

    }

    // Handle exceptions in processIncomingMessage
    suspend fun processIncomingMessage(messageBody: String, senderNumber: String) {
        Toast.makeText(this, "processIncomingMessage", Toast.LENGTH_SHORT).show()
        try {
            Toast.makeText(this, "$senderNumber : $messageBody", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("processIncomingMessage", "Error processing incoming message", e)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, register the broadcast receiver
                registerSmsReceiver()
            } else {
                // Permission denied, show an error message
                runOnUiThread {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun hasSmsPermission(context: Context): Boolean {
        val readSmsPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
        val sendSmsPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
        val receivedSmsPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
        val readPhoneStatePermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
        return readSmsPermission && sendSmsPermission && receivedSmsPermission && readPhoneStatePermission
    }

    fun requestSmsPermission(activity: Activity) {
        val permissions = arrayOf(android.Manifest.permission.READ_SMS, android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.READ_PHONE_STATE)
        ActivityCompat.requestPermissions(activity, permissions, 2)
    }

    private fun registerSmsReceiver() {
        if(!hasSmsPermission(this)){
            requestSmsPermission(this)
        }
        else{
            //val intentFilter = IntentFilter(android.provider.Telephony.RECEIVE_SMS)
            val intentFilter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
            registerReceiver(smsReceiver, intentFilter)
            Toast.makeText(this, "SmsReceiver registered", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsReceiver)
        Toast.makeText(this, "SmsReceiver unregistered", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ForwardYourMessagesTheme {
        Greeting("Android")
    }
}