package com.bismark.gameofthronecharacters.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bismark.gameofthronecharacters.data_layer.MovieCharacterRepository
import com.bismark.gameofthronecharacters.database.entities.MovieCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MovieCharacterViewModel @Inject constructor(
   val movieCharacterRepository: MovieCharacterRepository,
   @Named("Background") val dispatcher: CoroutineDispatcher
) : ViewModel() {
    val selectedCharacter: MutableStateFlow<MovieCharacter> = MutableStateFlow(MovieCharacter.empty())
    lateinit var movieCharacterPagerFlow: Flow<PagingData<MovieCharacter>>

    init {
        getPagingDataFlow()
    }


    fun getPagingDataFlow(){
        movieCharacterPagerFlow = movieCharacterRepository.getMovieCharacterPages()
            .flowOn(dispatcher)
            .cachedIn(viewModelScope)
    }

    fun selectCharacter(movieCharacter: MovieCharacter) {
        selectedCharacter.update { movieCharacter }
    }
}
