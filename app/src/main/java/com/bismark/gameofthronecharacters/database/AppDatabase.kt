package com.bismark.gameofthronecharacters.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bismark.gameofthronecharacters.database.dao.MovieCharacterDao
import com.bismark.gameofthronecharacters.database.dao.MovieCharacterRemoteKeyDao
import com.bismark.gameofthronecharacters.database.entities.MovieCharacter
import com.bismark.gameofthronecharacters.database.entities.MovieCharacterRemoteKeys

const val DATABASE_NAME = "app_database"

@Database(entities = [MovieCharacter::class, MovieCharacterRemoteKeys::class], version = 1, exportSchema = false)
@TypeConverters(BookTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieCharacterDao(): MovieCharacterDao
    abstract fun movieCharacterRemoteKeyDao(): MovieCharacterRemoteKeyDao
}
