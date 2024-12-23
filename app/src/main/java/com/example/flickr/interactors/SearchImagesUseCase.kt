package com.example.flickr.interactors

import com.example.flickr.data.FlickrService
import com.example.flickr.entities.ImageData
import javax.inject.Inject

class SearchImagesUseCase @Inject constructor(
    private val flickrService: FlickrService
) {

    suspend operator fun invoke(query: String): Result<List<ImageData>> {
        val tags = query.split(",").map { it.trim() }
        return flickrService.searchImages(tags)
    }
}
