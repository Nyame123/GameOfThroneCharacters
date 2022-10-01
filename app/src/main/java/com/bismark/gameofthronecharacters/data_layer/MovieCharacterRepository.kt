package com.bismark.gameofthronecharacters.data_layer

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.bismark.gameofthronecharacters.data_layer.datasource.MovieCharacterRemoteDataSource
import com.bismark.gameofthronecharacters.data_layer.paging.MovieCharacterRemoteMediator
import com.bismark.gameofthronecharacters.database.AppDatabase
import javax.inject.Inject

internal const val NETWORK_PAGE_SIZE = 10


class MovieCharacterRepository @Inject constructor(
    val appDatabase: AppDatabase,
    val movieCharacterRemoteDataSource: MovieCharacterRemoteDataSource
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getMovieCharacterPages() = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false
        ),
        remoteMediator = MovieCharacterRemoteMediator(
            appDatabase = appDatabase,
            movieCharacterRemoteDataSource = movieCharacterRemoteDataSource
        ),
        pagingSourceFactory = { appDatabase.movieCharacterDao().getMovieCharacters() }
    ).flow
}
