package com.example.fastfood.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(price: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return formatter.format(price).replace("₫", "₫")
}
fun createPartFromString(value: String): RequestBody {
    return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
}
fun createPartFromDouble(value: Double): RequestBody {
    return RequestBody.create("text/plain".toMediaTypeOrNull(), value.toString())
}
fun createPartFromInt(value: Int): RequestBody {
    return RequestBody.create("text/plain".toMediaTypeOrNull(), value.toString())
}
fun prepareFilePart(partName: String, fileUri: String): MultipartBody.Part {
    val file = File(fileUri)
    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
    return MultipartBody.Part.createFormData(partName, file.name, requestFile)
}