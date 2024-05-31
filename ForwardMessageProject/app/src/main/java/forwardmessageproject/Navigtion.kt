package forwardmessageproject

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import forwardmessageproject.ui.NavigationItem

@Composable
fun MainScreen(activity: MainActivity) {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopBar(activity) },
        bottomBar = { BottomNavigationBar(navController) },
        content = { padding ->
            Box(modifier = Modifier
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
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
            //HistoryScreen(activity)
        }
        composable(NavigationItem.Badges.route) {
            //BadgesScreen()
        }
/*        Start screen -> welcom or something
        Home screen with button add group and list of groups

*/
    }
}

@Composable
fun TopBar(activity: MainActivity) {
    TopAppBar(
        title = {
            Text(
                text = "Forward your messages",
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.wrapContentSize(),
                textAlign = TextAlign.Center
            )
            // log out button
        },
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.History,
        NavigationItem.Badges
    )
    BottomNavigation(
        backgroundColor = Color.Magenta,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = if (currentRoute == item.route) Color.Gray else Color.Gray.copy(0.4f),
                    )
                },
                label = { Text(text = item.title, color = if (currentRoute == item.route) Color.Gray else Color.Gray.copy(0.4f)) },
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