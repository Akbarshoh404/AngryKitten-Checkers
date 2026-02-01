package com.example.angrykitten_checkers.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.dp
import com.example.angrykitten_checkers.model.Piece
import com.example.angrykitten_checkers.model.PieceColor
import com.example.angrykitten_checkers.ui.theme.BoardDark
import com.example.angrykitten_checkers.ui.theme.BoardLight
import com.example.angrykitten_checkers.ui.theme.AccentGold
import com.example.angrykitten_checkers.ui.theme.Highlight
import com.example.angrykitten_checkers.ui.theme.PieceBlack
import com.example.angrykitten_checkers.ui.theme.PieceRed

@Composable
fun Cell(
    piece: Piece?,
    isDarkSquare: Boolean,
    isSelected: Boolean,
    isValidMove: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.1f else 1.0f, label = "pieceScale")
    
    Box(
        modifier = modifier
            .background(if (isDarkSquare) BoardDark else BoardLight)
            .clickable(enabled = true, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Highlight for valid moves
        if (isValidMove) {
             Box(
                 modifier = Modifier
                     .fillMaxSize(0.3f)
                     .background(Highlight, CircleShape)
             )
        }
        
        piece?.let {
            val pieceColor = if (it.color == PieceColor.RED) PieceRed else PieceBlack
            val strokeColor = if (it.color == PieceColor.RED) Color.White.copy(alpha=0.3f) else Color.White.copy(alpha=0.1f)
            
            Box(
                modifier = Modifier
                    .fillMaxSize(0.8f) // Responsive size
                    .scale(scale)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                pieceColor.copy(alpha = 0.8f),
                                pieceColor
                            )
                        ),
                        shape = CircleShape
                    )
                    .border(2.dp, strokeColor, CircleShape)
                    // Inner bevel effect simulation
                    .border(4.dp, Color.Black.copy(alpha = 0.2f), CircleShape) 
            ) {
                if (it.isKing) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "King",
                        tint = AccentGold,
                        modifier = Modifier
                            .fillMaxSize(0.6f)
                            .align(Alignment.Center)
                    )
                }
            }
            
            if (isSelected) {
                Box(
                    modifier = Modifier.fillMaxSize(0.9f).border(3.dp, AccentGold, CircleShape)
                )
            }
        }
    }
}