package com.tamersarioglu.listu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.tamersarioglu.listu.presentation.screen.AnimeDetailScreen
import com.tamersarioglu.listu.presentation.screen.FavoritesScreen
import com.tamersarioglu.listu.presentation.screen.SearchScreen
import com.tamersarioglu.listu.presentation.screen.SettingsScreen
import com.tamersarioglu.listu.presentation.screen.TopAnimeScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = TopAnimeRoute
    ) {
        composable<TopAnimeRoute> {
            TopAnimeScreen(
                onAnimeClick = { malId ->
                    navController.navigate(AnimeDetailRoute(malId = malId))
                },
                onSearchClick = { navController.navigate(SearchRoute) }
            )
        }
        
        composable<AnimeDetailRoute> { backStackEntry ->
            val animeDetailRoute = backStackEntry.toRoute<AnimeDetailRoute>()
            AnimeDetailScreen(
                malId = animeDetailRoute.malId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<SearchRoute> {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onAnimeClick = { malId -> navController.navigate(AnimeDetailRoute(malId)) }
            )
        }

        composable<FavoritesRoute> {
            FavoritesScreen(
                onAnimeClick = { malId ->
                    navController.navigate(AnimeDetailRoute(malId = malId))
                }
            )
        }

        composable<SettingsRoute> {
            SettingsScreen()
        }
    }
}