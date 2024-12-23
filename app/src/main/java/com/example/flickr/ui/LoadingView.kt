package com.example.flickr.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.example.flickr.R
import com.example.flickr.viewmodels.LoadingState

@Composable
fun LoadingView(
    state: LoadingState,
    onDismissRequest: ()->Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is LoadingState.Idle -> {
                // No-op
            }
            is LoadingState.Loading -> {
                CircularProgressIndicator()
            }
            is LoadingState.Error -> {
                Dialog(
                    onDismissRequest = onDismissRequest,
                ) {
                    val context = LocalContext.current
                    val errorTemplate = remember { context.getString(R.string.error_template) }
                    val errorMessage = remember { errorTemplate.format(state.message) }
                    Text(text = errorMessage)
                }
            }
        }

        content()
    }
}
