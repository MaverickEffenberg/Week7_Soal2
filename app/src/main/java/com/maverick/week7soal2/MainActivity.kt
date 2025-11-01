package com.maverick.week7soal2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.maverick.week7soal2.ui.screens.homepage.ArtistExplorerApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtistExplorerApp()
        }
    }
}