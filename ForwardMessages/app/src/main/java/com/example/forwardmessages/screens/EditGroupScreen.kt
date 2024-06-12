package com.example.forwardmessages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

// TODO change delete group beacouse now it does not work
@Composable
fun EditGroupScreen(activity: MainActivity, navController: NavHostController, groupId: Int){
    Scaffold(
        topBar = { TopBar(activity) },
        content = { padding ->
            Box(modifier = Modifier
                .padding(padding)
                .background(colorResource(R.color.LightBrown3))
            ) {
                EditGroupInfo(activity,navController, groupId)
            }
        },
        bottomBar = { BottomNavGroupEditBar(activity = activity, navController = navController, groupId = groupId) }
    )
}

@Composable
fun EditGroupInfo(activity: MainActivity, navController: NavHostController, groupId: Int) {
    val group = activity.getGroup(groupId).collectAsState(initial = null).value
    if(group != null) {
        var groupName by rememberSaveable { mutableStateOf("${group.name}") }
        var phoneNumber by rememberSaveable { mutableStateOf("${group.phoneNumber}") }
        var messageCharacters by rememberSaveable { mutableStateOf("${group.contentText}") }
        val localContext = LocalContext.current

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(){
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = groupName,
                        onValueChange = { newValue ->
                            groupName = newValue
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
                        value = phoneNumber,
                        onValueChange = { newValue ->
                            phoneNumber = newValue
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
                        value = messageCharacters,
                        onValueChange = { newValue ->
                            messageCharacters = newValue
                        },
                        label = { Text("Message characters to track: ") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(focusedContainerColor = colorResource(R.color.LightBrown4), unfocusedContainerColor = colorResource(R.color.LightBrown4))
                    )
                }
                Row(){
                    Button(onClick = {
                        if (groupName != "") {
                            val newGroup = Group(
                                id = group.id,
                                name = groupName.uppercase(),
                                phoneNumber = phoneNumber,
                                contentText = messageCharacters,
                                enabled = group.enabled
                            )
                            activity.updateGroup(group = newGroup)
                            navController.navigate("home") {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                    }
                                }
                                launchSingleTop = true
                            }
                        }
                        else{
                            // Show error message
                            Log.d("AddGroupScreen", "Group name is empty")
                            Toast.makeText(activity.applicationContext, "Please enter group Name", Toast.LENGTH_SHORT).show()
                        }
                    }){Text("Save Changes")}
                }
            }
        }
    }
}


@Composable
fun BottomNavGroupEditBar(activity: MainActivity,
                          navController: NavController,
                          groupId: Int) {
    val iconColor = colorResource(R.color.LightBrown4)
    val group = activity.getGroup(groupId).collectAsState(initial = null).value
    BottomNavigation(
        backgroundColor = colorResource(R.color.DarkBrown2),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Group Info",
                    tint = iconColor,
                )
            },
            label = { Text(text = "Back", color = iconColor) },
            alwaysShowLabel = true,
            selected = currentRoute == "showGroup/${groupId}",
            onClick = {
                navController.navigate("showGroup/${groupId}") {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                        }
                    }
                    launchSingleTop = true
                }
            }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete Group",
                    tint = iconColor
                )
            },
            label = { Text(text = "Delete", color = iconColor) },
            alwaysShowLabel = true,
            selected = currentRoute == "home",
            onClick = {
                activity.deleteGroup(group = group!!)
                navController.navigate("home") {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                        }
                    }
                    launchSingleTop = true
                }
            }
        )
    }
}
