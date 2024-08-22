package com.example.snake_game.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "points_table")
data class PointsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val points: Int
)
