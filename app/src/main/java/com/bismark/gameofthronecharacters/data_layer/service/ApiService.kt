package com.bismark.gameofthronecharacters.data_layer.service

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {

        const val BASE_URL = "https://www.anapioficeandfire.com/api/"
        private const val CHARACTERS = "characters"
        private const val PAGE_QUERY = "page"
        private const val PER_PAGE_QUERY = "pageSize"
    }

    @GET(CHARACTERS)
    suspend fun getMovieCharacters(
        @Query(PAGE_QUERY) page: Int? = null,
        @Query(PER_PAGE_QUERY) perPage: Int? = null
    ): List<MovieCharacterRaw>
}
