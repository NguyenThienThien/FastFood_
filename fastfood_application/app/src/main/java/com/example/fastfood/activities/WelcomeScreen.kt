package com.example.fastfood.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fastfood.R
import com.example.fastfood.ui.theme.OpenSans
import com.example.fastfood.utils.LoginUtils
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(navController: NavController){
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        delay(3000)
        val isLoggedIn = LoginUtils.getLoginState(context)
        if(isLoggedIn){
            navController.navigate("TabNav")
        }else{
            navController.navigate("MainLoginSignUp")
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFfdeeda))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )

            Text(
                text = "FAST FOOD",
                fontFamily = OpenSans,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Color(0xFFec2578)
            )

        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 10.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.res_welcome),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
        }
    }

}