package com.bismark.gameofthronecharacters.data_layer.paging

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.scan

enum class RemotePresentationState {
    INITIAL, ERROR , REMOTE_LOADING, SOURCE_LOADING, PRESENTED
}

/**
 * Coalesce the [CombinedLoadStates] to [RemotePresentationState]
 **/
fun CombinedLoadStates.asRemotePresentationState(state: RemotePresentationState): RemotePresentationState = when (state) {
    RemotePresentationState.PRESENTED -> when (this.mediator?.append) {
        is LoadState.Loading -> RemotePresentationState.REMOTE_LOADING
        is LoadState.Error -> RemotePresentationState.ERROR
        else -> state
    }
    RemotePresentationState.INITIAL -> when (this.mediator?.append) {
        is LoadState.Loading -> RemotePresentationState.REMOTE_LOADING
        is LoadState.Error -> RemotePresentationState.ERROR
        else -> state
    }
    RemotePresentationState.REMOTE_LOADING -> when (this.source.append) {
        is LoadState.Loading -> RemotePresentationState.SOURCE_LOADING
        is LoadState.Error -> RemotePresentationState.ERROR
        else -> state
    }
    RemotePresentationState.SOURCE_LOADING -> when (this.source.append) {
        is LoadState.NotLoading -> RemotePresentationState.PRESENTED
        is LoadState.Error -> RemotePresentationState.ERROR
        else -> state
    }
    RemotePresentationState.ERROR -> when (this.mediator?.append) {
        is LoadState.Loading -> RemotePresentationState.REMOTE_LOADING
        is LoadState.NotLoading -> when (this.source.append) {
            is LoadState.NotLoading -> RemotePresentationState.PRESENTED
            is LoadState.Error -> RemotePresentationState.ERROR
            else -> state
        }
        is LoadState.Error -> RemotePresentationState.ERROR
        else -> state
    }
}

/**
 * Coalesce the [CombinedLoadStates] to [RemotePresentationState] in a form of a flow
 **/
fun Flow<CombinedLoadStates>.asRemotePresentationState(): Flow<RemotePresentationState> =
    scan(RemotePresentationState.INITIAL) { state, loadState ->
        when (state) {
            RemotePresentationState.PRESENTED -> when (loadState.mediator?.refresh) {
                is LoadState.Loading -> RemotePresentationState.REMOTE_LOADING
                is LoadState.Error -> RemotePresentationState.ERROR
                else -> state
            }
            RemotePresentationState.INITIAL -> when (loadState.mediator?.refresh) {
                is LoadState.Loading -> RemotePresentationState.REMOTE_LOADING
                is LoadState.Error -> RemotePresentationState.ERROR
                else -> state
            }
            RemotePresentationState.REMOTE_LOADING -> when (loadState.source.refresh) {
                is LoadState.Loading -> RemotePresentationState.SOURCE_LOADING
                is LoadState.Error -> RemotePresentationState.ERROR
                else -> state
            }
            RemotePresentationState.SOURCE_LOADING -> when (loadState.source.refresh) {
                is LoadState.NotLoading -> RemotePresentationState.PRESENTED
                is LoadState.Error -> RemotePresentationState.ERROR
                else -> state
            }
            RemotePresentationState.ERROR -> when (loadState.mediator?.refresh) {
                is LoadState.Loading -> RemotePresentationState.REMOTE_LOADING
                is LoadState.NotLoading -> when (loadState.source.refresh) {
                    is LoadState.NotLoading -> RemotePresentationState.PRESENTED
                    is LoadState.Error -> RemotePresentationState.ERROR
                    else -> state
                }
                is LoadState.Error -> RemotePresentationState.ERROR
                else -> state
            }
        }
    }.distinctUntilChanged()
