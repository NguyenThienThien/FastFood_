package com.example.fastfood.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fastfood.activities.CartScreen
import com.example.fastfood.activities.HomeScreen
import com.example.fastfood.activities.NotificationScreen
import com.example.fastfood.activities.PersonalManagementScreen

@Composable
fun AppNavigation(navigationController: NavController){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFEC2578),
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .height(60.dp)
                    .shadow(
                        elevation = 5.dp,
                        spotColor = Color.Black,
                        shape = RoundedCornerShape(5.dp)
                    )
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                listOfNavItems.forEach { navItem ->
                    val selected = currentDestination?.hierarchy?.any{ it.route == navItem.route } == true
                    val iconSize by animateFloatAsState(if (selected) 28f else 25f, label = "")
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(navItem.route){
                                popUpTo(navController.graph.findStartDestination().id){
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            val iconColor = if (selected) Color(0xFFEC2578) else Color.White // Màu icon khi được chọn và không chọn
                            Image(
                                painter = painterResource(id = navItem.icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(iconSize.dp)
                                    .height(iconSize.dp),
                                colorFilter = ColorFilter.tint(iconColor)
                            )
                        },
                    )
                }
            }
        }
    ) {
            paddingValues: PaddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.HomeScreen.name,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Screens.HomeScreen.name){
                HomeScreen(navController = navigationController)
            }

            composable(route = Screens.CartScreen.name){
                CartScreen(navController = navigationController)
            }

            composable(route = Screens.NotificationScreen.name){
                NotificationScreen(navController = navigationController)
            }

            composable(route = Screens.PersonalManagementScreen.name){
                PersonalManagementScreen(navController = navigationController)
            }
        }
    }
}