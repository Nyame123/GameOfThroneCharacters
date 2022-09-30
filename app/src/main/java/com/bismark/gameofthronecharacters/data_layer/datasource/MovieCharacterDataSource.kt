package com.bismark.gameofthronecharacters.data_layer.datasource

import com.bismark.gameofthronecharacters.database.entities.MovieCharacter

interface MovieCharacterDataSource {
    suspend fun getMovieCharacter(): List<MovieCharacter>
}
