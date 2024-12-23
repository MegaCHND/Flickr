package com.example.flickr

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.flickr.navigation.Screen
import com.example.flickr.ui.FlickrSearchScreen
import com.example.flickr.ui.FlickrViewScreen
import com.example.flickr.ui.theme.ChallengeApplicationTheme
import com.example.flickr.viewmodels.ImageSearchViewModel
import com.example.flickr.viewmodels.ImageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChallengeApplicationTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Search,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Screen.Search> {
                            val viewModel: ImageSearchViewModel = hiltViewModel()
                            FlickrSearchScreen(
                                onViewImage = {
                                    viewModel.onImageSelected(it)
                                    navController.navigate(Screen.Image(it.uri.toString()))
                                },
                                viewModel = viewModel,
                            )
                        }

                        composable<Screen.Image> { backStackEntry ->
                            val route: Screen.Image = backStackEntry.toRoute()

                            val viewModel: ImageViewModel = hiltViewModel()
                            val state by viewModel.state.collectAsState()
                            LaunchedEffect(route.uri) { viewModel.loadImage(Uri.parse(route.uri)) }
                            FlickrViewScreen(
                                state = state,
                            )
                        }
                    }
                }
            }
        }
    }
}
