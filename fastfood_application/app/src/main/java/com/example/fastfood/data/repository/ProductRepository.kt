package com.example.fastfood.data.repository

import com.example.fastfood.data.models.ProductResponse
import com.example.fastfood.data.models.ProductTypeResponse
import com.example.fastfood.data.network.FastFoodApiService

class ProductRepository(private val apiService: FastFoodApiService) {
    suspend fun getListProducts(): List<ProductResponse> {
        val response = apiService.getListProducts()
        if(response.isSuccessful){
            return response.body()?: emptyList()
        }else{
            throw Exception("Failed to fetch products")
        }
    }
}