package com.bismark.gameofthronecharacters.data_layer.datasource

import com.bismark.gameofthronecharacters.database.dao.MovieCharacterDao
import com.bismark.gameofthronecharacters.database.entities.MovieCharacter

class MovieCharacterLocalDataSource(private val movieCharacterDao: MovieCharacterDao) : MovieCharacterDataSource {

    override suspend fun getMovieCharacter(): List<MovieCharacter> =
        movieCharacterDao.getMovieCharacters()
}
