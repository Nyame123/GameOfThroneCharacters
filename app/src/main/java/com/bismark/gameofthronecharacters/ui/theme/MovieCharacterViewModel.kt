package com.bismark.gameofthronecharacters.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bismark.gameofthronecharacters.data_layer.MovieCharacterRepository
import com.bismark.gameofthronecharacters.database.entities.MovieCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MovieCharacterViewModel @Inject constructor(
   movieCharacterRepository: MovieCharacterRepository
) : ViewModel() {

    val selectedCharacter: MutableStateFlow<MovieCharacter> = MutableStateFlow(MovieCharacter.empty())
    val movieCharacterPagerFlow = movieCharacterRepository.getMovieCharacterPages()
        .cachedIn(viewModelScope)

    fun selectCharacter(movieCharacter: MovieCharacter) {
        selectedCharacter.update { movieCharacter }
    }
}
