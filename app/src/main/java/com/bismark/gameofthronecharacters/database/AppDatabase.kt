package com.bismark.gameofthronecharacters.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bismark.gameofthronecharacters.database.dao.MovieCharacterDao
import com.bismark.gameofthronecharacters.database.entities.MovieCharacter

const val DATABASE_NAME = "app_database"

@Database(entities = arrayOf(MovieCharacter::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieCharacterDao(): MovieCharacterDao
}
