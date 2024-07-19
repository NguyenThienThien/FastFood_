package com.example.fastfood.data.models

import com.google.gson.annotations.SerializedName

data class OrderItem(
    val nameProduct: String,
    val imageProduct: String,
    val priceProduct: Double,
    val quantity: Int
)
data class Order(
    @SerializedName("_id") val id: String,
    val userId: String,
    val orderItems: List<OrderItem>,
    val totalAmount: Double,
    val statusOrder: Int,
    val dateOrder: String,
    val timeOrder: String
)

data class OrderResponse(
    @SerializedName("_id") val id: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("orderItems") val orderItems: List<OrderItem>,
    @SerializedName("totalAmount") val totalAmount: Double,
    @SerializedName("statusOrder") val statusOrder: Int,
    @SerializedName("dateOrder") val dateOrder: String,
    @SerializedName("timeOrder") val timeOrder: String
)

fun OrderResponse.toOrder(): Order {
    return Order(
        id = this.id,
        userId = this.userId,
        orderItems = this.orderItems,
        totalAmount = this.totalAmount,
        statusOrder = this.statusOrder,
        dateOrder = this.dateOrder,
        timeOrder = this.timeOrder
    )
}