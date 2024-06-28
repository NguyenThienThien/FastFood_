package com.example.fastfood.data.models

import com.google.gson.annotations.SerializedName

data class Cart(
    val id: String,
    val user_id: String,
    val product_id: String,
    val nameProduct: String,
    val priceProduct: Double,
    val imageProduct: List<String>,
    val quantity_cart: Int
)

data class CartResponse(
    @SerializedName("_id") val id: String,
    @SerializedName("user_id") val user_id: String,
    @SerializedName("product_id") val product_id: String,
    @SerializedName("nameProduct") val nameProduct: String,
    @SerializedName("priceProduct") val priceProduct: Double,
    @SerializedName("imageProduct") val imageProduct: List<String>,
    @SerializedName("quantity_cart") val quantity_cart: Int
)

fun CartResponse.toCart(): Cart{
    return Cart(
        id = this.id,
        user_id = this.user_id,
        product_id = this.product_id,
        nameProduct = this.nameProduct,
        priceProduct = this.priceProduct,
        imageProduct = this.imageProduct,
        quantity_cart = this.quantity_cart,
    )
}