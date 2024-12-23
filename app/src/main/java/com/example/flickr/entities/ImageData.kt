package com.example.flickr.entities

import android.net.Uri

data class ImageData(
    val uri: Uri,
    val title: String,
    val description: String,
    val author: String,
    val publishedDate: String
)
