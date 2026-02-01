package com.example.angrykitten_checkers.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.angrykitten_checkers.model.Piece
import com.example.angrykitten_checkers.ui.theme.BoardDark

@Composable
fun Board(
    board: List<List<Piece?>>,
    selectedPiece: Pair<Int, Int>?,
    validMoves: List<Pair<Int, Int>>,
    onCellClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(8.dp))
            .background(Color(0xFF3E2723), shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            for (row in board.indices) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    for (col in board[row].indices) {
                        val piece = board[row][col]
                        // Crucial: Key by content to prevent ghosting artifacts
                        key(row, col, piece) {
                            val isDarkSquare = (row + col) % 2 == 1
                            val isSelected = selectedPiece?.first == row && selectedPiece.second == col
                            val isValidMove = validMoves.contains(row to col)
                            Cell(
                                piece = piece,
                                isDarkSquare = isDarkSquare,
                                isSelected = isSelected,
                                isValidMove = isValidMove,
                                onClick = { onCellClick(row, col) },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                        }
                    }
                }
            }
        }
    }
}