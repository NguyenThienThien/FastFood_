package com.example.fastfood.activities.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ShapeDefaults
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
fun ItemProduct(product: Product, viewModel: CartViewModel) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userId = SharePrefsUtil.getUserId(context)

    Box(
        modifier = Modifier
            .width(180.dp)
            .height(155.dp)
            .shadow(elevation = 3.dp, RoundedCornerShape(10.dp))
            .background(Color.White)
    ) {
        Column {
            // Product image
            AsyncImage(
                model = product.imageProduct[0],
                contentDescription = null,
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.padding(3.dp))

            // Product name
            Text(
                text = product.nameProduct,
                fontFamily = OpenSans,
                fontSize = 16.sp,
                fontWeight = FontWeight(500),
                modifier = Modifier.padding(start = 10.dp),
                textAlign = TextAlign.Center,
            )

            // Product price
            Text(
                text = formatCurrency(product.priceProduct),
                fontFamily = OpenSans,
                fontSize = 14.sp,
                fontWeight = FontWeight(600),
                modifier = Modifier.padding(start = 10.dp),
                textAlign = TextAlign.Center,
                color = Color(0xFFEC2578)
            )
        }

        // Add to cart button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(11.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                modifier = Modifier
                    .height(28.dp)
                    .width(28.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFEC2578)),
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
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.padding(5.dp))
}