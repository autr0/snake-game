package com.example.snake_game

import android.util.Log
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
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

    var move = Pair(1, 0)
        set(value) {
            scope.launch {
                mutex.withLock {
                    field = value
                }
            }
        }

    suspend fun start(
        mutableState: MutableStateFlow<State>,
        state: MutableState<Boolean>,
        dialog: MutableState<Boolean>,
        snakeSize: MutableState<Int>
    ) {
        scope.launch {
//            var snakeLength = 3

            while (state.value) {
                delay(150)
                mutableState.update {

                    val newPosition = it.snake.first().let { poz ->
                        mutex.withLock {
                            Pair(
                                (poz.first + move.first + BOARD_SIZE) % BOARD_SIZE,
                                (poz.second + move.second + BOARD_SIZE) % BOARD_SIZE
                            )
                        }
                    }

                    if (newPosition == it.food) {
//                        snakeLength++
                        snakeSize.value++
                    }

                    if (it.snake.contains(newPosition)) {
//                        snakeLength = 2
                        state.value = false
                        dialog.value = true

                    }

                    it.copy(
                        food = if (newPosition == it.food) Pair(
                            Random().nextInt(BOARD_SIZE),
                            Random().nextInt(BOARD_SIZE)
                        ) else it.food,
                        snake = listOf(newPosition) + it.snake.take(
//                            snakeLength
                            snakeSize.value - 1
                        )
                    )
                }

            }

        }

    }

    companion object {
        const val BOARD_SIZE = 16
    }

}