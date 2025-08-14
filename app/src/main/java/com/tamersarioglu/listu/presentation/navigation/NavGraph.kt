package com.tamersarioglu.listu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.tamersarioglu.listu.core.network.ConnectivityObserver
import com.tamersarioglu.listu.presentation.screen.AnimeDetailScreen
import com.tamersarioglu.listu.presentation.screen.FavoritesScreen
import com.tamersarioglu.listu.presentation.screen.SearchScreen
import com.tamersarioglu.listu.presentation.screen.SettingsScreen
import com.tamersarioglu.listu.presentation.screen.TopAnimeScreen
import com.tamersarioglu.listu.core.performance.PerformanceMonitor
import com.tamersarioglu.listu.presentation.ui.theme.ThemeManager

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    connectivityObserver: ConnectivityObserver,
    themeManager: ThemeManager,
    performanceMonitor: PerformanceMonitor
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
                connectivityObserver = connectivityObserver
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
            SettingsScreen(
                themeManager = themeManager,
                performanceMonitor = performanceMonitor
            )
        }
    }
}