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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fastfood.R
import com.example.fastfood.ui.theme.OpenSans
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePassWord(navController: NavController) {
    val currentPassword = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val rePasswordState = remember { mutableStateOf("") }

    val currentPasswordVisibility = remember { mutableStateOf(false) }
    val passwordVisibility = remember { mutableStateOf(false) }
    val rePasswordVisibility = remember { mutableStateOf(false) }

    val currentPasswordFocused = remember { mutableStateOf(false) }
    val passwordFocused = remember { mutableStateOf(false) }
    val rePasswordFocused = remember { mutableStateOf(false) }

    val currentPasswordFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val rePasswordFocusRequester = remember { FocusRequester() }

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        ChangePassHeader(navController = navController)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Old Password",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = currentPassword.value,
            onValueChange = { currentPassword.value = it },
            placeholder = { Text("Current Password") },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.icon_lock),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = {
                val image = if (currentPasswordVisibility.value) R.drawable.blind else R.drawable.icon_eye
                IconButton(
                    onClick = {
                        currentPasswordVisibility.value = !currentPasswordVisibility.value
                    }
                ) {
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(currentPasswordFocusRequester)
                .onFocusChanged { currentPasswordFocused.value = it.isFocused }
                .shadow(if (currentPasswordFocused.value) 10.dp else 0.dp, RoundedCornerShape(10.dp), spotColor = Color.Transparent)
                .then(if (currentPasswordFocused.value) Modifier.padding(4.dp) else Modifier.padding(0.dp)),
            singleLine = true,
            visualTransformation = if (currentPasswordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFFEC2578),
                unfocusedIndicatorColor = Color(0xFFD2D2D2),
                containerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "New Password",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            placeholder = { Text("New Password") },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.icon_lock),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = {
                val image = if (passwordVisibility.value) R.drawable.blind else R.drawable.icon_eye
                IconButton(
                    onClick = {
                        passwordVisibility.value = !passwordVisibility.value
                    }
                ) {
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester)
                .onFocusChanged { passwordFocused.value = it.isFocused }
                .shadow(if (passwordFocused.value) 10.dp else 0.dp, RoundedCornerShape(10.dp), spotColor = Color.Transparent)
                .then(if (passwordFocused.value) Modifier.padding(4.dp) else Modifier.padding(0.dp)),
            singleLine = true,
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFFEC2578),
                unfocusedIndicatorColor = Color(0xFFD2D2D2),
                containerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Confirm New Password",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = rePasswordState.value,
            onValueChange = { rePasswordState.value = it },
            placeholder = { Text("Confirm New Password") },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.icon_lock),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = {
                val image = if (rePasswordVisibility.value) R.drawable.blind else R.drawable.icon_eye
                IconButton(
                    onClick = {
                        rePasswordVisibility.value = !rePasswordVisibility.value
                    }
                ) {
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(rePasswordFocusRequester)
                .onFocusChanged { rePasswordFocused.value = it.isFocused }
                .shadow(if (rePasswordFocused.value) 10.dp else 0.dp, RoundedCornerShape(10.dp), spotColor = Color.Transparent)
                .then(if (rePasswordFocused.value) Modifier.padding(4.dp) else Modifier.padding(0.dp)),
            singleLine = true,
            visualTransformation = if (rePasswordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFFEC2578),
                unfocusedIndicatorColor = Color(0xFFD2D2D2),
                containerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                when {
                    currentPassword.value.isEmpty() -> {
                        currentPasswordFocusRequester.requestFocus()
                        Toast.makeText(context, "Vui lòng không để trống mật khẩu cũ", Toast.LENGTH_SHORT).show()
                    }
                    passwordState.value.isEmpty() -> {
                        passwordFocusRequester.requestFocus()
                        Toast.makeText(context, "Vui lòng không để trống mật khẩu mới", Toast.LENGTH_SHORT).show()
                    }
                    passwordState.value.length < 8 -> {
                        passwordFocusRequester.requestFocus()
                        Toast.makeText(context, "Mật khẩu mới phải có trên 8 ký tự", Toast.LENGTH_SHORT).show()
                    }
                    rePasswordState.value.isEmpty() -> {
                        rePasswordFocusRequester.requestFocus()
                        Toast.makeText(context, "Vui lòng nhập lại mật khẩu mới", Toast.LENGTH_SHORT).show()
                    }
                    passwordState.value != rePasswordState.value -> {
                        rePasswordFocusRequester.requestFocus()
                        Toast.makeText(context, "Mật khẩu và nhập lại mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        val user = auth.currentUser
                        if (user != null && user.email != null) {
                            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword.value)

                            user.reauthenticate(credential).addOnCompleteListener { authTask ->
                                if (authTask.isSuccessful) {
                                    user.updatePassword(passwordState.value).addOnCompleteListener { updateTask ->
                                        if (updateTask.isSuccessful) {
                                            Toast.makeText(context, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                                            navController.navigateUp()
                                        } else {
                                            Toast.makeText(context, "Mật khẩu cũ không chính xác", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "Mật khẩu cũ không chính xác", Toast.LENGTH_SHORT).show()
                                }
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
            Text(
                text = "Change Password",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ChangePassHeader(navController: NavController) {
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
                text = "Đổi mật khẩu",
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
