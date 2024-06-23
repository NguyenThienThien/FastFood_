package com.example.fastfood.activities

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fastfood.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainLoginSignUp(navController: NavController){
    var selectedTabIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Image(
            painter = painterResource(id = R.drawable.pattern),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                modifier = Modifier
                    .width(380.dp)
                    .padding(vertical = 10.dp),
                contentScale = ContentScale.Crop
            )
        }



        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 270.dp)
                .background(Color.White)
        ) {
            Scaffold(
                topBar = {
                    TabRow(selectedTabIndex = selectedTabIndex, contentColor = Color(0xFFEC2578)) {
                        Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }) {
                            Text("Login", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
                        }
                        Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }) {
                            Text("Sign Up", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    when (selectedTabIndex) {
                        0 -> LoginPage(navController, onSignUp = { selectedTabIndex = 1 })
                        1 -> SignUpPage(navController,onSignUpSuccess = { selectedTabIndex = 0 })
                    }
                }
            }

        }
    }

}



