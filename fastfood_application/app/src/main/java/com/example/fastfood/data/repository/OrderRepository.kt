package com.example.fastfood.data.repository

import com.example.fastfood.data.models.Order
import com.example.fastfood.data.models.StatusResponse
import com.example.fastfood.data.network.FastFoodApiService
import retrofit2.Response

class OrderRepository(private val apiService: FastFoodApiService){
    suspend fun addOrder(order: Order): Response<StatusResponse> {
        return apiService.addOrder(order)
    }

    suspend fun deleteAllCart(userId: String): Response<StatusResponse> {
        return apiService.deleteAllCartsByUserId(userId)
    }

    suspend fun getOrderByUserId(userId: String): Response<List<Order>> {
        return apiService.getOrderByUserId(userId)
    }

}