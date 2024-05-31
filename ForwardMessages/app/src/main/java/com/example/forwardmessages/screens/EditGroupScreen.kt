package com.example.forwardmessages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
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
        bottomBar = { BottomNavGroupEditBar(navController = navController, groupId = groupId) }
    )
}

@Composable
fun EditGroupInfo(activity: MainActivity, navController: NavHostController, groupId: Int) {
    val group = activity.getGroup(groupId).collectAsState(initial = null).value
    if(group != null) {
        var groupName by rememberSaveable { mutableStateOf("${group.name}") }
        var phoneNumber by rememberSaveable { mutableStateOf("${group.phoneNumber}") }
        var messageCharacters by rememberSaveable { mutableStateOf("") }
        //val selectedContacts by activity.selectedContacts.collectAsState()
        val localContext = LocalContext.current

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(){
                Row() {
                    Text(text = group.name)
                }
                if(group.phoneNumber != "") {
                    Row() {
                        group.phoneNumber?.let { Text(text = it) }
                    }
                }
                if(group.contentText != "") {
                    Row() {
                        Text(text = messageCharacters)
                    }
                }
                Row(){
                    Button(onClick = {
                        activity.deleteGroup(group = group)
                        navController.navigate("home") {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
//                            saveState = true
                                }
                            }
                            launchSingleTop = true
//                    restoreState = true
                        }
                    }){Text("Delete Group")}
                }
            }
        }
    }
}


@Composable
fun BottomNavGroupEditBar(navController: NavController, groupId: Int) {
    val iconColor = colorResource(R.color.LightBrown4)
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
//                            saveState = true
                        }
                    }
                    launchSingleTop = true
//                    restoreState = true
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
            selected = currentRoute == "edit/${groupId}",
            onClick = {
//                TODO: delete group ( and contacts it there will be time...)
//                And go back to group info
                navController.navigate("showGroup/${groupId}") {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
//                            saveState = true
                        }
                    }
                    launchSingleTop = true
//                    restoreState = true
                }
            }
        )
    }
}
