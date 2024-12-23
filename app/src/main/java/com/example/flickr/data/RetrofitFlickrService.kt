package com.example.flickr.data

import android.net.Uri
import com.example.flickr.entities.ImageData
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

class RetrofitFlickrService @Inject constructor(
    private val retrofit: Retrofit,
    private val cache: ImageDataCache,
) : FlickrService {

    private val api: FlickrApi by lazy {
        retrofit.create(FlickrApi::class.java)
    }

    override suspend fun searchImages(tags: List<String>): Result<List<ImageData>> {
        return api.searchImages(tags.joinToString(",")).body()?.let { result ->
            result.items.map { flickrImage ->
                ImageData(
                    uri = Uri.parse(flickrImage.media.m),
                    title = flickrImage.title,
                    author = flickrImage.author,
                    description = flickrImage.description,
                    publishedDate = flickrImage.published,
                )
            }.also {
                it.take(CACHE_SIZE).forEach { imageData ->
                    cache.put(imageData)
                }
            }.let { Result.success(it) }
        } ?: Result.failure(Exception("Failed to load images"))
    }

    override suspend fun getImageData(uri: Uri): ImageData? {
        return cache.get(uri)
    }
}

/**
 * Example URL: https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1&tags=porcupine
 */
interface FlickrApi {
    @GET("services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun searchImages(@Query("tags") tags: String): Response<FlickrSearchResult>
}

data class FlickrSearchResult(
    val items: List<FlickrImage>
)

data class FlickrImage(
    val title: String,
    val media: Media,
    val description: String,
    val author: String,
    val published: String
)

data class Media(
    val m: String
)
