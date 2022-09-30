package com.bismark.gameofthronecharacters.data_layer.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bismark.gameofthronecharacters.core.leftOrNull
import com.bismark.gameofthronecharacters.core.rightOrNull
import com.bismark.gameofthronecharacters.data_layer.datasource.MovieCharacterRemoteDataSource
import com.bismark.gameofthronecharacters.database.AppDatabase
import com.bismark.gameofthronecharacters.database.entities.MovieCharacter
import com.bismark.gameofthronecharacters.database.entities.MovieCharacterRemoteKeys

private const val STARTING_PAGE_INDEX = 0

@OptIn(ExperimentalPagingApi::class)
class MovieCharacterRemoteMediator(
    val appDatabase: AppDatabase,
    val movieCharacterRemoteDataSource: MovieCharacterRemoteDataSource
) : RemoteMediator<Int, MovieCharacter>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieCharacter>): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                val prevKey =
                    remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state = state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey =
                    remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        val movieCharacterResponse =
            movieCharacterRemoteDataSource.getMovieCharacter(page = page, perPage = state.config.pageSize)

        val endOfPaginationReached = movieCharacterResponse.rightOrNull()?.isEmpty() ?: true
        return if (movieCharacterResponse.isLeft) {
            MediatorResult.Error(Exception(movieCharacterResponse.leftOrNull()?.getFailureMessage()))
        } else {
            val data = movieCharacterResponse.rightOrNull()
            appDatabase.withTransaction {
                //clear the db
                if (loadType == LoadType.REFRESH) {
                    appDatabase.movieCharacterDao().deleteMovieCharacterAll()
                    appDatabase.movieCharacterRemoteKeyDao().deleteAll()
                }

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = data?.map { MovieCharacterRemoteKeys(pageID = it.id, prevKey = prevKey, nextKey = nextKey) }

                appDatabase.movieCharacterDao().saveMovieCharacterAll(data!!)
                appDatabase.movieCharacterRemoteKeyDao().insertAll(keys!!)
            }
            MediatorResult.Success(endOfPaginationReached)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieCharacter>): MovieCharacterRemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movieCharacter ->
                // Get the remote keys of the last item retrieved
                appDatabase.movieCharacterRemoteKeyDao().movieCharacterRemoteKeyId(movieCharacter.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieCharacter>): MovieCharacterRemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movieCharacter ->
                // Get the remote keys of the first items retrieved
                appDatabase.movieCharacterRemoteKeyDao().movieCharacterRemoteKeyId(movieCharacter.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieCharacter>
    ): MovieCharacterRemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                appDatabase.movieCharacterRemoteKeyDao().movieCharacterRemoteKeyId(repoId)
            }
        }
    }
}
