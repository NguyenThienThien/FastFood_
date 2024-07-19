package com.example.fastfood.activities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fastfood.data.models.Order
import com.example.fastfood.ui.theme.OpenSans
import com.example.fastfood.utils.SharePrefsUtil
import com.example.fastfood.utils.formatCurrency
import com.example.fastfood.viewModel.OrderViewModel

@Composable
fun OrderHistory(navController: NavController, orderViewModel: OrderViewModel = viewModel()){

    val context = LocalContext.current
    val userId = SharePrefsUtil.getUserId(context)

    LaunchedEffect(userId) {
        if(userId != null){
            orderViewModel.getOrder(userId)
        }
    }

    val orderItems by orderViewModel.order.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        OrderHistoryHeader(navController = navController)
        OrderHistoryList(orderItems = orderItems, userId = userId!!)
    }
}

@Composable
fun OrderHistoryList(orderItems: List<Order>, userId: String) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(orderItems){orderItems ->
            OrderHistoryItem(order = orderItems, userId = userId)
        }
    }

}

@Composable
fun OrderHistoryItem(order: Order, userId: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .shadow(3.dp, RoundedCornerShape(10.dp))
            .background(
                Color.White
            )
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
        ) {
            Text(
                text = "id: ",
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                color = Color(0xFF747474),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = order.id,
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
                text = "userId: ",
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                color = Color(0xFF747474),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = order.userId,
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
                text = "tổng số lượng món: ",
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                color = Color(0xFF747474),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${order.orderItems.size}",
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
                text = "tổng giá: ",
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                color = Color(0xFF747474),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = formatCurrency(order.totalAmount),
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                color = Color(0xFFed2578)
            )
        }

        Divider(color = Color(0xFFf4f4f4), thickness = 1.dp)

        Row(
            modifier = Modifier.padding(10.dp),
        ) {
            Text(
                text = "Ngày giờ đặt hàng: ",
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                color = Color(0xFF747474),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${order.timeOrder} - ${order.dateOrder}",
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
                text = "trạng thái: ",
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                color = Color(0xFF747474),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = if(order.statusOrder == 0) "Đang chờ xác nhận" else "Đã giao",
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                fontFamily = OpenSans,
                color = Color(0xFF747474)
            )
        }

    }
}

@Composable
fun OrderHistoryHeader(navController: NavController) {
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
                text = "Lịch sử đặt hàng",
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