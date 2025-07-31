package com.tamersarioglu.listu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tamersarioglu.listu.presentation.screen.TopAnimeScreen
import com.tamersarioglu.listu.presentation.ui.theme.ListuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListuTheme {
                TopAnimeScreen()
            }
        }
    }
}