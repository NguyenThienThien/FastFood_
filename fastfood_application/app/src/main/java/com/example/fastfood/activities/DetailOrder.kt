package com.example.fastfood.activities

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fastfood.R
import com.example.fastfood.activities.components.ItemProductOfOrderDetail
import com.example.fastfood.data.models.Cart
import com.example.fastfood.data.models.User
import com.example.fastfood.ui.theme.OpenSans
import com.example.fastfood.utils.SharePrefsUtil
import com.example.fastfood.utils.formatCurrency
import com.example.fastfood.viewModel.CartViewModel
import com.example.fastfood.viewModel.OrderViewModel
import com.google.firebase.database.*
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailsOrder(navController: NavController, cartViewModel: CartViewModel = viewModel(), orderViewModel: OrderViewModel = viewModel()) {
    val context = LocalContext.current
    val userId = SharePrefsUtil.getUserId(context)
    val customerData = remember { mutableStateOf(User()) }
    val currentDateTime = remember { LocalDateTime.now().plusHours(1) }
    val formatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM")
    val formattedDateTime = currentDateTime.format(formatter)
    var showSuccessDialog by remember { mutableStateOf(false) }

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

    LaunchedEffect(userId) {
        if (userId != null) {
            cartViewModel.getCart(userId)
        }
    }

    val cartItems by cartViewModel.cartList.observeAsState(emptyList())
    val totalAmount = cartItems.sumByDouble { it.priceProduct * it.quantity_cart }

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

    var newPhone by remember { mutableStateOf("") }
    var newAddress by remember { mutableStateOf("") }

    LaunchedEffect(customerData.value) {
        newPhone = customerData.value.phone ?: ""
        newAddress = customerData.value.address ?: ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        OrderHeader(navController)
        ShippingAddressSection(customerData, navController)
        DeliveryTimeSection(formattedDateTime)
        Column(
            modifier = Modifier.weight(6f)
        ){
            OrderDetailsSection(cartItems, cartViewModel, userId, totalAmount)
        }
        Column(
            modifier = Modifier
                .weight(0.8f)
                .background(Color.White)
                .fillMaxSize()
        ){
            if (userId != null) {
                OrderButton(navController, totalAmount, orderViewModel, userId, cartItems, customerData)
            }
        }
    }
}

@Composable
fun OrderHeader(navController: NavController) {
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
                text = "Xác nhận đơn hàng",
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

@Composable
fun ShippingAddressSection(customerData: State<User>, navController: NavController) {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(id = R.drawable.icon_address), contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = "Địa chỉ giao hàng",
                fontSize = 14.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
            )
        }

        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${customerData.value.name} | ${customerData.value.phone}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight(500),
                        fontFamily = OpenSans,
                        color = Color(0xFF747474)
                    )
                }

                Spacer(modifier = Modifier.padding(2.dp))

                Text(
                    text = customerData.value.address,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    fontFamily = OpenSans,
                    color = Color(0xFF747474)
                )
            }

            IconButton(onClick = {navController.navigate("ChangeInformationScreen")}) {
                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color(0xFF747474), modifier = Modifier.size(20.dp))
            }
        }
        Divider(color = Color(0xFFf4f4f4), thickness = 1.dp)
    }
}

@Composable
fun DeliveryTimeSection(formattedDateTime: String) {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 9.dp)
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.icon_clock), contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Giao ngay - $formattedDateTime - Hôm nay",
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    fontFamily = OpenSans,
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color(0xFF747474), modifier = Modifier.size(20.dp))
            }
        }
        Divider(color = Color(0xFFf4f4f4), thickness = 1.dp)
    }
}

@Composable
fun OrderDetailsSection(cartItems: List<Cart>, cartViewModel: CartViewModel, userId: String?, totalAmount: Double) {
    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            items(cartItems) { cartItem ->
                if (userId != null) {
                    ItemProductOfOrderDetail(cartItem, cartViewModel, userId)
                }
            }
        }

        OrderSummary(totalAmount, cartItems)
    }
}

