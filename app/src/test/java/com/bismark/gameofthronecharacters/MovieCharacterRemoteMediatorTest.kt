package com.bismark.gameofthronecharacters

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bismark.gameofthronecharacters.core.Either
import com.bismark.gameofthronecharacters.core.Failure
import com.bismark.gameofthronecharacters.data_layer.datasource.MovieCharacterRemoteDataSource
import com.bismark.gameofthronecharacters.data_layer.paging.MovieCharacterRemoteMediator
import com.bismark.gameofthronecharacters.database.AppDatabase
import com.bismark.gameofthronecharacters.database.entities.MovieCharacter
import com.bismark.gameofthronecharacters.provider.provideMovieCharacter
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class MovieCharacterRemoteMediatorTest {

    @get:Rule
    val coroutinesTestRule = CoroutineTestRule()

    private val appDatabaseMock: AppDatabase = mockk(relaxed = true)
    private val movieCharacterRemoteDataSourceMock: MovieCharacterRemoteDataSource = mockk(relaxed = true)
    lateinit var movieCharacterRemoteMediator: MovieCharacterRemoteMediator

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(
            "androidx.room.RoomDatabaseKt"
        )
        val transactionLambda = slot<suspend () -> R>()
        coEvery { appDatabaseMock.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }
        movieCharacterRemoteMediator = MovieCharacterRemoteMediator(
            appDatabase = appDatabaseMock,
            movieCharacterRemoteDataSource = movieCharacterRemoteDataSourceMock
        )
    }

    @Test
    fun `given appDatabase and remote Movie DataSource, when remote mediator loads a page and brings list of data return MediatorResult Success`() =
        runTest {

            val page = 1
            val pageSize = 10
            val movieDataList = mutableListOf<MovieCharacter>()
            movieDataList += provideMovieCharacter(
                id = 1,
                name = "Afia",
                gender = "Female"
            )
            movieDataList += provideMovieCharacter(
                id = 2,
                name = "Famous",
                gender = "Male"
            )

            movieDataList += provideMovieCharacter(
                id = 3,
                name = "Bismark",
                gender = "Male"
            )

            coEvery {
                movieCharacterRemoteDataSourceMock.getMovieCharacter(page = page, perPage = pageSize)
            } returns Either.Right(movieDataList)

            val pagingState = PagingState<Int, MovieCharacter>(
                pages = listOf(),
                anchorPosition = null,
                config = PagingConfig(pageSize = 10),
                leadingPlaceholderCount = 10
            )

            val result = movieCharacterRemoteMediator.load(LoadType.REFRESH, pagingState)

            assertTrue(result is RemoteMediator.MediatorResult.Success)
            assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        }

    @Test
    fun `given appDatabase and remote Movie DataSource, when remote mediator loads a page and brings empty list return MediatorResult Success that ends the pagination`() =
        runTest {

            val page = 1
            val pageSize = 10
            val movieDataList = mutableListOf<MovieCharacter>()

            coEvery {
                movieCharacterRemoteDataSourceMock.getMovieCharacter(page = page, perPage = pageSize)
            } returns Either.Right(movieDataList)

            val pagingState = PagingState<Int, MovieCharacter>(
                pages = listOf(),
                anchorPosition = null,
                config = PagingConfig(pageSize = 10),
                leadingPlaceholderCount = 10
            )

            val result = movieCharacterRemoteMediator.load(LoadType.REFRESH, pagingState)

            assertTrue(result is RemoteMediator.MediatorResult.Success)
            assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        }

    @Test
    fun `given appDatabase and remote Movie DataSource, when remote mediator loads a page and error occurs return MediatorResult Error`() =
        runTest {

            val page = 1
            val pageSize = 10
            val movieDataList = mutableListOf<MovieCharacter>()

            coEvery {
                movieCharacterRemoteDataSourceMock.getMovieCharacter(page = page, perPage = pageSize)
            } returns Either.Left(Failure.ServerConnectionError(Exception("Error occurred")))

            val pagingState = PagingState<Int, MovieCharacter>(
                pages = listOf(),
                anchorPosition = null,
                config = PagingConfig(pageSize = 10),
                leadingPlaceholderCount = 10
            )

            val result = movieCharacterRemoteMediator.load(LoadType.REFRESH, pagingState)

            assertTrue(result is RemoteMediator.MediatorResult.Error)
        }
}
