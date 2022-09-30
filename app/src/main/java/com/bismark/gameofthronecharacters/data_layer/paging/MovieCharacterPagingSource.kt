package com.bismark.gameofthronecharacters.data_layer.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bismark.gameofthronecharacters.data_layer.service.ApiService
import com.bismark.gameofthronecharacters.database.entities.MovieCharacter

private const val STARTING_PAGE_INDEX = 0
internal const val NETWORK_PAGE_SIZE = 20
class MovieCharacterPagingSource(val apiService: ApiService) : PagingSource<Int, MovieCharacter>(){

    /**
     * This method is used for refresh call to the [androidx.paging.PagingSource.load]
     * after the initial load.
     **/
    override fun getRefreshKey(state: PagingState<Int, MovieCharacter>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieCharacter> {
        val position = params.key ?: STARTING_PAGE_INDEX
    }
}
