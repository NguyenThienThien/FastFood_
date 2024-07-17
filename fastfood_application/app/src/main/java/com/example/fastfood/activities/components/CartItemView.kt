package com.example.fastfood.activities.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fastfood.R
import com.example.fastfood.data.models.Cart
import com.example.fastfood.ui.theme.OpenSans
import com.example.fastfood.utils.formatCurrency
import com.example.fastfood.viewModel.CartViewModel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartItemView(cart: Cart, cartViewModel: CartViewModel, userId: String) {
    val cartList = cartViewModel.cartList.value
    val quantityState = cart.quantity_cart
    val scope = rememberCoroutineScope()
    val dismissState = rememberDismissState()

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp)
                    .background(Color(0xFFec2578), RoundedCornerShape(10.dp))
                    .clickable {
                        scope.launch {
                            try {
                                cartViewModel.deleteCart(cart.id, userId)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        },
        dismissContent = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .shadow(3.dp, RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = cart.imageProduct.getOrNull(0),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .height(80.dp)
                            .width(80.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = cart.nameProduct ?: "",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight(500),
                            fontFamily = OpenSans
                        )
                        Spacer(modifier = Modifier.padding(2.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${cart.sold} đã bán | ",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500),
                                fontFamily = OpenSans,
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "${cart.rate}",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight(500),
                                    fontFamily = OpenSans
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                                StarRating(rate = cart.rate)
                            }
                        }

                        Spacer(modifier = Modifier.padding(2.dp))

                        Row {
                            Text(
                                text = formatCurrency(cart.priceProduct),
                                color = Color(0xFFec2578),
                                fontSize = 16.sp,
                                fontWeight = FontWeight(500),
                                modifier = Modifier.weight(1f),
                                fontFamily = OpenSans
                            )

                            Row {
                                Column(
                                    modifier = Modifier
                                        .width(25.dp)
                                        .height(25.dp)
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color(0xFFec81ae))
                                        .clickable {
                                            if (quantityState > 1) {
                                                val newQuantity = quantityState - 1
                                                cartViewModel.updateCartQuantity(
                                                    cart.id,
                                                    newQuantity,
                                                    userId
                                                )
                                            }
                                        },
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.minus),
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.padding(5.dp))

                                Text(
                                    text = "$quantityState",
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight(600),
                                    fontFamily = OpenSans
                                )

                                Spacer(modifier = Modifier.padding(5.dp))

                                Column(
                                    modifier = Modifier
                                        .width(25.dp)
                                        .height(25.dp)
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color(0xFFec2578))
                                        .clickable {
                                            val newQuantity = quantityState + 1
                                            cartViewModel.updateCartQuantity(
                                                cart.id,
                                                newQuantity,
                                                userId
                                            )
                                        },
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.add),
                                        contentDescription = null,
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.padding(8.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(5.dp))
            }
        }
    )

}

@Composable
fun StarRating(rate: Float) {
    Box(
        modifier = Modifier.size(18.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = if (rate == 0f) Color.Gray else Color(0xFFec2578),
            modifier = Modifier.size(18.dp)
        )
    }
}