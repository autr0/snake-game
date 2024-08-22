package com.example.snake_game.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.snake_game.presentation.SnakeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    goBack: () -> Unit,
    vm: SnakeViewModel
) {
    val dialogState = remember { mutableStateOf(false) }

    val style = MaterialTheme.typography.headlineLarge
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Settings",
                            style = style,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { goBack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "back arrow"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            SettingsItem(
                icon = Icons.Default.Star,
                iconDescription = "star",
                text = "Dark theme"
            ) {
                Switch(
                    checked = vm.isDarkTheme.value,
                    onCheckedChange = {
                        vm.switchAppTheme()
                    },
                    modifier = Modifier.size(50.dp),
                    colors = SwitchDefaults.colors(
                        checkedIconColor = MaterialTheme.colorScheme.primary,
                        checkedThumbColor = MaterialTheme.colorScheme.secondary,
                        uncheckedIconColor = MaterialTheme.colorScheme.secondary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            SettingsItem(
                icon = Icons.Default.Delete,
                iconDescription = "deleteData",
                text = "Delete all scores data",
                isClickable = true,
                onClick = {
                    dialogState.value = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "arrow1",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Divider( // remove that later?
                color = MaterialTheme.colorScheme.primary,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (dialogState.value) {
        DeletePointsDialog(deleteState = dialogState) {
            vm.clearAllPointsData()
        }
    }

    BackHandler {
        goBack()
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    iconDescription: String,
    text: String,
    isClickable: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Divider(
        color = MaterialTheme.colorScheme.primary,
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth()
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isClickable) { onClick?.invoke() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = iconDescription,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                content()
            }
        }

    }

}

@Composable
fun DeletePointsDialog(deleteState: MutableState<Boolean>, clearData: () -> Unit) {
    val style = MaterialTheme.typography.headlineSmall

    AlertDialog(
        onDismissRequest = { deleteState.value = false },
        confirmButton = {
            TextButton(onClick = {
                clearData()
                deleteState.value = false
            }) {
                Text(text = "Delete", style = style)
            }
        },
        dismissButton = {
            TextButton(onClick = { deleteState.value = false }) {
                Text(text = "Cancel", style = style)
            }
        },
        title = { Text(text = "Are you really want to delete all of you scores?", style = style) },
        shape = RoundedCornerShape(5.dp)
    )
}