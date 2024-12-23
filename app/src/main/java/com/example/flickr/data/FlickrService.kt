package com.example.flickr.data

import android.net.Uri
import com.example.flickr.entities.ImageData

interface FlickrService {
    suspend fun searchImages(tags: List<String>): Result<List<ImageData>>
    suspend fun getImageData(uri: Uri): ImageData?
}
