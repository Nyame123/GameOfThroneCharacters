package com.bismark.gameofthronecharacters.di

import com.bismark.gameofthronecharacters.data_layer.MovieCharacterRepository
import com.bismark.gameofthronecharacters.data_layer.datasource.MovieCharacterRemoteDataSource
import com.bismark.gameofthronecharacters.data_layer.service.ApiService
import com.bismark.gameofthronecharacters.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    fun providesMovieCharacterRemoteDataSource(apiService: ApiService): MovieCharacterRemoteDataSource =
        MovieCharacterRemoteDataSource(apiService)

    @Provides
    fun providesMovieCharacterRepository(
        appDatabase: AppDatabase,
        movieCharacterRemoteDataSource: MovieCharacterRemoteDataSource
    ): MovieCharacterRepository =
        MovieCharacterRepository(
            appDatabase = appDatabase,
            movieCharacterRemoteDataSource = movieCharacterRemoteDataSource
        )

}
