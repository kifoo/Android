package com.example.forwardmessages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


// TODO add enable disable button
@Composable
fun ShowGroupScreen(activity: MainActivity, navController: NavHostController, groupId: Int){
    Scaffold(
        topBar = { TopBar(activity) },
        content = { padding ->
            Box(modifier = Modifier
                .padding(padding)
                .background(colorResource(R.color.LightBrown3))
            ) {
                GroupInfo(activity, groupId)
            }
        },
        bottomBar = { BottomNavGroupInfoBar(navController = navController, groupId = groupId) }
    )
}


@Composable
fun GroupInfo(activity: MainActivity, groupId: Int){
    val group = activity.getGroup(groupId).collectAsState(initial = null).value
    val groupsWithCondition = activity.getGroupListByConditions("", "884182605").collectAsState(initial = null).value

    val contacts = activity.getContacts(groupId).collectAsState(initial = emptyList()).value
    if(group != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.LightBrown3))
        ){
            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(group.name.uppercase(),
                        color = colorResource(R.color.DarkBrown)
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text("Informations to track for forwarding messages in this group", color = colorResource(R.color.DarkBrown).copy(alpha = 0.6f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (group.phoneNumber != "") {
                        Text(
                            "Phone number: ${group.phoneNumber}",
                            color = colorResource(R.color.DarkBrown).copy(alpha = 0.8f)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    if (group.contentText != "") {
                        Text(
                            "Message: ${group.contentText}",
                            color = colorResource(R.color.DarkBrown).copy(alpha = 0.8f)
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text("Contacts: ", color = colorResource(R.color.DarkBrown).copy(alpha = 0.8f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ){
                    LazyColumn(){
                        items(contacts){contact ->
                            Row(
                                modifier = Modifier.fillMaxSize()
                            ){
                                Text(contact.name, color = colorResource(R.color.DarkBrown))
                            }
                            Row(){
                                Text(contact.phoneNumber,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorResource(R.color.DarkBrown).copy(alpha = 0.6f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavGroupInfoBar(navController: NavController, groupId: Int) {
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
                    contentDescription = "Add Group",
                    tint = iconColor,
                )
            },
            label = { Text(text = "Home", color = iconColor) },
            alwaysShowLabel = true,
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
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
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit Group",
                    tint = iconColor,
                )
            },
            label = { Text(text = "Edit", color = iconColor) },
            alwaysShowLabel = true,
            selected = currentRoute == "edit/${groupId}",
            onClick = {
                navController.navigate("edit/${groupId}") {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            //saveState = true
                        }
                    }
                    launchSingleTop = true
                    //restoreState = true
                }
            }
        )
    }
}