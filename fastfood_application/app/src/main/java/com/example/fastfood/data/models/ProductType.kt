package com.example.fastfood.data.models

import com.google.gson.annotations.SerializedName

data class ProductType(
    val id: String,
    val typeName: String,
    val imageProductType: String
)

data class ProductTypeResponse(
    @SerializedName("_id") val id: String,
    @SerializedName("typeName") val typeName: String,
    @SerializedName("imageProductType") val imageProductType: String
)

fun productTypeResponseToProductType(response: ProductTypeResponse): ProductType {
    return ProductType(
        id = response.id,
        typeName = response.typeName,
        imageProductType = response.imageProductType
    )
}
