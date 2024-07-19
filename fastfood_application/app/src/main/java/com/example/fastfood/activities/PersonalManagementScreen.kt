package com.example.fastfood.activities

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.fastfood.R
import com.example.fastfood.data.models.User
import com.example.fastfood.ui.theme.OpenSans
import com.example.fastfood.utils.LoginUtils
import com.example.fastfood.utils.SharePrefsUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun PersonalManagementScreen(navController: NavController){

    val context = LocalContext.current
    val userId = SharePrefsUtil.getUserId(context)
    val customerData = remember { mutableStateOf(User()) }
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance()
        val customerRef = database.getReference("Users").child(userId!!)

        customerRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val customer = snapshot.getValue(User::class.java)
                if (customer != null) {
                    customerData.value = customer
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    var name by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }

    LaunchedEffect(customerData.value) {
        name = customerData.value.name ?: ""
        image = customerData.value.image ?: ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(5.dp),
    ){
        Text(
            text = "Profile",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = OpenSans,
        )

        Spacer(modifier = Modifier.padding(5.dp))

        ProfileHeader(name = name, image = image)
        ProfileOptions(navController, auth)
    }
}

@Composable
fun ProfileHeader(name: String, image: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BorderedAsyncImage(
            image = image,
            borderColor = Color(0xFFffe1e1),
            borderWidth = 15.dp
        )

        Spacer(modifier = Modifier.padding(2.dp))

        Text(
            text = name,
            fontSize = 15.sp,
            fontWeight = FontWeight(600),
            fontFamily = OpenSans,
            color = Color.Gray
        )
    }
}

@Composable
fun ProfileOptions(navController: NavController, auth: FirebaseAuth) {
    val context = LocalContext.current
    ProfileOption(
        iconResId = R.drawable.edit_profile,
        text = "Đổi thông tin cá nhân",
        onClick = { navController.navigate("ChangeInformationScreen") }
    )

    ProfileOption(
        iconResId = R.drawable.password,
        text = "Đổi mật khẩu",
        onClick = { navController.navigate("ChangePasswordScreen") }
    )

    ProfileOption(
        iconResId = R.drawable.bill,
        text = "Lịch sử đặt hàng",
        onClick = { navController.navigate("OrderHistoryScreen") }
    )

    ProfileOption(
        iconResId = R.drawable.logout,
        text = "Đăng xuất",
        onClick = {
            auth.signOut()
            LoginUtils.saveLoginState(context, false)
            SharePrefsUtil.clearUserId(context)
            navController.navigate("MainLoginSignUp")
        }
    )
}

@Composable
fun ProfileOption(iconResId: Int, text: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .shadow(2.dp, RoundedCornerShape(10.dp))
            .height(50.dp)
            .background(Color.White)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = iconResId), contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    fontFamily = OpenSans,
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color(0xFF747474), modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun BorderedAsyncImage(image: String, borderColor: Color, borderWidth: Dp) {
    Box(
        modifier = Modifier
            .size(100.dp) // Kích thước của Box và ảnh
            .clip(RoundedCornerShape(100.dp)) // Đảm bảo viền cùng góc với ảnh
            .background(Color.Transparent) // Đặt nền của Box là trong suốt
    ) {
        AsyncImage(
            model = image,
            contentDescription = null,
            modifier = Modifier
                .matchParentSize() // Đảm bảo ảnh chiếm toàn bộ không gian của Box
                .clip(RoundedCornerShape(50.dp)),
            contentScale = ContentScale.Crop
        )

        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            drawRoundRect(
                color = borderColor,
                size = size,
                style = Stroke(width = borderWidth.toPx()), // Độ dày của viền
                cornerRadius = CornerRadius(50.dp.toPx())
            )
        }
    }
}