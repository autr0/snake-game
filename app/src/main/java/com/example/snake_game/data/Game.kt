package com.example.snake_game.data

import com.example.snake_game.presentation.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Random

class Game(
    private val scope: CoroutineScope
) {
    private val mutex = Mutex()
    private var gameJob: Job? = null

    var move = Pair(1, 0)
        set(value) {
            scope.launch {
                mutex.withLock {
                    field = value
                }
            }
        }

    fun start(gameState: MutableStateFlow<State>) {
        gameJob = scope.launch {
            val random = Random()

            while (gameState.value.isSnakeMoving) {
                delay(150)

                val newPosition = gameState.value.snake.first().let { poz ->
                    mutex.withLock {
                        Pair(
                            (poz.first + move.first + BOARD_SIZE) % BOARD_SIZE,
                            (poz.second + move.second + BOARD_SIZE) % BOARD_SIZE
                        )
                    }
                }

                gameState.update { state ->
                    state.copy(
                        food = if (newPosition == state.food) Pair(
                            random.nextInt(BOARD_SIZE),
                            random.nextInt(BOARD_SIZE)
                        ) else state.food,
                        snake = listOf(newPosition) + state.snake.take(state.currentSnakeSize - 1),
                        currentSnakeSize = if (newPosition == state.food) {
                            state.currentSnakeSize + 1
                        } else {
                            state.currentSnakeSize
                        },
                        isSnakeMoving = !gameState.value.snake.contains(newPosition),
                        isShowDialog = gameState.value.snake.contains(newPosition)
                    )
                }
            }
        }
    }

    fun stopGame() {
        gameJob?.cancel()
        gameJob = null
    }

    companion object {
        const val BOARD_SIZE = 16
    }
}