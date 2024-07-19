package com.example.fastfood.activities

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fastfood.R
import com.example.fastfood.data.models.Cart
import com.example.fastfood.ui.theme.OpenSans
import com.example.fastfood.utils.SharePrefsUtil
import com.example.fastfood.utils.formatCurrency
import com.example.fastfood.viewModel.CartViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import com.example.fastfood.activities.components.CartItemView

@Composable
fun CartScreen(navController: NavController, cartViewModel: CartViewModel = viewModel()) {
    val context = LocalContext.current
    val userId = SharePrefsUtil.getUserId(context)

    LaunchedEffect(userId) {
        if (userId != null) {
            cartViewModel.getCart(userId)
        }
    }

    val cartItems by cartViewModel.cartList.observeAsState(emptyList())
    val totalAmount = cartItems.sumByDouble { it.priceProduct * it.quantity_cart }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "My cart",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = OpenSans,
        )
        Spacer(modifier = Modifier.padding(5.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(8f)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(cartItems) { cartItem ->
                    if (userId != null) {
                        CartItemView(cartItem, cartViewModel, userId)
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
                .shadow(3.dp, RoundedCornerShape(10.dp))
                .background(Color.White),
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Tổng tiền",
                        fontSize = 18.sp,
                        fontWeight = FontWeight(400),
                        fontFamily = OpenSans,
                    )

                    Text(
                        text = formatCurrency(totalAmount),
                        fontSize = 18.sp,
                        fontWeight = FontWeight(500),
                        fontFamily = OpenSans,
                    )
                }

                Button(
                    onClick = {
                        if (cartItems.isEmpty()) {
                            Toast.makeText(context, "Bạn đang không có sản phẩm nào", Toast.LENGTH_SHORT).show()
                        } else {
                            navController.navigate("DetailOrderScreen")
                        }
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1.5f),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEC2578)
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }


        }

    }
}

