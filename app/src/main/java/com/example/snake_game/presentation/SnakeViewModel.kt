package com.example.snake_game.presentation

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.snake_game.data.database.PointsEntity
import com.example.snake_game.data.model.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SnakeViewModel(private val db: MainDb, private  val themeManager: ThemeManager) : ViewModel() {
    @Suppress("UNCHECKED_CAST")
    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val database = checkNotNull(extras[APPLICATION_KEY] as App).database
                val themeManager = ThemeManager(checkNotNull(extras[APPLICATION_KEY] as App).applicationContext)
                return SnakeViewModel(database, themeManager) as T
            }
        }
    }

    private val game = Game(viewModelScope)

    private val _gameState = MutableStateFlow(State(food = Pair(5, 5), snake = listOf(Pair(7, 7))))
    val gameState: Flow<State> = _gameState

    private val isSnakeMoving = mutableStateOf(false)

    private val _currentSnakeSize = mutableIntStateOf(3)
    val currentSnakeSize = _currentSnakeSize

    private val _dialogState = mutableStateOf(false)
    val dialogState = _dialogState

    private val _pointsList = MutableStateFlow(listOf<Int>())
    val pointsList = _pointsList.asStateFlow()

    private var pointsEntity: PointsEntity? = null

    var isDarkTheme = mutableStateOf(themeManager.isDarkTheme)
        private set

    init {
        viewModelScope.launch {
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
                Log.d("MyLog", "update points list with: ${pointsList.joinToString(" ")}")
            }
        }
    }

    fun switchAppTheme() {
        isDarkTheme.value = !isDarkTheme.value
        themeManager.isDarkTheme = isDarkTheme.value
    }


    private fun insertPoints() {
        viewModelScope.launch {
            val namePoint = pointsEntity?.copy(
                points = _currentSnakeSize.intValue
            ) ?: PointsEntity(
                points = _currentSnakeSize.intValue
            )

            db.dao.insertPoints(namePoint)

            pointsEntity = null
            _currentSnakeSize.intValue = 3
        }
    }

    fun clearAllPointsData() {
        viewModelScope.launch {
            db.dao.deleteAllPoints()
        }
    }

    private fun updateNameEntity(minEntity: PointsEntity?) {
        pointsEntity = minEntity
    }

    private fun compareScores() {
        if (pointsEntity != null && _currentSnakeSize.intValue > pointsEntity!!.points) {
            insertPoints()
        }
    }

    fun startGame() {
        _gameState.value = State(food = Pair(5, 5), snake = listOf(Pair(7, 7)))
        _dialogState.value = false
        _currentSnakeSize.intValue = 3
        isSnakeMoving.value = true
        viewModelScope.launch {
            game.start(
                gameState = _gameState,
                isSnakeMoving = isSnakeMoving,
                dialog = _dialogState,
                snakeSize = _currentSnakeSize
            )
        }
    }

    fun restartGame() {
        _gameState.value = State(food = Pair(5, 5), snake = listOf(Pair(7, 7)))
        closeDialog()
        _currentSnakeSize.intValue = 3
        isSnakeMoving.value = true
        viewModelScope.launch {
            game.start(
                gameState = _gameState,
                isSnakeMoving = isSnakeMoving,
                dialog = _dialogState,
                snakeSize = _currentSnakeSize
            )
        }
    }

    fun finishMovingState() {
        isSnakeMoving.value = false
    }

    fun closeDialog() {
        _dialogState.value = false

        if (pointsEntity == null) {
            insertPoints()
        } else compareScores()
    }

    fun onDirectionChange(
        it: Pair<Int, Int>
    ) {
        game.move = it
    }

}