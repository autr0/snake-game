package com.example.snake_game.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.snake_game.data.Game
import com.example.snake_game.data.model.State
import com.example.snake_game.ui.theme.LightThemeSnakeGreen

@Composable
fun Board(state: State, isDarkTheme: Boolean) {
    BoxWithConstraints(Modifier.padding(16.dp)) {
        val tileSize = maxWidth / Game.BOARD_SIZE

        Box(
            Modifier
                .size(maxWidth)
                .border(2.dp, MaterialTheme.colorScheme.primary)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Box(
            Modifier
                .offset(x = tileSize * state.food.first, y = tileSize * state.food.second)
                .size(tileSize)
                .background(
                    if (isDarkTheme) Color.Green else LightThemeSnakeGreen,
                    CircleShape
                )
        )

        state.snake.forEach {
            if (it == state.snake[0]) {
                Box(
                    modifier = Modifier
                        .offset(x = tileSize * it.first, y = tileSize * it.second)
                        .size(tileSize)
                        .background(
                            Color.Red, ShapeDefaults.Small
                        )
                )
            } else {
                Box(
                    modifier = Modifier
                        .offset(x = tileSize * it.first, y = tileSize * it.second)
                        .size(tileSize)
                        .background(
                            if (isDarkTheme) Color.Green else LightThemeSnakeGreen,
                            ShapeDefaults.Small
                        )
                )
            }
        }
    }
}