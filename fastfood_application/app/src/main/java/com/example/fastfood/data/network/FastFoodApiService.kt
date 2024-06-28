package com.example.fastfood.data.network

import com.example.fastfood.data.models.Cart
import com.example.fastfood.data.models.ProductResponse
import com.example.fastfood.data.models.ProductTypeResponse
import com.example.fastfood.data.models.StatusResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

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

    //===========POST===========
    @POST("add-cart")
    suspend fun addCart(@Body cart: Cart): Response<StatusResponse>
}