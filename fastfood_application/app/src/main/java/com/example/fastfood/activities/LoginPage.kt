package com.example.fastfood.activities

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.example.fastfood.utils.SharePrefsUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(navController: NavController, onSignUp: () -> Unit) {

    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    val passwordVisibility = remember { mutableStateOf(false) }

    val emailFocused = remember { mutableStateOf(false) }
    val passwordFocused = remember { mutableStateOf(false) }

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    // đăng nhập bằng google
//    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestIdToken(context.getString(R.string.default_web_client_id))
//        .requestEmail()
//        .build()
//
//    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)
//    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//        try {
//            val account = task.getResult(ApiException::class.java)!!
//            firebaseAuthWithGoogle(account.idToken!!, auth, context, navController)
//        } catch (e: ApiException) {
//            Log.w("LoginPage", "Google sign-in failed", e)
//        }
//    }



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

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Password",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            placeholder = { Text(text = "Enter your password") },
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
                .onFocusChanged { passwordFocused.value = it.isFocused }
                .shadow(
                    if (passwordFocused.value) 10.dp else 0.dp,
                    RoundedCornerShape(10.dp),
                    spotColor = Color.Transparent
                )
                .focusRequester(passwordFocusRequester)
                .then(if (passwordFocused.value) Modifier.padding(4.dp) else Modifier.padding(0.dp)),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFFEC2578),
                unfocusedIndicatorColor = Color(0xFFD2D2D2),
                containerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Forget Password?",
            color = Color(0xFFEC2578),
            modifier = Modifier.align(Alignment.End),
            fontWeight = FontWeight(600)
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Button(
            onClick = {
                when {
                    emailState.value.isEmpty() -> {
                        emailFocusRequester.requestFocus()
                        Toast.makeText(context, "Vui lòng không để trống email", Toast.LENGTH_SHORT).show()
                    }
                    !emailState.value.matches(emailPattern.toRegex()) -> {
                        emailFocusRequester.requestFocus()
                        Toast.makeText(context, "email không đúng định dạng", Toast.LENGTH_SHORT).show()
                    }
                    passwordState.value.isEmpty() -> {
                        passwordFocusRequester.requestFocus()
                        Toast.makeText(context, "Vui lòng không để trống mật khẩu", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        auth.signInWithEmailAndPassword(emailState.value, passwordState.value)
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                    val userId = auth.currentUser?.uid ?: ""
                                    SharePrefsUtil.saveUserId(context, userId)
                                    Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                                    navController.navigate("TabNav")
                                }else{
                                    Toast.makeText(context, "Email hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show()
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
                text = "Login",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Row {
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .weight(1f)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Or",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF959494)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .weight(1f)
            )
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.facebook),
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.gmail),
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .clickable {
//                        launcher.launch(googleSignInClient.signInIntent)
                    }
            )
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            "Do you already have an account ?",
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            "Sign In",
            color = Color(0xFFEC2578),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { onSignUp() }
        )
    }
}

//private fun firebaseAuthWithGoogle(idToken: String, auth: FirebaseAuth, context: Context, navController: NavController) {
//    val credential = GoogleAuthProvider.getCredential(idToken, null)
//    auth.signInWithCredential(credential)
//        .addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
//                navController.navigate("TabNav")
//            } else {
//                Toast.makeText(context, "Đăng nhập Google thất bại", Toast.LENGTH_SHORT).show()
//            }
//        }
//}