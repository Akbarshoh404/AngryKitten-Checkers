package com.example.angrykitten_checkers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.angrykitten_checkers.navigation.AppNavigation
import com.example.angrykitten_checkers.ui.theme.AngryKittenCheckersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AngryKittenCheckersTheme {
                AppNavigation()
            }
        }
    }
}