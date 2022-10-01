package com.bismark.gameofthronecharacters.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bismark.gameofthronecharacters.data_layer.MovieCharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieCharacterViewModel @Inject constructor(
   movieCharacterRepository: MovieCharacterRepository
) : ViewModel() {

    val movieCharacterPagerFlow = movieCharacterRepository.getMovieCharacterPages()
        .cachedIn(viewModelScope)
}
