package com.example.snake_game.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoints(nameEntity: NameEntity)

//    @Update
//    suspend fun updatePoints(nameEntity: NameEntity)

    @Delete
    suspend fun deletePoints(nameEntity: NameEntity)

    @Query("SELECT * FROM points_table")
    fun getAllPoints(): Flow<List<NameEntity>>
}