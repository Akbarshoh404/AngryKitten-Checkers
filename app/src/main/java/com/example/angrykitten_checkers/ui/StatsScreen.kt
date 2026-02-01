package com.example.angrykitten_checkers.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.angrykitten_checkers.viewmodel.CheckersViewModel

@Composable
fun StatsScreen(onBackClick: () -> Unit, viewModel: CheckersViewModel = viewModel()) {
    val (gamesPlayed, redWins, blackWins) = viewModel.getStats()
    val winRate = if (gamesPlayed > 0) (redWins.toFloat() / gamesPlayed * 100).toInt() else 0
    AnimatedVisibility(visible = true, enter = fadeIn()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Stats", fontSize = 24.sp, color = Color.Black)
                    Text("Games Played: $gamesPlayed", modifier = Modifier.padding(8.dp), color = Color.Black)
                    Text("Red Wins: $redWins", modifier = Modifier.padding(8.dp), color = Color.Black)
                    Text("Black Wins: $blackWins", modifier = Modifier.padding(8.dp), color = Color.Black)
                    Text("Win Rate (Red): $winRate%", modifier = Modifier.padding(8.dp), color = Color.Black)
                    Button(
                        onClick = onBackClick,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Back", color = Color.White)
                    }
                }
            }
        }
    }
}