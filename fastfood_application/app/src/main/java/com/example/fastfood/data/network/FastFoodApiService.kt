package com.example.fastfood.data.network

import com.example.fastfood.data.models.Cart
import com.example.fastfood.data.models.Order
import com.example.fastfood.data.models.ProductResponse
import com.example.fastfood.data.models.ProductTypeResponse
import com.example.fastfood.data.models.QuantityRequest
import com.example.fastfood.data.models.StatusResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface FastFoodApiService {
    //===========GET===========
    @GET("get-productTypes")
    suspend fun getListProductTypes(): Response<List<ProductTypeResponse>>

    @GET("get-products")
    suspend fun getListProducts(): Response<List<ProductResponse>>

    @GET("get-productsById/{id}")
    suspend fun getDetailsProduct(@Path("id") id: String): Response<ProductResponse>

    @GET("get-productsByTypeId/{productTypeId}")
    suspend fun getProductsByType(@Path("productTypeId") productTypeId: String): Response<List<ProductResponse>>

    @GET("get-cartByUserId")
    suspend fun getCartByUserId(@Query("user_id") userId: String): Response<List<Cart>>

    @GET("get-orderByUserId")
    suspend fun getOrderByUserId(@Query("user_id") userId: String): Response<List<Order>>

    //===========POST===========
    @POST("add-cart")
    suspend fun addCart(@Body cart: Cart): Response<StatusResponse>

    @POST("add-order")
    suspend fun addOrder(@Body order: Order): Response<StatusResponse>

    //===========PUT===========
    @PUT("update-quantity/{id}")
    suspend fun updateCartQuantity(
        @Path("id") cartId: String,
        @Body quantity: QuantityRequest
    ): Response<StatusResponse>

    //===========DELETE===========
    @DELETE("delete-cartById/{id}")
    suspend fun deleteCart(@Path("id") id: String): Response<StatusResponse>

    @DELETE("delete-all-cartsByUserId/{user_id}")
    suspend fun deleteAllCartsByUserId(@Path("user_id") userId: String): Response<StatusResponse>

}