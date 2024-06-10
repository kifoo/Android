package com.example.forwardmessages

import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.forwardmessages.ui.theme.ForwardMessagesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
    }
}

class MainActivity : ComponentActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var smsReceiver: SmsReceiver
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    var selectedContacts: MutableState<List<Contact>> = mutableStateOf(listOf())
    var context: Context = this

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
        ).fallbackToDestructiveMigration()
            .addMigrations(MIGRATION_1_2)
            .build()

        // BroadcastReceiver to listen for incoming SMS
        smsReceiver = SmsReceiver()
        registerSmsReceiver()

        setContent {
            ForwardMessagesTheme {
                MainScreen(this)
            }
        }
    }

    //  SMS RECEIVER FUNCTIONS
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

    fun sendSms(phoneNumber: String, message: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        runOnUiThread {
            Toast.makeText(this, "Message sent to $phoneNumber", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle exceptions in processIncomingMessage
    suspend fun processIncomingMessage(messageBody: String, senderNumber: String) {
        try {
            val groupsList = withContext(Dispatchers.IO) {
                getGroupListByConditions(messageBody, senderNumber).first()
            }
            Log.d("processIncomingMessage", "Processing messages for ${groupsList.size} groups")
            // Send the message to the contacts in those groups
            groupsList.forEach { group ->
                Log.d("processIncomingMessage", "Group: $group")
                if (group.enabled) {
                    val contacts = withContext(Dispatchers.IO) { daoDB.databaseDao().getContactsByGroupId(group.id).first() }
                    contacts.forEach { contact ->
                        sendSms(contact.phoneNumber, messageBody)
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

    // Checking if permission is granted
    fun hasContactPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED;
    }

    // Requesting permissions if permission is not granted.
    fun requestContactPermission(context: Context, activity: Activity) {
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
        } else {
            null!!
        }
    }

    fun getGroups(): Flow<List<Group>> = daoDB.databaseDao().getAllGroups()
    fun getGroup(groupId: Int): Flow<Group> = daoDB.databaseDao().getGroupById(groupId)
    fun getContacts(groupId: Int): Flow<List<Contact>> = daoDB.databaseDao().getContactsByGroupId(groupId)
    private fun checkIfContactExists(contact: Contact): Flow<Boolean> = daoDB.databaseDao().checkIsContactExists(contact.name, contact.phoneNumber)
    private fun checkIfRelationshipExists(groupId: Int, contactId: String): Flow<Boolean> = daoDB.databaseDao().checkIfRelationshipExists(groupId, contactId)
    fun updateGroupEnabled(group: Group, enabled: Boolean){
        lifecycleScope.launch(Dispatchers.IO) {
            daoDB.databaseDao().updateGroupEnabled(enabled, group.id)
        }
    }
    fun updateGroup(group: Group){
        lifecycleScope.launch(Dispatchers.IO) {
            daoDB.databaseDao().update(group)
        }
    }
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

