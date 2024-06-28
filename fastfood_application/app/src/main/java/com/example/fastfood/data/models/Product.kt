package com.example.fastfood.data.models

import com.google.gson.annotations.SerializedName

data class Product(
    val id: String,
    val nameProduct: String,
    val productTypeId: String,
    val priceProduct: Double,
    val imageProduct: List<String>,
    val describeProduct: String,
    val statusProduct: String,
    val rate: Float,
    val sold: Int
)

data class ProductResponse(
    @SerializedName("_id") val id: String,
    @SerializedName("nameProduct") val nameProduct: String,
    @SerializedName("productTypeId") val productTypeId: String,
    @SerializedName("priceProduct") val priceProduct: Double,
    @SerializedName("imageProduct") val imageProduct: List<String>,
    @SerializedName("describeProduct") val describeProduct: String,
    @SerializedName("statusProduct") val statusProduct: String,
    @SerializedName("rate") val rate: Float,
    @SerializedName("sold") val sold: Int,
)

fun productResponseToProduct(response: ProductResponse) : Product {
    return Product(
        id = response.id,
        nameProduct = response.nameProduct,
        productTypeId = response.productTypeId,
        priceProduct = response.priceProduct,
        imageProduct = response.imageProduct,
        describeProduct = response.describeProduct,
        statusProduct = response.statusProduct,
        rate = response.rate,
        sold = response.sold,
    )
}
