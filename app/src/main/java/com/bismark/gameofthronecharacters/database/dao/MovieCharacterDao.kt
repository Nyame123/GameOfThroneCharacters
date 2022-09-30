package com.bismark.gameofthronecharacters.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bismark.gameofthronecharacters.database.entities.MovieCharacter
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieCharacterDao {

    @Query("select * from MovieCharacter")
    suspend fun getMovieCharacters(): List<MovieCharacter>

    @Query("select * from MovieCharacter where id= :id")
    suspend fun getMovieCharacter(id: Long): MovieCharacter

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovieCharacter(movieCharacter: MovieCharacter)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovieCharacterAll(movieCharacter: List<MovieCharacter>)

    @Update
    suspend fun updateMovieCharacter(movieCharacter: MovieCharacter)

    @Delete
    suspend fun deleteMovieCharacter(movieCharacter: MovieCharacter)
}
