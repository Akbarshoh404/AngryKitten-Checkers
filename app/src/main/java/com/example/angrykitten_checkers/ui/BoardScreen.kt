package com.example.angrykitten_checkers.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.angrykitten_checkers.model.GameMode
import com.example.angrykitten_checkers.model.PieceColor
import com.example.angrykitten_checkers.ui.components.Board
import com.example.angrykitten_checkers.ui.components.GameStatus
import com.example.angrykitten_checkers.viewmodel.CheckersViewModel

@Composable
fun BoardScreen(
    isVsAI: Boolean,
    onBackClick: () -> Unit,
    viewModel: CheckersViewModel = viewModel()
) {
    val gameState = viewModel.gameState.collectAsState()
    val isAiThinking = viewModel.isAiThinking.collectAsState()
    val mustCapture = viewModel.mustCapture.collectAsState()
    val game = gameState.value

    // Initialize Game Mode Logic
    LaunchedEffect(isVsAI) {
        val mode = if (isVsAI) GameMode.PvAI else GameMode.PvP
        viewModel.setGameMode(mode)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (game.gameOver) "GAME OVER" else game.gameStatus,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* Open Settings */ }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onBackground)
                }
            }

            // Central Content Area (Board)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Turn / AI Indicator above board
                     if (isAiThinking.value) {
                         Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
                             CircularProgressIndicator(
                                 modifier = Modifier.height(20.dp).width(20.dp),
                                 strokeWidth = 2.dp,
                                 color = MaterialTheme.colorScheme.primary
                             )
                             Spacer(modifier = Modifier.width(8.dp))
                             Text("AI Thinking...", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
                         }
                     } else {
                         // Warning for Mandatory Capture
                         AnimatedVisibility(visible = mustCapture.value) {
                             Text(
                                 text = "Capture Required!",
                                 style = MaterialTheme.typography.titleMedium,
                                 color = MaterialTheme.colorScheme.error,
                                 fontWeight = FontWeight.Bold,
                                 modifier = Modifier.padding(bottom = 8.dp)
                             )
                         }
                         
                         if (!game.gameOver) {
                             Text(
                                 text = if (game.currentPlayer == PieceColor.RED) "Your Turn (Red)" else "Opponent Turn (Black)",
                                 style = MaterialTheme.typography.headlineMedium,
                                 fontWeight = FontWeight.Bold,
                                 color = if (game.currentPlayer == PieceColor.RED) MaterialTheme.colorScheme.primary else Color.Gray,
                                 modifier = Modifier.padding(bottom = 16.dp)
                             )
                         }
                     }

                    // The Board taking Max Width
                    Board(
                        board = game.board,
                        selectedPiece = game.selectedPiece,
                        validMoves = if (game.selectedPiece != null) game.getValidMoves(game.selectedPiece!!.first, game.selectedPiece!!.second) else emptyList(),
                        onCellClick = { row, col ->
                            if (!isAiThinking.value) {
                                if (game.selectedPiece == null) {
                                    viewModel.selectPiece(row, col)
                                } else {
                                    viewModel.movePiece(row, col)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                }
            }
            // Bottom Area (preserved)

            
            // Bottom Area
            if (game.gameOver) {
                GameStatus(
                    status = game.gameStatus,
                    onResetClick = { viewModel.resetGame() },
                    onBackClick = onBackClick
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                     IconButton(onClick = { viewModel.resetGame() }) {
                         Icon(Icons.Default.Refresh, contentDescription = "Restart", tint = MaterialTheme.colorScheme.onBackground)
                     }
                }
            }
        }
    }
}