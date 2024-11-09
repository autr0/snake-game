package com.example.snake_game.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.snake_game.presentation.SnakeViewModel

@Composable
fun GameScreen(
    openPreviousScreen: () -> Unit,
    vm: SnakeViewModel
) {
    val state by vm.gameState.collectAsStateWithLifecycle()

    if (state.isShowDialog) {
        ScoreDialog(
            score = state.currentSnakeSize,
            closeDialog = { vm.closeDialog() },
            handlePoints= { vm.handlePoints() },
            stopGame = { vm.stopGame() },
            restartGame = { vm.restartGame() }
        )
    }

    LaunchedEffect(key1 = state.navigateBack) {
        if (state.navigateBack) {
            openPreviousScreen()
        }
    }

    val isDarkTheme by vm.isDarkTheme

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${state.currentSnakeSize}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Board(state = state, isDarkTheme = isDarkTheme)
        Buttons {
            vm.onDirectionChange(it)
        }
    }

    BackHandler {
        vm.handlePoints()
        vm.stopGame()
        openPreviousScreen()
    }

}