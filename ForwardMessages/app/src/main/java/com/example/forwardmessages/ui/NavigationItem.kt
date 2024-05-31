package com.example.forwardmessages

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    data object Home : NavigationItem("home", R.drawable.baseline_home, "Home")
    data object History : NavigationItem("history", R.drawable.baseline_history, "History")
    data object GroupInfo : NavigationItem("showGroup", R.drawable.baseline_badges, "Badges")
}