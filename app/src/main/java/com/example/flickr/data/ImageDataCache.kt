package com.example.flickr.data

import android.net.Uri
import android.util.LruCache
import com.example.flickr.entities.ImageData

class ImageDataCache(
    private val lruCache: LruCache<Uri, ImageData>
) {
    fun put(imageData: ImageData) {
        lruCache.put(imageData.uri, imageData)
    }

    fun get(uri: Uri): ImageData? {
        return lruCache.get(uri)
    }
}

const val CACHE_SIZE = 50
