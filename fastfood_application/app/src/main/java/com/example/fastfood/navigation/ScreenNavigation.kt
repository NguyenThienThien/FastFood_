package com.example.fastfood.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fastfood.ForgetPassword
import com.example.fastfood.activities.DetailsOrder
import com.example.fastfood.activities.LoginPage
import com.example.fastfood.activities.MainLoginSignUp
import com.example.fastfood.activities.WelcomeScreen
import com.example.fastfood.activities.ChangeInformationScreen
import com.example.fastfood.activities.ChangePassWord
import com.example.fastfood.activities.OrderHistory

@RequiresApi(Build.VERSION_CODES.O)
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
        composable(ScreensList.ChangeInformationScreen.route){ ChangeInformationScreen(navController = navController) }
        composable(ScreensList.OrderHistoryScreen.route){ OrderHistory(navController = navController) }
        composable(ScreensList.ChangePasswordScreen.route){ ChangePassWord(navController = navController) }
        composable(ScreensList.ForgetPasswordScreen.route){ ForgetPassword(navController = navController) }
    }
}