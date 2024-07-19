package com.example.fastfood.data.repository

import com.example.fastfood.data.models.Cart
import com.example.fastfood.data.models.QuantityRequest
import com.example.fastfood.data.models.StatusResponse
import com.example.fastfood.data.network.FastFoodApiService
import retrofit2.Response

class CartRepository(private val apiService: FastFoodApiService) {
    suspend fun addToCart(cart: Cart): Response<StatusResponse> {
        return apiService.addCart(cart)
    }

    suspend fun getCartByUserId(userId: String): Response<List<Cart>> {
        return apiService.getCartByUserId(userId)
    }

    suspend fun updateCart(cartId: String, quantity: Int): Response<StatusResponse> {
        return apiService.updateCartQuantity(cartId, QuantityRequest(quantity))
    }

    suspend fun deleteCartItem(cartId: String): Response<StatusResponse> {
        return apiService.deleteCart(cartId)
    }

}