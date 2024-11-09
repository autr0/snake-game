package com.example.snake_game.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.snake_game.App
import com.example.snake_game.data.ThemeManager
import com.example.snake_game.data.Game
import com.example.snake_game.data.database.MainDb
import com.example.snake_game.data.model.PointsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SnakeViewModel(private val db: MainDb, private val themeManager: ThemeManager) : ViewModel() {
    @Suppress("UNCHECKED_CAST")
    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val database = checkNotNull(extras[APPLICATION_KEY] as App).database
                val themeManager =
                    ThemeManager(checkNotNull(extras[APPLICATION_KEY] as App).applicationContext)
                return SnakeViewModel(database, themeManager) as T
            }
        }
    }

    private val game = Game(viewModelScope)

    private val _gameState = MutableStateFlow(State())
    val gameState: StateFlow<State> = _gameState.asStateFlow()

    private val _pointsList = MutableStateFlow(listOf<Int>())
    val pointsList = _pointsList.asStateFlow()

    private var pointsEntity: PointsEntity? = null

    var isDarkTheme = mutableStateOf(themeManager.isDarkTheme)
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
//          collect pointsData
            db.dao.getAllPoints().collect { pointsEntitiesList ->
                if (pointsEntitiesList.size > 4) {
                    val min = pointsEntitiesList.minOfOrNull { it.points }
                    val ent = pointsEntitiesList.find { it.points == min }
                    updateNameEntity(ent)
                }
                val pointsList = mutableListOf<Int>()
                pointsEntitiesList.sortedByDescending { it.points }
                    .map { pointsList.add(it.points) }

                _pointsList.update { pointsList }
            }
        }
    }

    fun switchAppTheme() {
        isDarkTheme.value = !isDarkTheme.value
        themeManager.isDarkTheme = isDarkTheme.value
    }

    private fun insertPoints(newPoints: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val namePoint = pointsEntity?.copy(
                points = newPoints
            ) ?: PointsEntity(
                points = newPoints
            )
            db.dao.insertPoints(namePoint)

            pointsEntity = null
            _gameState.update { state -> state.copy(currentSnakeSize = 3) }
        }
    }

    fun clearAllPointsData() {
        viewModelScope.launch(Dispatchers.IO) {
            db.dao.deleteAllPoints()
            pointsEntity = null
        }
    }

    private fun updateNameEntity(minEntity: PointsEntity?) {
        pointsEntity = minEntity
    }

    private fun compareScores(newPoints: Int) {
        if (pointsEntity != null && _gameState.value.currentSnakeSize > pointsEntity!!.points) {
            insertPoints(newPoints)
        }
    }

    private fun initializeGameState() {
        _gameState.update { state ->
            state.copy(
                food = Pair(5, 5),
                snake = listOf(Pair(7, 7)),
                currentSnakeSize = 3,
                isSnakeMoving = true,
                isShowDialog = false,
                navigateBack = false
            )
        }
    }

    fun startGame() {
        initializeGameState()
        game.start(gameState = _gameState)
    }

    fun restartGame() {
        handlePoints()
        stopGame()
        initializeGameState()
        game.start(gameState = _gameState)
    }

    fun closeDialog() {
        _gameState.update { state -> state.copy(
            isShowDialog = false,
            navigateBack = true
        ) }
    }

    fun stopGame() {
        game.stopGame()
        _gameState.update { state ->
            state.copy(isSnakeMoving = false)
        }
    }

    fun handlePoints() {
        val newPoints = _gameState.value.currentSnakeSize

        if (pointsEntity == null) {
            insertPoints(newPoints)
        } else {
            compareScores(newPoints)
        }
    }

    fun onDirectionChange(newValue: Pair<Int, Int>) {
        game.move = newValue
    }

}