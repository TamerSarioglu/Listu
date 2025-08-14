package com.tamersarioglu.listu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tamersarioglu.listu.presentation.navigation.FavoritesRoute
import com.tamersarioglu.listu.presentation.navigation.NavGraph
import com.tamersarioglu.listu.presentation.navigation.SearchRoute
import com.tamersarioglu.listu.presentation.navigation.SettingsRoute
import com.tamersarioglu.listu.presentation.navigation.TopAnimeRoute
import com.tamersarioglu.listu.presentation.ui.theme.ListuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListuTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavBar(
                            currentDestinationRoute = navController.currentBackStackEntryAsState().value?.destination?.route,
                            onNavigateTop = {
                                navController.navigate(TopAnimeRoute) {
                                    popUpTo(0) { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            onNavigateSearch = {
                                navController.navigate(SearchRoute) {
                                    popUpTo(0) { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            onNavigateFavorites = {
                                navController.navigate(FavoritesRoute) {
                                    popUpTo(0) { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            onNavigateSettings = {
                                navController.navigate(SettingsRoute) {
                                    popUpTo(0) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        NavGraph(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomNavBar(
    currentDestinationRoute: String?,
    onNavigateTop: () -> Unit,
    onNavigateSearch: () -> Unit,
    onNavigateFavorites: () -> Unit,
    onNavigateSettings: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentDestinationRoute?.contains("TopAnimeRoute") == true,
            onClick = onNavigateTop,
            icon = { Icon(Icons.Filled.Home, contentDescription = "Top") },
            label = { Text("Top") }
        )
        NavigationBarItem(
            selected = currentDestinationRoute?.contains("SearchRoute") == true,
            onClick = onNavigateSearch,
            icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            label = { Text("Search") }
        )
        NavigationBarItem(
            selected = currentDestinationRoute?.contains("FavoritesRoute") == true,
            onClick = onNavigateFavorites,
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites") },
            label = { Text("Favorites") }
        )
        NavigationBarItem(
            selected = currentDestinationRoute?.contains("SettingsRoute") == true,
            onClick = onNavigateSettings,
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            label = { Text("Settings") }
        )
    }
}