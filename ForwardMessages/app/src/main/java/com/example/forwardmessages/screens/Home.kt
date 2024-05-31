package com.example.forwardmessages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(activity: MainActivity, navController: NavHostController) {
    Scaffold(
        topBar = { TopBar(activity) },
        content = { padding ->
            Box(modifier = Modifier
                .padding(padding)
                .background(colorResource(R.color.LightBrown3))
            ) {
                GroupsList(activity, navController)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/
                    navController.navigate("history"){
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                containerColor = colorResource(R.color.DarkBrown2)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add Group",
                    tint = colorResource(R.color.LightBrown4)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    )
}

@Composable
fun GroupsList(activity: MainActivity, navController: NavHostController){
    val groups by activity.getGroups().collectAsState(initial = emptyList())
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        LazyColumn(){
            items(groups){group ->
                GroupItem(group, navController)
            }
        }
    }
}

@Composable
fun GroupItem(group: Group, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Row(){
                Text("${group.name.uppercase()} -> ID:${group.id}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.DarkBrown)
                )
            }
            Row(){
                if(group.phoneNumber != "") {
                    Text(
                        "Phone number: ${group.phoneNumber}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(R.color.DarkBrown).copy(alpha = 0.6f)
                    )
                }
                if(group.contentText != "") {
                    Text(
                        "Message: ${group.contentText}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(R.color.DarkBrown).copy(alpha = 0.6f)
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = androidx.compose.ui.Alignment.End
        ){
            IconButton(onClick = {
                navController.navigate("showGroup/${group.id}") {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }){
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Group Info",
                    tint = colorResource(R.color.DarkBrown2)
                )
            }
        }
    }
    // TODO("Not yet implemented")
}
