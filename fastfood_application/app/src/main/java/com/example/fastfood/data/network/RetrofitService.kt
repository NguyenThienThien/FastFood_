package com.example.fastfood.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

open class RetrofitService {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: FastFoodApiService by lazy {
        retrofit.create(FastFoodApiService::class.java)
    }
}