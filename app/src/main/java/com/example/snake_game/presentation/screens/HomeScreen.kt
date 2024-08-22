package com.example.snake_game.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.snake_game.presentation.SnakeViewModel

@Composable
fun HomeScreen(
    goAndPlay: () -> Unit,
    vm: SnakeViewModel,
    openSettings: () -> Unit
) {
    val style = MaterialTheme.typography.headlineMedium

    val pointsList = vm.pointsList.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp,end = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { openSettings() }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "settings",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.48f)
                .padding(12.dp)
                .background(MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
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
                        pointsList.value.forEach { point ->
                            Text(text = "$point", style = style)
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