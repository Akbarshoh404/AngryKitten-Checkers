package com.example.angrykitten_checkers.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.angrykitten_checkers.model.Piece

@Composable
fun Board(
    board: Array<Array<Piece?>>,
    selectedPiece: Pair<Int, Int>?,
    onCellClick: (Int, Int) -> Unit
) {
    Column {
        for (row in board.indices) {
            Row {
                for (col in board[row].indices) {
                    val isDarkSquare = (row + col) % 2 == 1
                    val isSelected = selectedPiece?.first == row && selectedPiece.second == col
                    Cell(
                        piece = board[row][col],
                        isDarkSquare = isDarkSquare,
                        isSelected = isSelected,
                        onClick = { onCellClick(row, col) }
                    )
                }
            }
        }
    }
}