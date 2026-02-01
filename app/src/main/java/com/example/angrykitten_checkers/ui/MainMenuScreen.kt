package com.example.angrykitten_checkers.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.angrykitten_checkers.ui.theme.Charcoal
import com.example.angrykitten_checkers.ui.theme.Gunmetal
import com.example.angrykitten_checkers.ui.theme.NeonRed
import com.example.angrykitten_checkers.ui.theme.SoftCream

@Composable
fun MainMenuScreen(
    onTwoPlayersClick: () -> Unit,
    onVsAIClick: () -> Unit,
    onStatsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Gunmetal, Charcoal)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(1000)) + scaleIn()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ANGRY KITTEN",
                        style = MaterialTheme.typography.titleLarge,
                        color = NeonRed,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = MaterialTheme.typography.titleLarge.letterSpacing
                    )
                    Text(
                        text = "CHECKERS",
                        style = MaterialTheme.typography.displayLarge,
                        color = SoftCream,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(0.8f) // Constrain width for cleaner look on tablets
            ) {
                MenuButton(text = "Player vs AI", onClick = onVsAIClick, delay = 100)
                MenuButton(text = "Two Players", onClick = onTwoPlayersClick, delay = 200)
                MenuButton(text = "Statistics", onClick = onStatsClick, delay = 300)
                // Add Settings here if requested
            }
        }
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit, delay: Int) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(durationMillis = 500, delayMillis = delay)) + 
                scaleIn(animationSpec = tween(durationMillis = 500, delayMillis = delay))
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}