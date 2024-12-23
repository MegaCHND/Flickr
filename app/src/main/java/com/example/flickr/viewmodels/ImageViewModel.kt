package com.example.flickr.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickr.data.RetrofitFlickrService
import com.example.flickr.entities.ImageData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val flickrService: RetrofitFlickrService
): ViewModel() {

    private val mutableState = MutableStateFlow<ImageData?>(null)
    val state: StateFlow<ImageData?> = mutableState.asStateFlow()

    fun loadImage(uri: Uri) {
        viewModelScope.launch {
            mutableState.update {
                flickrService.getImageData(uri)
            }
        }
    }
}
