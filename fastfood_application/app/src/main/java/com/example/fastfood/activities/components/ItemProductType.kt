package com.example.fastfood.activities.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fastfood.data.models.ProductType
import com.example.fastfood.ui.theme.OpenSans

@Composable
fun ItemTypes(
    productType: ProductType,
    isSelected: Boolean,
    onItemSelected: (String) -> Unit
) {

    val backgroundColor = if (isSelected) Color(0xFFF14E89) else Color.Transparent
    val contentColor = if (isSelected) Color.White else Color.Black

    Row(
        modifier = Modifier
            .width(150.dp)
            .height(43.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFF14E89), RoundedCornerShape(8.dp))
            .padding(10.dp)
            .clickable {
                // Gọi hàm callback khi ItemTypes được click
                onItemSelected(productType.id)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = productType.imageProductType,
            contentDescription = null,
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(50.dp)),
            contentScale = ContentScale.Crop
        )

        Text(
            text = productType.typeName,
            fontFamily = OpenSans,
            fontSize = 16.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            color = contentColor
        )
    }

    Spacer(modifier = Modifier.padding(5.dp))
}