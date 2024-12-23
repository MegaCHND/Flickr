package com.example.flickr

import androidx.core.net.toUri
import com.example.flickr.data.RetrofitFlickrService
import com.example.flickr.entities.ImageData
import com.example.flickr.viewmodels.ImageViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class ImageViewModelTests {
    private lateinit var viewModel: ImageViewModel

    private val mockData = mock<ImageData>()

    private val mockUri = "mockUri".toUri()

    private suspend fun createMockVM(
        shouldError: Boolean = false,
        shouldSucceed: Boolean = false
    ): ImageViewModel {
        val mockService: RetrofitFlickrService = mock()
        when {
            shouldSucceed -> whenever(mockService.getImageData(mockUri)).thenReturn(mockData)
            shouldError -> whenever(mockService.getImageData(mockUri)).thenReturn(null)
        }
        return ImageViewModel(mockService)
    }

    @Test
    fun `When viewmodel is initialized, state is null`() = runTest {
        //GIVEN
        val result = mutableListOf<ImageData?>()
        viewModel = createMockVM(shouldSucceed = true)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(result)
        }

        //THEN
        assertEquals(null, result.first())
    }

    @Test
    fun `When viewmodel gets cached imageData it the imageData is returned`() = runTest {
        //GIVEN
        val result = mutableListOf<ImageData?>()
        viewModel = createMockVM(shouldSucceed = true)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(result)
        }

        //WHEN
        viewModel.loadImage(mockUri)

        //THEN
        assertEquals(mockData, result.last())
    }

    @Test
    fun `When viewmodel trys to get imageData that wasn't cached null is returned`() = runTest {
        //GIVEN
        val result = mutableListOf<ImageData?>()
        viewModel = createMockVM(shouldError = true)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(result)
        }

        //WHEN
        viewModel.loadImage(mockUri)

        //THEN
        assertEquals(null, result.last())
    }
}