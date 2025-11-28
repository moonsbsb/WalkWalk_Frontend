package com.withwalk.app.api

import android.content.Context


class TokenManager(context: Context) {
    private val pref = context.getSharedPreferences("pref_token", Context.MODE_PRIVATE)

    fun saveToken(token: String){
        pref.edit().putString("token", token).apply()
    }
    fun getToken(): String?{
        return pref.getString("token", null)
    }
    fun clearToken(){
        pref.edit().remove("token").apply()
    }
}