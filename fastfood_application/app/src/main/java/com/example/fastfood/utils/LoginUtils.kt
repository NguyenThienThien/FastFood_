package com.example.fastfood.utils

import android.content.Context
import android.content.SharedPreferences

object LoginUtils {
    private const val PREFS_NAME = "fastfood_prefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    private fun getPreferences(context: Context): SharedPreferences{
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveLoginState(context: Context, isLoggedIn: Boolean){
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun getLoginState(context: Context): Boolean {
        val prefs = getPreferences(context)
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
}