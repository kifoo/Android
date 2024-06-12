package com.example.forwardmessages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


// Maybe later change to AlertDialog from lab5
@Composable
fun AddGroupScreen(activity: MainActivity, navController: NavController){
    var groupName:MutableState<String> = rememberSaveable { mutableStateOf("") }
    var phoneNumber:MutableState<String> = rememberSaveable { mutableStateOf("") }
    var messageCharacters:MutableState<String> = rememberSaveable { mutableStateOf("") }
    val selectedContacts:MutableState<List<Contact>> = activity.selectedContacts

    LaunchedEffect(Unit) {
        groupName.value = ""
        phoneNumber.value = ""
        messageCharacters.value = ""
    }

    Scaffold(
        topBar = { TopBar(activity) },
        content = { padding ->
            AddGroupForm(
                activity = activity,
                navController = navController,
                padding = padding,
                groupName = groupName,
                phoneNumber = phoneNumber,
                messageCharacters = messageCharacters,
                selectedContacts = selectedContacts
            )
        },
        bottomBar = {
            BottomNavigationAddGroupBar(
                activity = activity,
                navController = navController,
                groupName = groupName,
                phoneNumber = phoneNumber,
                messageCharacters = messageCharacters,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGroupForm(activity: MainActivity,
                 navController: NavController,
                 padding: PaddingValues,
                 groupName: MutableState<String>,
                 phoneNumber: MutableState<String>,
                 messageCharacters: MutableState<String>,
                 selectedContacts: MutableState<List<Contact>>
){
    val localContext = LocalContext.current
    var showEnterContactRow = rememberSaveable { mutableStateOf(false) }
    var contactManualNumber = rememberSaveable { mutableStateOf("") }
    var contactManualName = rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        contactManualNumber.value = ""
        contactManualName.value = ""
        showEnterContactRow.value = false
    }

    Box(modifier = Modifier
        .padding(padding)
        .background(colorResource(id = R.color.LightBrown3))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = groupName.value,
                    onValueChange = { newValue ->
                        groupName.value = newValue
                    },
                    label = { Text("Group name: ") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(focusedContainerColor = colorResource(R.color.LightBrown4), unfocusedContainerColor = colorResource(R.color.LightBrown4))
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = phoneNumber.value,
                    onValueChange = { newValue ->
                        phoneNumber.value = newValue
                    },
                    label = { Text("Phone Number to track: ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(focusedContainerColor = colorResource(R.color.LightBrown4), unfocusedContainerColor = colorResource(R.color.LightBrown4))
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = messageCharacters.value,
                    onValueChange = { newValue ->
                        messageCharacters.value = newValue
                    },
                    label = { Text("Message characters to track: ") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(focusedContainerColor = colorResource(R.color.LightBrown4), unfocusedContainerColor = colorResource(R.color.LightBrown4))
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    Log.d("AddGroupScreen", "Button clicked on contacts")
                    if (activity.hasContactPermission(context = localContext)) {
                        activity.pickContactContract.launch(null)
                    }
                    else{
                        activity.requestContactPermission(context = localContext, activity = activity)
                    }
                }) {
                    Text("Choose contacts")
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    showEnterContactRow.value = !showEnterContactRow.value
                }) {
                    Text("Enter Contact")
                }
            }
            if (showEnterContactRow.value) {
                Spacer(modifier = Modifier.padding(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextField(
                        value = contactManualName.value,
                        onValueChange = { newValue ->
                            contactManualName.value = newValue
                        },
                        label = { Text("Enter Contact Name: ") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colorResource(R.color.LightBrown4),
                            unfocusedContainerColor = colorResource(R.color.LightBrown4)
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ){
                    TextField(
                        value = contactManualNumber.value,
                        onValueChange = { newValue ->
                            contactManualNumber.value = newValue
                        },
                        label = { Text("Enter Contact Number: ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(focusedContainerColor = colorResource(R.color.LightBrown4), unfocusedContainerColor = colorResource(R.color.LightBrown4))
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = {
                            if((contactManualName.value != "") and (contactManualNumber.value != "")) {
                                val newContact =
                                    Contact(contactManualName.value, contactManualNumber.value)
                                activity.selectedContacts.value += newContact
                                contactManualName.value = ""
                                contactManualNumber.value = ""
                            }
                            else{
                                // Show error message
                                Log.d("AddGroupScreen", "Group name is empty")
                                Toast.makeText(activity.applicationContext, "Please enter cntact info", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.padding(8.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = colorResource(
                                R.color.DarkBrown2
                            )
                        )
                    ) {
                        Icon(
                            imageVector = Icons.TwoTone.Add,
                            contentDescription = "Add contact",
                            tint = colorResource(R.color.LightBrown4)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                LazyColumn(
                ){
                    items(selectedContacts.value){contact ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Text(contact.name, style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Text(contact.phoneNumber, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun BottomNavigationAddGroupBar(activity: MainActivity,
                                navController: NavController,
                                groupName: MutableState<String>,
                                phoneNumber: MutableState<String>,
                                messageCharacters: MutableState<String>
) {
    val iconColor = colorResource(R.color.LightBrown4)
    BottomNavigation(
        backgroundColor = colorResource(R.color.DarkBrown2),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Cancel",
                    tint = iconColor,
                )
            },
            label = { Text(text = "Cancel", color = iconColor) },
            alwaysShowLabel = true,
            selected = currentRoute == "home",
            onClick = {
                activity.selectedContacts.value = emptyList()
                navController.navigate("home") {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Done,
                    contentDescription = "Add",
                    tint = iconColor,
                )
            },
            label = { Text(text = "Create", color = iconColor) },
            alwaysShowLabel = true,
            selected = currentRoute == "home",
            onClick = {
                if(groupName.value != "") {
                    // Add to database
                    activity.addGroupToDatabase(
                        groupName.value,
                        phoneNumber.value,
                        messageCharacters.value,
                        activity.selectedContacts.value
                    )
                    // Go to home screen
                    navController.navigate("home") {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                else{
                    // Show error message
                    Log.d("AddGroupScreen", "Group name is empty")
                    Toast.makeText(activity.applicationContext, "Please enter group Name", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}