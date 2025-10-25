package com.example.angrykitten_checkers.model

enum class PieceColor {
    RED, BLACK
}

data class Piece(
    val color: PieceColor,
    val isKing: Boolean
)