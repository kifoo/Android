package com.example.forwardmessages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


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


@Composable
fun Navigation(navController: NavHostController, activity: MainActivity) {
    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeScreen(activity, navController)
        }
        composable(NavigationItem.History.route) {
            AddGroupScreen(activity, navController,)
        }
        composable("showGroup/{groupId}") { backStackEntry ->
            val groupIdStr = backStackEntry.arguments?.getString("groupId")
            val groupId = groupIdStr?.toInt() ?: throw IllegalArgumentException("No group id provided")
            Log.d("Navigation", "Group ID: $groupId")
            ShowGroupScreen(activity, navController, groupId)
        }
        composable("edit/{groupId}") { backStackEntry ->
            val groupIdStr = backStackEntry.arguments?.getString("groupId")
            val groupId = groupIdStr?.toInt() ?: throw IllegalArgumentException("No group id provided")
            Log.d("Navigation", "Group ID: $groupId")
            EditGroupScreen(activity, navController, groupId)
        }
    }
}

@Composable
fun TopBar(activity: MainActivity) {
    TopAppBar(
        title = {
            Text(
                text = "Forward your messages",
                fontSize = 18.sp,
                color = colorResource(R.color.LightBrown4),
                modifier = Modifier.wrapContentSize(),
                textAlign = TextAlign.Center
            )
            // log out button
        },
        backgroundColor = colorResource(R.color.DarkBrown2),
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.History,
        NavigationItem.GroupInfo
    )
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = if (currentRoute == item.route) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSecondary.copy(0.4f),
                    )
                },
                label = { Text(text = item.title, color = if (currentRoute == item.route) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSecondary.copy(0.4f)) },
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
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
        }
    }
}