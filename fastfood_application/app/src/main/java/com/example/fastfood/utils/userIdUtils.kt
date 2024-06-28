package com.example.fastfood.utils

import android.content.Context
import android.content.SharedPreferences

object SharePrefsUtil {
    private const val PREFS_NAME = "fastfood_prefs"
    private const val KEY_USER_ID = "user_id"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserId(context: Context, userId: String){
        val editor = getPrefs(context).edit()
        editor.putString(KEY_USER_ID, userId)
        editor.apply()
    }

    fun getUserId(context: Context): String?{
        return getPrefs(context).getString(KEY_USER_ID, null)
    }

    fun clearUserId(context: Context){
        val editor = getPrefs(context).edit()
        editor.remove(KEY_USER_ID)
        editor.apply()
    }
}