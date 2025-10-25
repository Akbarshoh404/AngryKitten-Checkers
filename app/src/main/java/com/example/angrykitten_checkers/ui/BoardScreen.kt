package com.example.angrykitten_checkers.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
    if (isVsAI && gameState.value.currentPlayer == PieceColor.BLACK && !gameState.value.gameOver) {
        LaunchedEffect(gameState.value.currentPlayer) {
            viewModel.makeAIMove()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Board(
            board = gameState.value.board,
            selectedPiece = gameState.value.selectedPiece,
            onCellClick = { row, col ->
                if (gameState.value.selectedPiece == null) {
                    viewModel.selectPiece(row, col)
                } else {
                    viewModel.movePiece(row, col)
                }
            }
        )
        GameStatus(
            status = gameState.value.gameStatus,
            onResetClick = { viewModel.resetGame() },
            onBackClick = onBackClick
        )
    }
}