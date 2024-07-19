package com.example.fastfood.activities

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fastfood.R
import com.example.fastfood.ui.theme.OpenSans
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPassword(navController: NavController){

    val emailState = remember { mutableStateOf("") }
    val emailFocused = remember { mutableStateOf(false) }
    val emailFocusRequester = remember { FocusRequester() }
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        ForgetPasswordHeader(navController = navController)

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Nhập Email",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            placeholder = { Text(text = "Enter your email") },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.icon_email),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { emailFocused.value = it.isFocused }
                .shadow(
                    if (emailFocused.value) 10.dp else 0.dp,
                    RoundedCornerShape(10.dp),
                    spotColor = Color.Transparent
                )
                .focusRequester(emailFocusRequester)
                .then(if (emailFocused.value) Modifier.padding(4.dp) else Modifier.padding(0.dp)),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFFEC2578),
                unfocusedIndicatorColor = Color(0xFFD2D2D2),
                containerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                when {
                    emailState.value.isEmpty() -> {
                        emailFocusRequester.requestFocus()
                        Toast.makeText(context, "Vui lòng không để trống email", Toast.LENGTH_SHORT).show()
                    }
                    !emailState.value.matches(emailPattern.toRegex()) -> {
                        emailFocusRequester.requestFocus()
                        Toast.makeText(context, "Email không đúng định dạng", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        auth.sendPasswordResetEmail(emailState.value)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Email xác nhận đã được gửi", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Gửi email thất bại: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(10.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEC2578)
            ),
            shape = RectangleShape
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = "Lấy lại mật khẩu",
                fontSize = 18.sp,
                fontWeight = FontWeight(600),
                fontFamily = OpenSans,
            )
        }
    }
}

@Composable
fun ForgetPasswordHeader(navController: NavController) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { navController.navigateUp() }
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(25.dp))
            }
            Text(
                text = "Quên mật khẩu",
                fontSize = 18.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Divider(color = Color(0xFFf4f4f4), thickness = 1.dp)
        Spacer(modifier = Modifier.padding(5.dp))
    }
}