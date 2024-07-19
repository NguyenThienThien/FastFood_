package com.example.fastfood.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastfood.data.models.Cart
import com.example.fastfood.data.models.Order
import com.example.fastfood.data.models.OrderItem
import com.example.fastfood.data.network.RetrofitService
import com.example.fastfood.data.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OrderViewModel : ViewModel() {
    private val apiService = RetrofitService().apiService
    private val orderRepository = OrderRepository(apiService)
    private val _order = MutableLiveData<List<Order>>()
    val order: LiveData<List<Order>> get() = _order

    private val _orderStatus = MutableLiveData<String>()
    val orderStatus: LiveData<String> get() = _orderStatus

    fun getOrder(userId: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = orderRepository.getOrderByUserId(userId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _order.postValue(response.body())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addOrder(
        userId: String,
        cartItems: List<Cart>,
        totalAmount: Double
    ){
        val orderItems = cartItems.map {
            OrderItem(
                nameProduct = it.nameProduct,
                priceProduct = it.priceProduct,
                quantity = it.quantity_cart,
                imageProduct = it.imageProduct[0]
            )
        }

        val currentDateTime = LocalDateTime.now()
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val time = currentDateTime.format(timeFormatter)
        val date = currentDateTime.format(dateFormatter)

        val order = Order(
            id = "",
            userId = userId,
            totalAmount = totalAmount,
            orderItems = orderItems,
            statusOrder = 0,
            dateOrder = date,
            timeOrder = time
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("OrderViewModel", "Payload: $order")
                val response = orderRepository.addOrder(order)
                if (response.isSuccessful) {
                    _orderStatus.postValue("Order successfully placed!")
                    deleteAllCart(userId)
                } else {
                    _orderStatus.postValue("Failed to place order: ${response.message()}")
                }
            } catch (e: Exception) {
                _orderStatus.postValue("Error: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun deleteAllCart( userId: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = orderRepository.deleteAllCart(userId)
                if(response.isSuccessful && response.body()?.status == 200){
                    _orderStatus.postValue("delete all cart successfully placed!")
                } else {
                    withContext(Dispatchers.Main) {
                        _orderStatus.postValue("delete all cart Failed placed: ${response.message()}")
                    }
                }
            } catch (e: Exception){
                withContext(Dispatchers.Main) {
                    _orderStatus.postValue("Error: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }
}