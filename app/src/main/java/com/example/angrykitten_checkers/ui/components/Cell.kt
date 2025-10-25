package com.example.angrykitten_checkers.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.angrykitten_checkers.model.Piece
import com.example.angrykitten_checkers.model.PieceColor

@Composable
fun Cell(
    piece: Piece?,
    isDarkSquare: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(if (isDarkSquare) Color.DarkGray else Color.LightGray)
            .border(1.dp, if (isSelected) Color.Yellow else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        piece?.let {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = when (it.color) {
                            PieceColor.RED -> Color.Red
                            PieceColor.BLACK -> Color.Black
                        },
                        shape = CircleShape
                    )
            ) {
                if (it.isKing) {
                    Text(
                        text = "K",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}