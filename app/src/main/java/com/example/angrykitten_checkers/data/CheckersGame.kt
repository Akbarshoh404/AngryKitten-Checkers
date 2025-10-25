package com.example.angrykitten_checkers.data

import com.example.angrykitten_checkers.model.Piece
import com.example.angrykitten_checkers.model.PieceColor

data class CheckersGame(
    val board: Array<Array<Piece?>> = Array(8) { Array(8) { null } },
    var currentPlayer: PieceColor = PieceColor.RED,
    var selectedPiece: Pair<Int, Int>? = null,
    var gameStatus: String = "Red's Turn",
    var gameOver: Boolean = false,
    var winner: PieceColor? = null
) {
    init {
        for (row in 0..2) {
            for (col in 0..7) {
                if ((row + col) % 2 == 1) {
                    board[row][col] = Piece(PieceColor.BLACK, false)
                }
            }
        }
        for (row in 5..7) {
            for (col in 0..7) {
                if ((row + col) % 2 == 1) {
                    board[row][col] = Piece(PieceColor.RED, false)
                }
            }
        }
    }

    fun movePiece(from: Pair<Int, Int>, to: Pair<Int, Int>): Boolean {
        val (fromRow, fromCol) = from
        val (toRow, toCol) = to
        val piece = board[fromRow][fromCol] ?: return false

        if (isValidMove(from, to, piece)) {
            board[toRow][toCol] = piece
            board[fromRow][fromCol] = null
            if (toRow == 0 && piece.color == PieceColor.RED || toRow == 7 && piece.color == PieceColor.BLACK) {
                board[toRow][toCol] = piece.copy(isKing = true)
            }
            currentPlayer = if (currentPlayer == PieceColor.RED) PieceColor.BLACK else PieceColor.RED
            gameStatus = if (gameOver) "${winner?.name} Wins!" else "${currentPlayer.name}'s Turn"
            checkGameOver()
            return true
        }
        return false
    }

    private fun isValidMove(from: Pair<Int, Int>, to: Pair<Int, Int>, piece: Piece): Boolean {
        val (fromRow, fromCol) = from
        val (toRow, toCol) = to
        if (board[toRow][toCol] != null) return false
        val rowDiff = toRow - fromRow
        val colDiff = toCol - fromCol
        val isDiagonal = kotlin.math.abs(rowDiff) == kotlin.math.abs(colDiff)
        if (!isDiagonal || kotlin.math.abs(rowDiff) != 1) return false
        return if (piece.isKing) {
            true
        } else {
            when (piece.color) {
                PieceColor.RED -> rowDiff > 0
                PieceColor.BLACK -> rowDiff < 0
            }
        }
    }

    private fun checkGameOver() {
        val redPieces = board.flatten().count { it?.color == PieceColor.RED }
        val blackPieces = board.flatten().count { it?.color == PieceColor.BLACK }
        if (redPieces == 0) {
            gameOver = true
            winner = PieceColor.BLACK
            gameStatus = "Black Wins!"
        } else if (blackPieces == 0) {
            gameOver = true
            winner = PieceColor.RED
            gameStatus = "Red Wins!"
        }
    }
}