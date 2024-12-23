package com.example.flickr.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Search : Screen()
    @Serializable
    data class Image(
        val uri: String
    ) : Screen()
}
