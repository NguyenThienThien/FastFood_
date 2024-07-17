package com.example.fastfood.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fastfood.activities.DetailsOrder
import com.example.fastfood.activities.LoginPage
import com.example.fastfood.activities.MainLoginSignUp
import com.example.fastfood.activities.SignUpPage
import com.example.fastfood.activities.WelcomeScreen

@Composable
fun ScreenNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "WelcomeScreen"
    ) {
        composable(ScreensList.MainBottomNav.route){ AppNavigation(navigationController = navController) }
        composable(ScreensList.MainLoginSignUp.route){ MainLoginSignUp(navController = navController) }
        composable(ScreensList.LoginScreen.route){ LoginPage(navController = navController, onSignUp = ({Unit})) }
        composable(ScreensList.WelcomeScreen.route){ WelcomeScreen(navController = navController) }
        composable(ScreensList.DetailOrderScreen.route){ DetailsOrder(navController = navController) }
    }
}