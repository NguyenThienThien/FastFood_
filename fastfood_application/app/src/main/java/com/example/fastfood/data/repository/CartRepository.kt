package com.example.fastfood.data.repository

import com.example.fastfood.data.models.Cart
import com.example.fastfood.data.models.StatusResponse
import com.example.fastfood.data.network.FastFoodApiService
import com.example.fastfood.data.network.RetrofitService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class CartRepository(private val apiService: FastFoodApiService) {

    suspend fun addToCart(cart: Cart): Response<StatusResponse> {
        return apiService.addCart(cart)
    }

}