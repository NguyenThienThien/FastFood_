package com.example.fastfood.utils

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(price: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return formatter.format(price).replace("₫", "₫")
}