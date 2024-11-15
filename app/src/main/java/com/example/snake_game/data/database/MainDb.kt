package com.example.snake_game.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.snake_game.data.model.PointsEntity

@Database(
    entities = [
        PointsEntity::class
    ],
    version = 1
)
abstract class MainDb : RoomDatabase() {
    abstract val dao: Dao

    companion object {
        fun createDb(context: Context): MainDb {
            return Room.databaseBuilder(
                context,
                MainDb::class.java,
                "points.db"
            )
//                .fallbackToDestructiveMigration() // this is allowed in development process only!!! -->
//                                                  // all you previous data will be cleared
                .build()
        }
    }

}