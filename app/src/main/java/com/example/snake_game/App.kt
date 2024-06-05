package com.example.snake_game

import android.app.Application
import com.example.snake_game.data.MainDb

class App : Application() {
    val database by lazy { MainDb.createDb(this) }
}