package com.example.angrykitten_checkers.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainMenuScreen(
    onTwoPlayersClick: () -> Unit,
    onVsAIClick: () -> Unit,
    onStatsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Checkers Master", fontSize = 32.sp)
        Button(
            onClick = onTwoPlayersClick,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("2 Players")
        }
        Button(
            onClick = onVsAIClick,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Vs AI")
        }
        Button(
            onClick = onStatsClick,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Stats")
        }
    }
}