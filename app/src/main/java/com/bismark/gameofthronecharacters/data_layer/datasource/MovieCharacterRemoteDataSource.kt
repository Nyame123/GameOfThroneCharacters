package com.bismark.gameofthronecharacters.data_layer.datasource

import com.bismark.gameofthronecharacters.core.Either
import com.bismark.gameofthronecharacters.core.Failure
import com.bismark.gameofthronecharacters.data_layer.INetworkRequest
import com.bismark.gameofthronecharacters.data_layer.service.ApiService
import com.bismark.gameofthronecharacters.database.entities.MovieCharacter

class MovieCharacterRemoteDataSource(
    val apiService: ApiService
): MovieCharacterDataSource, INetworkRequest {

    override suspend fun getMovieCharacter(page: Int, perPage: Int): Either<Failure, List<MovieCharacter>> =
        onSuspendCall { apiService.getMovieCharacters(page,perPage).movieCharacters?.map { it.toMovieCharacter() } ?: emptyList() }
}
