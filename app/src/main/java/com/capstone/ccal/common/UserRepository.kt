package com.capstone.ccal.common

import android.app.Application
import android.content.Context

object UserRepository {
    private const val PREF_NAME = "user_info"

    fun saveUsername(context: Context, username: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.apply()
    }

    fun saveEmail(context: Context, email: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.apply()
    }

    fun getUsername(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", "") ?: ""
    }

    fun getEmail(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("email", "") ?: ""
    }
}
