package com.bismark.gameofthronecharacters

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.bismark.gameofthronecharacters.data_layer.MovieCharacterRepository
import com.bismark.gameofthronecharacters.database.entities.MovieCharacter
import com.bismark.gameofthronecharacters.provider.provideMovieCharacter
import com.bismark.gameofthronecharacters.ui.MovieCharacterViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieCharacterViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutineTestRule()

    private val movieCharacterRepositoryMock: MovieCharacterRepository = mockk(relaxed = true)
    lateinit var movieCharacterViewModel: MovieCharacterViewModel

    @Before
    fun setUp() {
        movieCharacterViewModel = MovieCharacterViewModel(
            movieCharacterRepository = movieCharacterRepositoryMock,
            dispatcher = Dispatchers.Default
        )
    }

    @Test
    fun `given a movie character repository and no movie data, when request for movieCharacter from paging source then emit empty MovieCharacter list`() = runTest {

        coEvery { movieCharacterRepositoryMock.getMovieCharacterPages() } returns flow {
            emit(PagingData.from(emptyList()))
        }

        movieCharacterViewModel.getPagingDataFlow()

        movieCharacterViewModel.movieCharacterPagerFlow.test {

            val differ = AsyncPagingDataDiffer(
                diffCallback = MovieCharacterDiffCallbackTest(),
                updateCallback = MovieCharacterUpdateCallbackTest(),
                workerDispatcher = Dispatchers.Main
            )

            differ.submitData(awaitItem())
            advanceUntilIdle()

            assertEquals(emptyList<MovieCharacter>(), differ.snapshot().items)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given a movie character repository and a list of movie data, when request for movieCharacter from paging source then emit the same count of data list`() = runTest {

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

        val expectedDataCount = movieDataList.size

        coEvery { movieCharacterRepositoryMock.getMovieCharacterPages() } returns flow {
            emit(PagingData.from(movieDataList))
        }

        movieCharacterViewModel.getPagingDataFlow()

        movieCharacterViewModel.movieCharacterPagerFlow.test {

            val differ = AsyncPagingDataDiffer(
                diffCallback = MovieCharacterDiffCallbackTest(),
                updateCallback = MovieCharacterUpdateCallbackTest(),
                workerDispatcher = Dispatchers.Main
            )

            differ.submitData(awaitItem())
            advanceUntilIdle()

            assertEquals(expectedDataCount, differ.snapshot().items.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    class MovieCharacterDiffCallbackTest : DiffUtil.ItemCallback<MovieCharacter>() {

        override fun areItemsTheSame(oldItem: MovieCharacter, newItem: MovieCharacter): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MovieCharacter, newItem: MovieCharacter): Boolean {
            return oldItem == newItem
        }

    }

    class MovieCharacterUpdateCallbackTest : ListUpdateCallback {

        override fun onInserted(position: Int, count: Int) {}

        override fun onRemoved(position: Int, count: Int) {}

        override fun onMoved(fromPosition: Int, toPosition: Int) {}

        override fun onChanged(position: Int, count: Int, payload: Any?) {}

    }
}
