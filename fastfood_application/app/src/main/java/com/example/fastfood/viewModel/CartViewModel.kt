package com.example.fastfood.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastfood.data.models.Cart
import com.example.fastfood.data.models.StatusResponse
import com.example.fastfood.data.network.RetrofitService
import com.example.fastfood.data.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class CartViewModel : ViewModel() {

    private val apiService = RetrofitService().apiService
    private val cartRepository = CartRepository(apiService)

    fun addToCart(
        userId: String,
        productId: String,
        nameProduct: String,
        priceProduct: Double,
        imageProduct: List<String>,
        quantity: Int,
        onComplete: (Boolean) -> Unit
    ) {
        val cart = Cart(
            id = "",
            user_id = userId,
            product_id = productId,
            nameProduct = nameProduct,
            priceProduct = priceProduct,
            imageProduct = imageProduct,
            quantity_cart = quantity
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = cartRepository.addToCart(cart)
                if(response.isSuccessful && response.body()?.status == 200){
                    onComplete(true)
                }else{
                    onComplete(false)
                }
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }
}