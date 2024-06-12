package com.example.fastfood.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ScreenNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "TabNav"
    ) {
        composable(ScreensList.MainBottomNav.route){ AppNavigation(navigationController = navController)}
    }
}