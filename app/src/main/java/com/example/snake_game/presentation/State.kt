package com.example.snake_game.presentation

data class State(
    val food: Pair<Int, Int> =  Pair(5, 5),
    val snake: List<Pair<Int, Int>> = listOf(Pair(7, 7)),
    val isSnakeMoving: Boolean = false,
    val currentSnakeSize: Int = 3,
    val isShowDialog: Boolean = false,
    val navigateBack: Boolean = false // need it to have the right order: closing the dialog then navigation
)