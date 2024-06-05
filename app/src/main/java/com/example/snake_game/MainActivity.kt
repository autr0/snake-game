package com.example.snake_game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snake_game.ui.theme.Snake_gameTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Snake_gameTheme {
                val viewModel: SnakeViewModel = viewModel(factory = SnakeViewModel.factory)


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

data class State(val food: Pair<Int, Int>, val snake: List<Pair<Int, Int>>)

@Composable
fun Snake(
    openPreviousScreen: () -> Unit,
    vm: SnakeViewModel
) {
    val state = vm.state.collectAsState(initial = null)

    val size by vm.currentSnakeSize.collectAsState()

    val dialogState by vm.dialogState.collectAsState()

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
                    text = "${
//                    state.value?.snake?.size ?: 0
                        size.value
                    }"
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

@Composable
fun Buttons(onDirectionChange: (Pair<Int, Int>) -> Unit) {
    val buttonSize = Modifier.size(64.dp)
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
        Button(
            onClick = { onDirectionChange(Pair(0, -1)) },
            modifier = buttonSize,
            shape = RoundedCornerShape(5.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                modifier = buttonSize
            )
        }
        Row {
            Button(
                onClick = { onDirectionChange(Pair(-1, 0)) },
                modifier = buttonSize,
                shape = RoundedCornerShape(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    modifier = buttonSize
                )
            }
            Spacer(modifier = buttonSize)
            Button(
                onClick = { onDirectionChange(Pair(1, 0)) },
                modifier = buttonSize,
                shape = RoundedCornerShape(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = buttonSize
                )
            }
        }
        Button(
            onClick = { onDirectionChange(Pair(0, 1)) },
            modifier = buttonSize,
            shape = RoundedCornerShape(5.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = buttonSize
            )
        }
    }
}

@Composable
fun Board(state: State) {
    BoxWithConstraints(Modifier.padding(16.dp)) {
        val tileSize = maxWidth / Game.BOARD_SIZE

        Box(
            Modifier
                .size(maxWidth)
                .border(2.dp, Color.Green)
        )

        Box(
            Modifier
                .offset(x = tileSize * state.food.first, y = tileSize * state.food.second)
                .size(tileSize)
                .background(
                    Color.Green, CircleShape
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
                            Color.Green, ShapeDefaults.Small
                        )
                )
            }
        }
    }
}


@Composable
fun Navigation(
    vm: SnakeViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                { navController.navigate("game") },
                vm
            )

        }

        composable("game") {
            Snake(
                { navController.popBackStack() },
                vm = vm
            )
        }

    }

}

@Composable
fun HomeScreen(
    goAndPlay: () -> Unit,
    vm: SnakeViewModel
) {
    val style = MaterialTheme.typography.headlineMedium

    val pointsList = vm.pointsList.collectAsState(initial = emptyList())

    if (pointsList.value.size > 4) {
        val min = pointsList.value.minOfOrNull { it.points }
        val ent = pointsList.value.find { it.points == min }
        vm.updateNameEntity(ent)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(2.dp, Color.Green)
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))

            Text(
                text = "Leaderboard",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.1f))


            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.3f),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "1.", style = style)
                    Text(text = "2.", style = style)
                    Text(text = "3.", style = style)
                    Text(text = "4.", style = style)
                    Text(text = "5.", style = style)
                }
                Column(
                    modifier = Modifier.fillMaxWidth(0.42f),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (pointsList.value.isNotEmpty()) {
                        pointsList.value.sortedByDescending { it.points }.forEach { entity ->
                            Text(text = "${entity.points}", style = style)
                        }
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "points", style = style)
                    Text(text = "points", style = style)
                    Text(text = "points", style = style)
                    Text(text = "points", style = style)
                    Text(text = "points", style = style)

                }

            }

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier.size(64.dp),
                onClick = {
                    goAndPlay()
                    vm.startGame()
                },
                shape = RoundedCornerShape(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "play"
                )
            }
        }
    }
}

@Composable
fun ScoreDialog(
    vm: SnakeViewModel,
    goHome: () -> Unit
) {
    val score by vm.currentSnakeSize.collectAsState()

    Dialog(
        onDismissRequest = {
            vm.closeDialog()
            goHome()
        }
    ) {
        Card(
            shape = RoundedCornerShape(5.dp),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "You scored", style = MaterialTheme.typography.headlineLarge)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${score.value}",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "points", style = MaterialTheme.typography.headlineLarge)
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { vm.restartGame() },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .clip(RoundedCornerShape(5.dp))
                        .border(BorderStroke(1.dp, Color.DarkGray))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "restart",
                        modifier = Modifier.size(75.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

}