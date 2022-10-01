package com.bismark.gameofthronecharacters.data_layer.datasource

import com.bismark.gameofthronecharacters.core.Either
import com.bismark.gameofthronecharacters.core.Failure
import com.bismark.gameofthronecharacters.data_layer.INetworkRequest
import com.bismark.gameofthronecharacters.data_layer.service.ApiService
import com.bismark.gameofthronecharacters.database.entities.MovieCharacter
import javax.inject.Inject

class MovieCharacterRemoteDataSource @Inject constructor(
    val apiService: ApiService
): MovieCharacterDataSource, INetworkRequest {

    override suspend fun getMovieCharacter(page: Int, perPage: Int): Either<Failure, List<MovieCharacter>> =
        onSuspendCall {
            val result = apiService.getMovieCharacters(page,perPage)
                result.map { it.toMovieCharacter() }
        }
}
