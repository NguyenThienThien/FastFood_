package com.example.fastfood.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastfood.data.models.ProductResponse
import com.example.fastfood.data.network.RetrofitService
import com.example.fastfood.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val _products = MutableLiveData<List<ProductResponse>>(emptyList())
    val products: LiveData<List<ProductResponse>> get() = _products

    private val apiService = RetrofitService().apiService
    private val productRepository = ProductRepository(apiService)

    init {
        fetchProducts()
    }

    private fun fetchProducts(){
        viewModelScope.launch {
            try {
                val product = productRepository.getListProducts()
                _products.postValue(product)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}