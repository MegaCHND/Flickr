package com.example.flickr.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.flickr.R
import com.example.flickr.entities.ImageData
import com.example.flickr.viewmodels.ImageSearchUiState
import com.example.flickr.viewmodels.ImageSearchViewModel


@Composable
fun FlickrSearchScreen(
    onViewImage: (ImageData) -> Unit,
    viewModel: ImageSearchViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    FlickrSearchScreen(
        state = state,
        modifier = modifier,
        onTapImage = onViewImage,
        onQueryChange = viewModel::onQueryChange,
        onClearError = viewModel::onClearError,
    )
}

@Composable
fun FlickrSearchScreen(
    state: ImageSearchUiState,
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit,
    onTapImage: (ImageData) -> Unit,
    onClearError: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        var query by remember { mutableStateOf("") }
        TextField(
            value = query,
            onValueChange = { query = it; onQueryChange(it) },
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            maxLines = 1,
            label = { Text(text = LocalContext.current.getString(R.string.label_search)) }
        )
        LoadingView(
            state = state.loadingState,
            onDismissRequest = onClearError,
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(100.dp),
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                items(state.images) {
                    AsyncImage(
                        model = it.uri,
                        contentDescription = it.description,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                            onTapImage(it)
                        }
                    )
                }
            }
        }
    }
}
