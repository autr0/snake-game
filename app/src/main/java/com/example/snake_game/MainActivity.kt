package com.example.snake_game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snake_game.data.ThemeManager
import com.example.snake_game.presentation.SnakeViewModel
import com.example.snake_game.presentation.screens.Navigation
import com.example.snake_game.ui.theme.Snake_gameTheme

class MainActivity : ComponentActivity() {
    private lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            themeManager = ThemeManager(this)

            val viewModel: SnakeViewModel = viewModel(factory = SnakeViewModel.factory)

            Snake_gameTheme(darkTheme = viewModel.isDarkTheme.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(vm = viewModel)
                }
            }
        }
    }
}