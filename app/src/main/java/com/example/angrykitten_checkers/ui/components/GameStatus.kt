package com.example.angrykitten_checkers.ui.components

import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column

@Composable
fun GameStatus(status: String, onResetClick: () -> Unit, onBackClick: () -> Unit) {
    AnimatedVisibility(visible = true, enter = scaleIn()) {
        Column(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = status, color = Color.Black)
            Button(
                onClick = onResetClick,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Reset Game", color = Color.White)
            }
            Button(
                onClick = onBackClick,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Back to Menu", color = Color.White)
            }
        }
    }
}