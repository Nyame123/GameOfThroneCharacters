package com.bismark.gameofthronecharacters.data_layer.datasource

import com.bismark.gameofthronecharacters.core.Either
import com.bismark.gameofthronecharacters.core.Failure
import com.bismark.gameofthronecharacters.database.entities.MovieCharacter

interface MovieCharacterDataSource {
    suspend fun getMovieCharacter(page: Int, perPage: Int): Either<Failure,List<MovieCharacter>>
}
