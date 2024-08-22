package com.example.snake_game.data

import android.content.Context
import android.content.SharedPreferences

class ThemeManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    var isDarkTheme: Boolean
        get() = sharedPreferences.getBoolean("is_dark_theme", false)
        set(value) {
            sharedPreferences.edit().putBoolean("is_dark_theme", value).apply()
        }
}