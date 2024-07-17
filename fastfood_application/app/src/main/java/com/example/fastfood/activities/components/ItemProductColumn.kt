package com.example.fastfood.activities.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fastfood.data.models.Product
import com.example.fastfood.ui.theme.OpenSans
import com.example.fastfood.utils.SharePrefsUtil
import com.example.fastfood.utils.formatCurrency
import com.example.fastfood.viewModel.CartViewModel
import kotlinx.coroutines.launch

@Composable
fun ItemProduct2(product: Product, viewModel: CartViewModel) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userId = SharePrefsUtil.getUserId(context)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(elevation = 3.dp, RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(10.dp),
    ) {
        // Product image
        AsyncImage(
            model = product.imageProduct[0],
            contentDescription = null,
            modifier = Modifier
                .height(70.dp)
                .width(90.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        // Product name
        Text(
            text = product.nameProduct,
            fontFamily = OpenSans,
            fontSize = 18.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Center,
        )

        // Product price and Add to cart button
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            // Product price
            Text(
                text = formatCurrency(product.priceProduct),
                fontFamily = OpenSans,
                fontSize = 20.sp,
                fontWeight = FontWeight(500),
                modifier = Modifier.padding(start = 10.dp),
                textAlign = TextAlign.Center,
                color = Color(0xFFEC2578)
            )

            // Add to cart button
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFFF05B99))
                    .height(21.dp)
                    .width(70.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        viewModel.addToCart(
                            userId = userId!!,
                            productId = product.id,
                            nameProduct = product.nameProduct,
                            priceProduct = product.priceProduct,
                            imageProduct = product.imageProduct,
                            quantity = 1,
                            rate = product.rate,
                            sold = product.sold
                        ) { success ->
                            coroutineScope.launch {
                                if (success) {
                                    Toast.makeText(
                                        context,
                                        "Thêm vào giỏ hàng thành công",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Thêm vào giỏ hàng thất bại",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Add to cart",
                        fontFamily = OpenSans,
                        fontSize = 10.sp,
                        fontWeight = FontWeight(600),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.padding(5.dp))
}