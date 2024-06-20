package com.example.fastfood.activities

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fastfood.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpPage(navController: NavController, onSignUpSuccess: () -> Unit) {

    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val rePasswordState = remember { mutableStateOf("") }

    val passwordVisibility = remember { mutableStateOf(false) }
    val rePasswordVisibility = remember { mutableStateOf(false) }

    val emailFocused = remember { mutableStateOf(false) }
    val passwordFocused = remember { mutableStateOf(false) }
    val rePasswordFocused = remember { mutableStateOf(false) }

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val rePasswordFocusRequester = remember { FocusRequester() }

    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Email address",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            placeholder = { Text("Email") },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.icon_email),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailFocusRequester)
                .onFocusChanged { emailFocused.value = it.isFocused }
                .shadow(if(emailFocused.value) 10.dp else 0.dp, RoundedCornerShape(10.dp), spotColor = Color.Transparent)
                .then(if(emailFocused.value) Modifier.padding(4.dp) else Modifier.padding(0.dp)),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFFEC2578),
                unfocusedIndicatorColor = Color(0xFFD2D2D2),
                containerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Password",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            placeholder = { Text("Password") },
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
                .shadow(if(passwordFocused.value) 10.dp else 0.dp, RoundedCornerShape(10.dp), spotColor = Color.Transparent)
                .then(if(passwordFocused.value) Modifier.padding(4.dp) else Modifier.padding(0.dp)),
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
            text = "RePassword",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = rePasswordState.value,
            onValueChange = { rePasswordState.value = it },
            placeholder = { Text("RePassword") },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.icon_lock),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = {
                val image =
                    if (rePasswordVisibility.value) R.drawable.blind else R.drawable.icon_eye
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
                .shadow(if(rePasswordFocused.value) 10.dp else 0.dp, RoundedCornerShape(10.dp), spotColor = Color.Transparent)
                .then(if(rePasswordFocused.value) Modifier.padding(4.dp) else Modifier.padding(0.dp)),
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
                    emailState.value.isEmpty() -> {
                        emailFocusRequester.requestFocus()
                        Toast.makeText(context, "Email is required", Toast.LENGTH_SHORT).show()
                    }
                    !emailState.value.matches(emailPattern.toRegex()) -> {
                        emailFocusRequester.requestFocus()
                        Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
                    }
                    passwordState.value.isEmpty() -> {
                        passwordFocusRequester.requestFocus()
                        Toast.makeText(context, "Password is required", Toast.LENGTH_SHORT).show()
                    }
                    passwordState.value.length < 8 -> {
                        passwordFocusRequester.requestFocus()
                        Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                    }
                    rePasswordState.value.isEmpty() -> {
                        rePasswordFocusRequester.requestFocus()
                        Toast.makeText(context, "RePassword is required", Toast.LENGTH_SHORT).show()
                    }
                    passwordState.value != rePasswordState.value -> {
                        rePasswordFocusRequester.requestFocus()
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        auth.createUserWithEmailAndPassword(emailState.value, passwordState.value)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    val userId = user?.uid ?: ""
                                    val userRef: DatabaseReference = FirebaseDatabase.getInstance().reference

                                    val userMap = mapOf(
                                        "id" to userId,
                                        "name" to userId,
                                        "image" to "",
                                        "phone" to "",
                                        "address" to "",
                                        "birthDate" to ""
                                    )

                                    userRef.child("Users").child(userId).setValue(userMap)
                                        .addOnCompleteListener { dbTask ->
                                            if (dbTask.isSuccessful){
                                                Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                                                onSignUpSuccess()
                                            }else{
                                                Toast.makeText(context, "Database Error: ${dbTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                } else {
                                    Toast.makeText(context, "Email already exists", Toast.LENGTH_SHORT).show()
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
                text = "Sign Up",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }



        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Do you already have an account ?",
                fontSize = 15.sp,
            )

            Text(
                text = "Login",
                fontSize = 15.sp,
                color = Color(0xFFEC2578),
                fontWeight = FontWeight(700),
                modifier = Modifier
                    .clickable {
                        onSignUpSuccess()
                    }
            )
        }

    }
}