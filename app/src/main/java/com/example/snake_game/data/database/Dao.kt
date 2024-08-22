package com.example.snake_game.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoints(pointsEntity: PointsEntity)

//    @Update
//    suspend fun updatePoints(nameEntity: NameEntity)

    @Delete
    suspend fun deletePoints(pointsEntity: PointsEntity)

    @Query("SELECT * FROM points_table")
    fun getAllPoints(): Flow<List<PointsEntity>>

    @Query("DELETE FROM points_table")
    suspend fun deleteAllPoints()

}