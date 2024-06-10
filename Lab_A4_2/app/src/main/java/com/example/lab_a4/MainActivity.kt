package com.example.lab_a4

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            DemoA4Content()

        }
    }
}

@Composable
fun DemoA4Content() {  //funkcja odpowiadająca za cały ekran (zawierający trzy różne "Dema"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ){
        Text(
            text = "DEMO A4 Compose",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Divider(modifier = Modifier.height(30.dp))
        DemoPrzelaczanePrzyciski()
        Divider(modifier = Modifier.height(20.dp))
        DemoNapiwek()
        Divider(modifier = Modifier.height(20.dp))
        DemoBatteryBroadcast()
    }

}


@Composable
fun DemoPrzelaczanePrzyciski() {
    var btnStatus by remember { mutableStateOf(1) }
    // 1 - aktywny jest pierwszy przycisk
    // 2 - aktywny jest drugi
    //3 - aktywny jest trzeci
    Column(
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        Text(
            text = "1) Przyciski",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Blue
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // PIERWSZY przycisk, którego kliknięcie aktywuje DRUGI przycisk
            Button(
                onClick = { btnStatus = if (btnStatus == 1) 2 else 1 },
                enabled = ( btnStatus == 1 )
            ) {
                Text(text = "PIERWSZY")
            }
            Spacer(modifier = Modifier.height(30.dp))
            // DRUGI przycisk, którego kliknięcie aktywuje TRZECI przycisk
            Button(
                onClick = { btnStatus = if (btnStatus == 2) 3 else 2 },
                enabled = ( btnStatus == 2 )
            ) {
                Text(text = "DRUGI")
            }
            Spacer(modifier = Modifier.height(30.dp))
            // TRZECI przycisk, którego kliknięcie aktywuje PIERWSZY przycisk
            Button(
                onClick = { btnStatus = if (btnStatus == 3) 1 else 3 },
                enabled = ( btnStatus == 3 )
            ) {
                Text(text = "TRZECI")
            }
        }
    }
}

@Composable
fun DemoNapiwek() {
    var textCost by rememberSaveable { mutableStateOf("") }
    var textCostTip by rememberSaveable { mutableStateOf("---") }
    var textTip by rememberSaveable { mutableStateOf("5") }

    Column(
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        Text(
            text = "2) Napiwek",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Blue
        )
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = textCost,
            onValueChange = {
                textCost = it.replace(",", ".")
            },
            placeholder = {Text("Tutaj wpisz koszt zamówienia")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text( text = "Domyślny napiwek ma teraz stałą wartość 5%",
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray,
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = textTip,
            onValueChange = {
                textTip = it.replace(",", ".")
            },
            placeholder = {Text("Tutaj wpisz nową wartość napiwku w %")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(20.dp))

        val context = LocalContext.current
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                val kosztZamowienia = textCost.toDoubleOrNull()
                val procentNapiwku = textTip.toDoubleOrNull()
                if( kosztZamowienia!= null && procentNapiwku!=null ) {
                    val wartoscNapiwku = kosztZamowienia * procentNapiwku / 100
                    textCostTip = String.format("%.2f zł", wartoscNapiwku)
                } else {
                    textCostTip = "---"
                    Toast.makeText(context,"Niepoprawny koszt zamówienia bądź wartość napiwku", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text(text = "POLICZ NAPIWEK")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Napiwek: " + textCostTip,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun DemoBatteryBroadcast() {
    // zmienne modelu danych -> stanu aplikacji
    val batteryState = remember { mutableStateOf("") }
    SystemBroadcastReceiver(Intent.ACTION_BATTERY_CHANGED) { batteryStatus ->
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        if (level >= 0.3) {
            batteryState.value = "Battery level: $level%"
        } else {
            batteryState.value = "Battery low: $level%"
        }
    }
    SystemBroadcastReceiver(Intent.ACTION_BATTERY_LOW) {    }
    SystemBroadcastReceiver(Intent.ACTION_BATTERY_OKAY) {    }
    SystemBroadcastReceiver(Intent.ACTION_POWER_CONNECTED) {    }
    SystemBroadcastReceiver(Intent.ACTION_POWER_DISCONNECTED) {    }
    Column(
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        Text(
            text = "3) BatteryBroadcast",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Blue
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = batteryState.value,
            fontSize = 20.sp,
            fontStyle = FontStyle.Italic,
            color = Color.DarkGray
        )
    }
}

@Composable
fun SystemBroadcastReceiver(
    systemAction: String,
    onSystemEvent: (intent: Intent?) -> Unit
) {
    val context = LocalContext.current
    val currentOnSystemEvent by rememberUpdatedState(onSystemEvent)

    DisposableEffect(context, systemAction) {
        val intentFilter = IntentFilter(systemAction)
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                currentOnSystemEvent(intent)
                val action = intent?.action
                val message = when (action) {
                    Intent.ACTION_BATTERY_LOW -> "Battery low"
                    Intent.ACTION_BATTERY_OKAY -> "Battery okay"
                    Intent.ACTION_POWER_CONNECTED -> "Power connected"
                    Intent.ACTION_POWER_DISCONNECTED -> "Power disconnected"
                    else -> ""
                }
                if(message != "") { Toast.makeText(context, message, Toast.LENGTH_SHORT).show() }
            }
        }
        context.registerReceiver(broadcast, intentFilter)
        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DemoA4ContentPreview() {
    DemoA4Content()
}