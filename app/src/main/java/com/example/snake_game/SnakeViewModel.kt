package com.example.snake_game

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.snake_game.data.MainDb
import com.example.snake_game.data.NameEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SnakeViewModel(val db: MainDb) : ViewModel() {
    @Suppress("UNCHECKED_CAST")
    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val database = checkNotNull(extras[APPLICATION_KEY] as App).database
                return SnakeViewModel(database) as T
            }
        }
    }

    val game = Game(viewModelScope)

    private val mutableState = MutableStateFlow(
        State(food = Pair(5, 5), snake = listOf(Pair(7, 7)))
    )
    val state: Flow<State> = mutableState

    private val movingState = mutableStateOf(false)

    private val _currentSnakeSize = MutableStateFlow(mutableStateOf(3))
    val currentSnakeSize = _currentSnakeSize.asStateFlow()

    private val _dialogState =
        MutableStateFlow(mutableStateOf(false))
    val dialogState = _dialogState.asStateFlow()

    val pointsList = db.dao.getAllPoints()
    val pointsList2 = mutableStateOf(emptyList<NameEntity>())

    private var nameEntity: NameEntity? = null

    private fun insertPoints() {
        viewModelScope.launch {
            val namePoint = nameEntity?.copy(
                points = _currentSnakeSize.value.value
            ) ?: NameEntity(
                points = _currentSnakeSize.value.value
            )

            db.dao.insertPoints(namePoint)

            nameEntity = null
            _currentSnakeSize.value.value = 3
        }
    }

//    fun deletePoints() {
//        viewModelScope.launch {
//            db.dao.deletePoints(nameEntity!!)
//        }
//    }

    fun updateNameEntity(minEntity: NameEntity?) {
        nameEntity = minEntity
    }

    private fun compareScores() {
        if (nameEntity != null &&_currentSnakeSize.value.value > nameEntity!!.points) {
            insertPoints()
//            deletePoints()
        }
    }

    fun startGame() {
        mutableState.value = State(food = Pair(5, 5), snake = listOf(Pair(7, 7)))
        _dialogState.value.value = false
//        closeDialog()
        _currentSnakeSize.value.value = 3
        movingState.value = true
        viewModelScope.launch {
            game.start(
                mutableState,
                movingState,
                _dialogState.value,
                _currentSnakeSize.value
            )
        }
    }

    fun restartGame() {
        mutableState.value = State(food = Pair(5, 5), snake = listOf(Pair(7, 7)))
//        _dialogState.value.value = false
        closeDialog()
        _currentSnakeSize.value.value = 3
        movingState.value = true
        viewModelScope.launch {
            game.start(
                mutableState,
                movingState,
                _dialogState.value,
                _currentSnakeSize.value
            )
        }
    }

    fun finishMovingState() {
        movingState.value = false
    }

    fun closeDialog() {
        _dialogState.value.value = false

        if (nameEntity == null) {
            insertPoints()
        } else compareScores()
    }

    fun onDirectionChange(
        it: Pair<Int, Int>
    ) {
        game.move = it
    }

}