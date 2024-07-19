package com.example.fastfood.viewModel

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastfood.data.models.Cart
import com.example.fastfood.data.models.StatusResponse
import com.example.fastfood.data.network.RetrofitService
import com.example.fastfood.data.repository.CartRepository
import com.example.fastfood.utils.SharePrefsUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class CartViewModel : ViewModel() {
    private val apiService = RetrofitService().apiService
    private val cartRepository = CartRepository(apiService)

    private val _cartList = MutableLiveData<List<Cart>>()
    val cartList: LiveData<List<Cart>> get() = _cartList

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun getCart(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = cartRepository.getCartByUserId(userId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _cartList.value = response.body()
                    } else {
                        _error.value = "Failed to fetch cart: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = "Failed to fetch cart: ${e.message}"
                }
            }
        }
    }

    fun addToCart(
        userId: String,
        productId: String,
        nameProduct: String,
        priceProduct: Double,
        imageProduct: List<String>,
        quantity: Int,
        rate: Float,
        sold: Int,
        onComplete: (Boolean) -> Unit
    ) {
        val cart = Cart(
            id = "",
            user_id = userId,
            product_id = productId,
            nameProduct = nameProduct,
            priceProduct = priceProduct,
            imageProduct = imageProduct,
            quantity_cart = quantity,
            rate = rate,
            sold = sold
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = cartRepository.addToCart(cart)
                if(response.isSuccessful && response.body()?.status == 200){
                    onComplete(true)
                }else{
                    _error.value = "Failed to add to cart: ${response.message()}"
                    onComplete(false)
                }
            } catch (e: Exception) {
                _error.value = "Failed to add to cart: ${e.message}"
                onComplete(false)
            }
        }
    }

    fun updateCartQuantity(
        cartId: String,
        newCartQuantity: Int,
        userId: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = cartRepository.updateCart(cartId, newCartQuantity)
                if (response.isSuccessful && response.body()?.status == 200) {
                    getCart(userId)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteCart(cartId: String, userId: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = cartRepository.deleteCartItem(cartId)
                if(response.isSuccessful && response.body()?.status == 200){
                    // Update cart list on the main thread
                    withContext(Dispatchers.Main) {
                        _cartList.value = _cartList.value?.filter { it.id != cartId }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _error.value = "Failed to delete cart: ${response.message()}"
                    }
                }
            } catch (e: Exception){
                withContext(Dispatchers.Main) {
                    _error.value = "Failed to delete cart: ${e.message}"
                    e.printStackTrace()
                }
            }
        }
    }

}