@Composable
fun OrderSummary(totalAmount: Double, cartItems: List<Cart>) {
    Column(modifier = Modifier.padding(10.dp)) {
        Row(
            modifier = Modifier.padding(10.dp),
        ) {
            Text(
                text = "Tổng cộng ( ${cartItems.size} món )",
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                color = Color(0xFF747474),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = formatCurrency(totalAmount),
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                color = Color(0xFF747474)
            )
        }

        Divider(color = Color(0xFFf4f4f4), thickness = 1.dp)

        Row(
            modifier = Modifier.padding(10.dp),
        ) {
            Text(
                text = "Phí giao hàng ( 0.6 km )",
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                color = Color(0xFF747474),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = formatCurrency(10000.0),
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                color = Color(0xFF747474)
            )
        }

        Divider(color = Color(0xFFf4f4f4), thickness = 1.dp)

        Row(
            modifier = Modifier.padding(10.dp),
        ) {
            Text(
                text = "Phí áp dụng (?)",
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                color = Color(0xFF747474),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = formatCurrency(3000.0),
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                color = Color(0xFF747474)
            )
        }

        Divider(color = Color(0xFFf4f4f4), thickness = 1.dp)

        Row(
            modifier = Modifier.padding(10.dp),
        ) {
            Text(
                text = "Khuyến mãi",
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                color = Color(0xFF747474),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = formatCurrency(0.0),
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                color = Color(0xFF747474)
            )
        }

        Divider(color = Color(0xFFf4f4f4), thickness = 1.dp)

        Row(
            modifier = Modifier.padding(10.dp),
        ) {
            Text(
                text = "Tổng cộng",
                fontSize = 15.sp,
                fontWeight = FontWeight(600),
                fontFamily = OpenSans,
                color = Color(0xFF000000),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = formatCurrency(totalAmount + 10000.0 + 3000.0),
                fontSize = 15.sp,
                fontWeight = FontWeight(600),
                fontFamily = OpenSans,
                color = Color(0xFFEC2578)
            )
        }

        Divider(color = Color(0xFFf4f4f4), thickness = 1.dp)

        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.icon_voucher), contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = "Thêm voucher",
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        fontFamily = OpenSans,
                    )
                }
                Text(
                    text = "Chọn voucher",
                    fontSize = 13.sp,
                    fontWeight = FontWeight(500),
                    fontFamily = OpenSans,
                    color = Color(0xFF747474)
                )
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color(0xFF747474), modifier = Modifier.size(20.dp))
                }
            }

            Divider(color = Color(0xFFf4f4f4), thickness = 1.dp)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderButton(navController: NavController, totalAmount: Double, orderViewModel: OrderViewModel, userId: String, cartItems: List<Cart>, customerData: State<User>) {
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Button(
            onClick = {
                // Kiểm tra thông tin
                if (customerData.value.phone.isNullOrEmpty() || customerData.value.address.isNullOrEmpty()) {
                    showErrorDialog = true
                } else {
                    orderViewModel.addOrder(userId, cartItems, totalAmount)
                    showSuccessDialog = true
                }
            },
            modifier = Modifier
                .fillMaxSize(),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEC2578)
            ),
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = "Đặt đơn - ${formatCurrency(totalAmount)}",
                fontSize = 18.sp,
                fontWeight = FontWeight(600),
                fontFamily = OpenSans,
            )
        }
    }

    if (showSuccessDialog) {
        SuccessDialog(navController) {
            showSuccessDialog = false
        }
    }

    if (showErrorDialog) {
        ErrorDialog(
            onDismiss = {
                showErrorDialog = false
                navController.navigate("ChangeInformationScreen")
            }
        )
    }
}

@Composable
fun SuccessDialog(navController: NavController, onDismiss: () -> Unit) {
    var countdown by remember { mutableStateOf(3) }
    val context = LocalContext.current

    LaunchedEffect(true) {
        while (countdown > 0) {
            delay(1000L)
            countdown -= 1
        }
        onDismiss()
        navController.navigate("TabNav")
    }

    AlertDialog(
        onDismissRequest = {},
        title = { Text("Thông báo") },
        text = { Text("Đặt hàng thành công!") },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEC2578)
                ),
            ) {
                Text(text = if (countdown > 0) countdown.toString() else "Đóng")
            }
        },
        containerColor = Color.White,
        modifier = Modifier
            .shadow(3.dp, RoundedCornerShape(10.dp))
            .background(Color.White)
    )
}
@Composable
fun ErrorDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Thông báo") },
        text = { Text("Không đủ thông tin để đặt hàng. Vui lòng cập nhật thông tin đầy đủ.") },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEC2578)
                ),
            ) {
                Text(text = "Cập nhật thông tin")
            }
        },
        containerColor = Color.White,
        modifier = Modifier
            .shadow(3.dp, RoundedCornerShape(10.dp))
            .background(Color.White)
    )
}