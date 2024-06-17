package com.example.fastfood.data.repository

import com.example.fastfood.data.models.ProductTypeResponse
import com.example.fastfood.data.network.FastFoodApiService
import retrofit2.Response

class ProductTypeRepository(private val apiService: FastFoodApiService) {
    suspend fun getListProductTypes(): List<ProductTypeResponse> {
        val response = apiService.getListProductTypes()
        if(response.isSuccessful){
            return response.body()?: emptyList()
        }else{
            throw Exception("Failed to fetch product types")
        }
    }

}