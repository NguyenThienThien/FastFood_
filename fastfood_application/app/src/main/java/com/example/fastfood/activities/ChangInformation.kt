package com.example.fastfood.activities

import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.fastfood.R
import com.example.fastfood.data.models.User
import com.example.fastfood.ui.theme.OpenSans
import com.example.fastfood.utils.SharePrefsUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeInformationScreen(navController: NavController) {
    val context = LocalContext.current
    val userId = SharePrefsUtil.getUserId(context)
    val customerData = remember { mutableStateOf(User()) }

    var image by remember { mutableStateOf(customerData.value.image ?: "") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val copiedImageUri = saveImageFromUri(context, uri)
            image = copiedImageUri.toString()
        }
    }

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

    var newName by remember { mutableStateOf("") }
    var newPhone by remember { mutableStateOf("") }
    var newAddress by remember { mutableStateOf("") }
    var newBirthDate by remember { mutableStateOf("") }
    var newImage by remember { mutableStateOf("") }

    LaunchedEffect(customerData.value) {
        newName = customerData.value.name ?: ""
        newPhone = customerData.value.phone ?: ""
        newAddress = customerData.value.address ?: ""
        newBirthDate = customerData.value.birthDate ?: ""
        newImage = customerData.value.image?: ""
    }

    val nameFocused = remember { mutableStateOf(false) }
    val phoneFocused = remember { mutableStateOf(false) }
    val addressFocused = remember { mutableStateOf(false) }
    val birthDateFocused = remember { mutableStateOf(false) }

    val nameFocusRequester = remember { FocusRequester() }
    val phoneFocusRequester = remember { FocusRequester() }
    val addressFocusRequester = remember { FocusRequester() }
    val birthDateFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        UpdateHeader(navController = navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (image.isNotEmpty()) {
                    val bitmap = remember { loadImageBitmapFromUri(context, Uri.parse(image)) }
                    bitmap?.let {
                        Image(
                            bitmap = it,
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .clickable { imagePickerLauncher.launch("image/*")},
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    val painter = rememberImagePainter(
                        data = newImage.takeIf { it.isNotEmpty() } ?: R.drawable.image,
                        builder = {
                            crossfade(true)
                            placeholder(R.drawable.image)
                        }
                    )
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            // Name TextField
            Text(
                text = "Name",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                placeholder = { Text(text = "Enter your name") },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.icon_person),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { nameFocused.value = it.isFocused }
                    .shadow(
                        if (nameFocused.value) 10.dp else 0.dp,
                        RoundedCornerShape(10.dp),
                        spotColor = Color.Transparent
                    )
                    .focusRequester(nameFocusRequester)
                    .then(if (nameFocused.value) Modifier.padding(4.dp) else Modifier.padding(0.dp)),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color(0xFFEC2578),
                    unfocusedIndicatorColor = Color(0xFFD2D2D2),
                    containerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Phone TextField
            Text(
                text = "Phone",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextField(
                value = newPhone,
                onValueChange = { newPhone = it },
                placeholder = { Text(text = "Enter your phone") },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.icon_phone),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { phoneFocused.value = it.isFocused }
                    .shadow(
                        if (phoneFocused.value) 10.dp else 0.dp,
                        RoundedCornerShape(10.dp),
                        spotColor = Color.Transparent
                    )
                    .focusRequester(phoneFocusRequester)
                    .then(if (phoneFocused.value) Modifier.padding(4.dp) else Modifier.padding(0.dp)),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color(0xFFEC2578),
                    unfocusedIndicatorColor = Color(0xFFD2D2D2),
                    containerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Address TextField
            Text(
                text = "Address",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextField(
                value = newAddress,
                onValueChange = { newAddress = it },
                placeholder = { Text(text = "Enter your address") },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.address),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { addressFocused.value = it.isFocused }
                    .shadow(
                        if (addressFocused.value) 10.dp else 0.dp,
                        RoundedCornerShape(10.dp),
                        spotColor = Color.Transparent
                    )
                    .focusRequester(addressFocusRequester)
                    .then(if (addressFocused.value) Modifier.padding(4.dp) else Modifier.padding(0.dp)),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color(0xFFEC2578),
                    unfocusedIndicatorColor = Color(0xFFD2D2D2),
                    containerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            // BirthDate Button and DatePicker
            Text(
                text = "BirthDate",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = newBirthDate,
                onValueChange = { newBirthDate = it },
                placeholder = { Text(text = "chosee birth date") },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.icon_birth),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { birthDateFocused.value = it.isFocused }
                    .shadow(
                        if (birthDateFocused.value) 10.dp else 0.dp,
                        RoundedCornerShape(10.dp),
                        spotColor = Color.Transparent
                    )
                    .focusRequester(birthDateFocusRequester)
                    .then(if (birthDateFocused.value) Modifier.padding(4.dp) else Modifier.padding(0.dp)),
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
                    var toastMessage = ""
                    val phoneRegex = Regex("^\\d{10}$")
                    val birthDateRegex = Regex("^\\d{2}/\\d{2}/\\d{4}$") // Định dạng dd/MM/yyyy
                    when {
                        newName.isBlank() -> toastMessage = "Không được để trống tên"
                        newPhone.isBlank() -> toastMessage = "Không được để trống số điện thoại"
                        !newPhone.matches(phoneRegex) -> toastMessage = "Số điện thoại chưa đúng định dạng"
                        newAddress.isBlank() -> toastMessage = "Không được để trống dịa chỉ"
                        newAddress.length < 10 -> toastMessage = "Địa chỉ phải trên 10 ký tự"
                        newBirthDate.isBlank() -> toastMessage = "Không được bỏ trống ngày tháng năm sinh"
                        !newBirthDate.matches(birthDateRegex) -> toastMessage = "Ngày tháng năm sinh chưa đúng định dạng dd/MM/yyyy"
                        else -> {
                            if (image.isNotEmpty()) {
                                val imageUri = Uri.parse(image)
                                uploadImageToFirebaseStorage(imageUri) { downloadUrl ->
                                    val database = FirebaseDatabase.getInstance()
                                    val customerRef = database.getReference("Users").child(userId!!)
                                    val updatedUser = User(
                                        id = userId,
                                        name = newName,
                                        phone = newPhone,
                                        address = newAddress,
                                        birthDate = newBirthDate,
                                        image = downloadUrl
                                    )
                                    customerRef.setValue(updatedUser)
                                    toastMessage = "Information updated successfully"
                                    navController.navigateUp()
                                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                val database = FirebaseDatabase.getInstance()
                                val customerRef = database.getReference("Users").child(userId!!)
                                val updatedUser = User(
                                    id = userId,
                                    name = newName,
                                    phone = newPhone,
                                    address = newAddress,
                                    birthDate = newBirthDate,
                                    image = customerData.value.image // Keep current image if no new image is selected
                                )
                                customerRef.setValue(updatedUser)
                                toastMessage = "Information updated successfully"
                                navController.navigateUp()
                                Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    if (toastMessage.isNotEmpty()) {
                        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp)),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEC2578)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Cập nhật",
                    fontSize = 18.sp,
                    fontWeight = FontWeight(600),
                    fontFamily = OpenSans,
                )
            }
        }
    }
}

@Composable
fun UpdateHeader(navController: NavController) {
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
                text = "Cập nhật thông tin",
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

fun loadImageBitmapFromUri(context: Context, uri: Uri): ImageBitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        bitmap.asImageBitmap()
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
    // Tạo thư mục để lưu trữ ảnh
    val cw = ContextWrapper(context)
    val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
    // Tạo file để lưu ảnh
    val fileName = "image_${UUID.randomUUID()}.jpg"
    val file = File(directory, fileName)

    try {
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    // Trả về Uri của file đã lưu
    return Uri.fromFile(file)
}
fun saveImageFromUri(context: Context, imageUri: Uri): Uri {
    val contentResolver: ContentResolver = context.contentResolver
    val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    return saveBitmapToFile(context, bitmap)
}

fun uploadImageToFirebaseStorage(uri: Uri, onComplete: (String) -> Unit) {
    val storageReference = FirebaseStorage.getInstance().reference
    val fileReference = storageReference.child("images/${System.currentTimeMillis()}.jpg")
    val uploadTask = fileReference.putFile(uri)

    uploadTask.continueWithTask { task ->
        if (!task.isSuccessful) {
            task.exception?.let { throw it }
        }
        fileReference.downloadUrl
    }.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val downloadUri = task.result
            onComplete(downloadUri.toString())
        } else {
            // Handle failures
        }
    }
}

@Composable
@Preview
fun ChangeInformationScreenPreview() {
    ChangeInformationScreen(navController = rememberNavController())
}
