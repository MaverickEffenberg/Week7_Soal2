package com.maverick.week7soal2.ui.screens.homepage

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.maverick.week7soal2.ui.theme.ArtistExplorerTheme

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AlbumDetail : Screen("albumDetail/{albumId}") {
        fun createRoute(albumId: String) = "albumDetail/$albumId"
    }
}

@Composable
fun ArtistExplorerApp() {
    ArtistExplorerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Screen.Home.route) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        onAlbumClick = { albumId ->
                            navController.navigate(Screen.AlbumDetail.createRoute(albumId))
                        }
                    )
                }
                composable(
                    route = Screen.AlbumDetail.route,
                    arguments = listOf(navArgument("albumId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val albumId = backStackEntry.arguments?.getString("albumId")
                    if (albumId != null) {
                        AlbumDetailScreen(
                            albumId = albumId,
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}