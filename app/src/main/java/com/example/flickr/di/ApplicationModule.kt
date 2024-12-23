package com.example.flickr.di

import android.util.LruCache
import com.example.flickr.data.CACHE_SIZE
import com.example.flickr.data.FlickrService
import com.example.flickr.data.ImageDataCache
import com.example.flickr.data.RetrofitFlickrService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ApplicationModule {

    @Binds
    fun bindFlickrService(
        retrofitFlickrService: RetrofitFlickrService
    ): FlickrService

    companion object {
        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://api.flickr.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        fun provideCache(): ImageDataCache {
            return ImageDataCache(LruCache(CACHE_SIZE))
        }
    }
}
