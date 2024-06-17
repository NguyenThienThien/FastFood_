package com.example.fastfood.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastfood.data.models.ProductTypeResponse
import com.example.fastfood.data.network.RetrofitService
import com.example.fastfood.data.repository.ProductTypeRepository
import kotlinx.coroutines.launch

class ProductTypesViewModel : ViewModel() {
    private val _productTypes = MutableLiveData<List<ProductTypeResponse>>(emptyList())
    val productTypes: LiveData<List<ProductTypeResponse>> get() = _productTypes

    private val apiService = RetrofitService().apiService
    private val productTypeRepository = ProductTypeRepository(apiService)
    init {
        fetchProductTypes()
    }

    private fun fetchProductTypes() {
        viewModelScope.launch {
            try {
                val types = productTypeRepository.getListProductTypes()
                _productTypes.postValue(types)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}