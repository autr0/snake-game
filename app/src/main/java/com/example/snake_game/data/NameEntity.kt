package com.example.snake_game.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "points_table")
data class NameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val points: Int
)
