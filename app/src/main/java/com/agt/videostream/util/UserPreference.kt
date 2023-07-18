package com.agt.videostream.util

import android.content.Context
import android.content.SharedPreferences

class UserPreference(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserIdPrefs", Context.MODE_PRIVATE)
    private val userIdKey = "user_id"


    fun getUserId(): String? {
        return sharedPreferences.getString(userIdKey, null)
    }

    fun setUserId(userId: String) {
        sharedPreferences.edit().putString(userIdKey, userId).apply()
    }

    fun clearUserId() {
        sharedPreferences.edit().remove(userIdKey).apply()
    }



}