package com.example.fastfood.data.network

import com.example.fastfood.data.models.ProductResponse
import com.example.fastfood.data.models.ProductTypeResponse
import retrofit2.Response
import retrofit2.http.GET
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
}