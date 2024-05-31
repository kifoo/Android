package com.example.forwardmessages

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.forwardmessages.ui.theme.ForwardMessagesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
    }
}

class MainActivity : ComponentActivity() {
    private lateinit var smsReceiver: BroadcastReceiver
    var selectedContacts: MutableState<List<Contact>> = mutableStateOf(listOf())

    companion object {
        lateinit var daoDB: AppDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        daoDB = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "forward-messages-db"
        )//.fallbackToDestructiveMigration()
            //.addMigrations(MIGRATION_1_2)
            .build()

        // BroadcastReceiver to listen for incoming SMS
        registerSmsReceiver()

        setContent {
            ForwardMessagesTheme {
                MainScreen(this)
            }
        }
    }
    // SMS RECEIVER FUNCTIONS

    fun hasSmsPermission(context: Context): Boolean {
        val readSmsPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
        val sendSmsPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
        return readSmsPermission && sendSmsPermission
    }

    fun requestSmsPermission(activity: Activity) {
        val permissions = arrayOf(android.Manifest.permission.READ_SMS, android.Manifest.permission.SEND_SMS)
        ActivityCompat.requestPermissions(activity, permissions, 2)
    }

    private fun registerSmsReceiver() {
        // Check if the app has SMS permissions
        if (!hasSmsPermission(this)) {
            // Request SMS permissions if not
            requestSmsPermission(this)
        }
        var senderNumber = mutableStateOf<String>("")
        var messageBody = mutableStateOf<String>("")
        smsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "android.provider.Telephony.SMS_RECEIVED") {
                    Toast.makeText(context, "Message received", Toast.LENGTH_SHORT).show()
                    Log.d("smsReceiver", "Message received")
                    val bundle = intent.extras
                    val pdus = bundle?.get("pdus") as Array<Any>
                    val messages = arrayOfNulls<SmsMessage>(pdus.size)
                    for (i in pdus.indices) {
                        messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                    }
                    // Processing the incoming message
                    lifecycleScope.launch(Dispatchers.IO) {
                        messages.forEach { message ->
                            message?.let {
                                senderNumber.value = message.displayOriginatingAddress
                                messageBody.value = message.displayMessageBody
                                Toast.makeText(context, "Message from $senderNumber: $messageBody", Toast.LENGTH_SHORT).show()
                                processIncomingMessage(messageBody, senderNumber)
                            }
                        }
                    }
                }
            }
        }
        registerReceiver(smsReceiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }

    private fun sendSms(phoneNumber: String, message: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        Toast.makeText(this, "Message sent to $phoneNumber", Toast.LENGTH_SHORT).show()
    }

    // Handle exceptions in processIncomingMessage
    private suspend fun processIncomingMessage(messageBody: MutableState<String>, senderNumber: MutableState<String>) {
        try {
            val groupsList = getGroupListByConditions(messageBody.value, senderNumber.value).first()
            // Send the message to the contacts in those groups
            groupsList.forEach { group ->
                Log.d("processIncomingMessage", "Group: $group")
                if(group.enabled) {
                    val contacts = daoDB.databaseDao().getContactsByGroupId(group.id).first()
                    contacts.forEach { contact ->
                        sendSms(contact.phoneNumber, messageBody.value)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("processIncomingMessage", "Error processing incoming message", e)
        }
    }

    // ENTER CONTACTS FUNCTIONS
    // Defined the contract launcher for picking contacts
    val pickContactContract = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) { uri ->
        uri?.let { contactUri ->
            val cursor = contentResolver.query(contactUri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    val name = it.getString(nameIndex)
                    val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
                    val id = it.getLong(idIndex)

                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id.toString()),
                        null
                    )

                    phoneCursor?.use { phoneCursor ->
                        if (phoneCursor.moveToFirst()) {
                            val phoneNumberIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            val phoneNumber = phoneCursor.getString(phoneNumberIndex)

                            // Update the selectedContacts list
                            selectedContacts.value += Contact(
                                phoneNumber = phoneNumber,
                                name = name
                            )
                        }
                    }
                }
            }
        }
    }



    fun hasContactPermission(context: Context): Boolean {
        // Checking if permission is present or not.
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED;
    }

    fun requestContactPermission(context: Context, activity: Activity) {
        // Requesting permissions if permission is not granted.
        if (!hasContactPermission(context)) {
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_CONTACTS), 1)
        }
    }

    // DATABASE FUNCTIONS
    fun addGroupToDatabase(
        groupName: String,
        phoneNumber: String?,
        messageContent: String?,
        contacts: List<Contact>
    ){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val newGroup = Group(
                    name = groupName.uppercase(),
                    phoneNumber = phoneNumber,
                    contentText = messageContent,
                    enabled = true
                )
                daoDB.databaseDao().insertGroup(newGroup)
                val groupId = getGroupId(newGroup)
                contacts.forEach { contact ->
                    if(!checkIfContactExists(contact).first()) {
                        daoDB.databaseDao().insertContact(contact)
                    }
                    if(!checkIfRelationshipExists(groupId, contact.phoneNumber).first()){
                        val newRelationship =
                            Relationship(groupId = groupId, contactId = contact.phoneNumber)
                        daoDB.databaseDao().insertRelationship(newRelationship)
                    }
                }
            } catch (e: Exception) {
                Log.e("addGroupToDatabase", e.toString())
            }
        }
        // Clear the selectedContacts list after
        // the database operations are completed
        selectedContacts.value = listOf()
    }

    private fun getGroupId(group: Group): Int {
        // Check if phoneNumber and contentText are not null
        if (group.phoneNumber != "" && group.phoneNumber != null && group.contentText != "" && group.contentText != null) {
            return daoDB.databaseDao().getGroupIdWithAllParameters(group.name, group.phoneNumber, group.contentText)
        } else if (group.phoneNumber != "" && group.phoneNumber != null) {
            return daoDB.databaseDao().getGroupIdWithPhoneNumber(group.name, group.phoneNumber.toString())
        } else if (group.contentText != "" && group.contentText != null) {
            return daoDB.databaseDao().getGroupIdWithContentText(group.name, group.contentText.toString())
        } else {
            return daoDB.databaseDao().getGroupIdWithName(group.name)
        }
    }
    fun getGroupListByConditions(message: String, phoneNumber: String): Flow<List<Group>> {
        return if(phoneNumber != ""  && message !=""){
            daoDB.databaseDao().getGroupsByCondition(message, phoneNumber)
        }else if (phoneNumber != "") {
            daoDB.databaseDao().getGroupsByPhoneNumber(phoneNumber)
        } else if (message != "") {
            daoDB.databaseDao().getGroupsByContext(message)
        } else {
            null!!
        }
    }

    fun getGroups(): Flow<List<Group>> = daoDB.databaseDao().getAllGroups()
    fun getGroup(groupId: Int): Flow<Group> = daoDB.databaseDao().getGroupById(groupId)
    fun getContacts(groupId: Int): Flow<List<Contact>> = daoDB.databaseDao().getContactsByGroupId(groupId)
    private fun checkIfContactExists(contact: Contact): Flow<Boolean> = daoDB.databaseDao().checkIsContactExists(contact.name, contact.phoneNumber)
    private fun checkIfRelationshipExists(groupId: Int, contactId: String): Flow<Boolean> = daoDB.databaseDao().checkIfRelationshipExists(groupId, contactId)
    fun deleteGroup(group: Group) {
        lifecycleScope.launch(Dispatchers.IO) {
            val relationships =
                daoDB.databaseDao().getRelationshipsByGroupId(groupId = group.id).first()
            relationships.forEach {
                daoDB.databaseDao().deleteRelationship(it)
            }
            daoDB.databaseDao().deleteGroup(group)
        }
    }
}


@Composable
fun MainScreen(activity: MainActivity) {
    val navController = rememberNavController()
    Scaffold(
        content = { padding ->
            Box(modifier = Modifier
                .padding(padding)
                .background(colorResource(id = R.color.LightBrown3))
            ) {
                Navigation(navController = navController, activity = activity)
            }
        },
        modifier = Modifier
            .fillMaxSize()
    )
}
