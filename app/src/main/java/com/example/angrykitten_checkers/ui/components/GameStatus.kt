package com.example.angrykitten_checkers.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameStatus(status: String, onResetClick: () -> Unit, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(text = status)
        Button(
            onClick = onResetClick,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Reset Game")
        }
        Button(
            onClick = onBackClick,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Back to Menu")
        }
    }
}