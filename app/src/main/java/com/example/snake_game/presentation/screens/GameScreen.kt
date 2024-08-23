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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.snake_game.presentation.SnakeViewModel

@Composable
fun GameScreen(
    openPreviousScreen: () -> Unit,
    vm: SnakeViewModel
) {
    val state = vm.gameState.collectAsState(initial = null)

    val size = remember { vm.currentSnakeSize }

    val dialogState = remember {
        vm.dialogState
    }

    if (dialogState.value) {
        ScoreDialog(vm, openPreviousScreen)
    }

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
                    text = "${size.intValue}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        state.value?.let {
            Board(it)
        }
        Buttons {
            vm.onDirectionChange(it)
        }
    }

    BackHandler {
        if (dialogState.value) {
            vm.closeDialog()
        }
        vm.finishMovingState()
        openPreviousScreen()
    }

}