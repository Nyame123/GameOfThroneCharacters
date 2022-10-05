package com.bismark.gameofthronecharacters.di

import android.content.Context
import androidx.room.Room
import com.bismark.gameofthronecharacters.data_layer.service.ApiService
import com.bismark.gameofthronecharacters.database.AppDatabase
import com.bismark.gameofthronecharacters.database.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private const val CONNECT_TIMEOUT = 30L
private const val READ_TIMEOUT = 30L
private const val WRITE_TIMEOUT = 30L

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesOKHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    @Named("Background")
    fun provideCoroutineBackgroundDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Singleton
    @Provides
    fun providesRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(ApiService.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun providesRoomDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
}
