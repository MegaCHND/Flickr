package com.example.flickr.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickr.data.ImageDataCache
import com.example.flickr.entities.ImageData
import com.example.flickr.interactors.SearchImagesUseCase
import com.example.flickr.utils.nullToEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageSearchViewModel @Inject constructor(
    private val searchImages: SearchImagesUseCase,
    private val imageDataCache: ImageDataCache
): ViewModel() {

    private val mutableState: MutableStateFlow<ImageSearchUiState> = MutableStateFlow(
        ImageSearchUiState(emptyList(), LoadingState.Idle)
    )
    val state: StateFlow<ImageSearchUiState> = mutableState.asStateFlow()

    private var searchJob: Job? = null
    fun onQueryChange(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            mutableState.update {
                it.copy(loadingState = LoadingState.Loading)
            }
            searchImages(query)
                .onSuccess {  result ->
                    mutableState.update {
                        it.copy(images = result, loadingState = LoadingState.Idle)
                    }
                }
                .onFailure {  error ->
                    mutableState.update {
                        Log.e("Search", "Failed to load images", error)
                        it.copy(loadingState = LoadingState.Error(error.message.nullToEmpty()))
                    }
                }
        }
    }

    fun onClearError() {
        mutableState.update {
            it.copy(loadingState = LoadingState.Idle)
        }
    }

    fun onImageSelected(it: ImageData) {
        imageDataCache.put(it) // ensure ImageData is in cache
    }
}

data class ImageSearchUiState(
    val images: List<ImageData>,
    val loadingState: LoadingState
)

sealed class LoadingState {
    data object Loading : LoadingState()
    data object Idle : LoadingState()
    data class Error(val message: String) : LoadingState()
}
