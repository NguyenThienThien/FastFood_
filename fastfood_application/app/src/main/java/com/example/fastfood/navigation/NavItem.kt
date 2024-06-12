package com.example.fastfood.navigation

import com.example.fastfood.R

data class NavItem(
    val label: String,
    val icon: Int,
    val route: String
)

val listOfNavItems = listOf(
    NavItem(
        label = "Home",
        icon = R.drawable.home,
        route = Screens.HomeScreen.name
    ),
    NavItem(
        label = "Cart",
        icon = R.drawable.cart,
        route = Screens.CartScreen.name
    ),
    NavItem(
        label = "Notification",
        icon = R.drawable.notification,
        route = Screens.NotificationScreen.name
    ),
    NavItem(
        label = "Personal",
        icon = R.drawable.person,
        route = Screens.PersonalManagementScreen.name
    )
)