package com.example.angrykitten_checkers.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.angrykitten_checkers.data.CheckersGame
import com.example.angrykitten_checkers.model.PieceColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class CheckersViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(CheckersGame())
    val gameState: StateFlow<CheckersGame> = _gameState
    private var gamesPlayed = 0
    private var redWins = 0
    private var blackWins = 0

    fun selectPiece(row: Int, col: Int) {
        val game = _gameState.value
        if (game.board[row][col]?.color == game.currentPlayer) {
            _gameState.value = game.copy(selectedPiece = Pair(row, col))
        }
    }

    fun movePiece(toRow: Int, toCol: Int) {
        val game = _gameState.value
        game.selectedPiece?.let { from ->
            if (game.movePiece(from, Pair(toRow, toCol))) {
                _gameState.value = game.copy()
                if (game.gameOver) {
                    gamesPlayed++
                    if (game.winner == PieceColor.RED) redWins++
                    else if (game.winner == PieceColor.BLACK) blackWins++
                }
            }
        }
    }

    fun makeAIMove() {
        val game = _gameState.value
        if (game.currentPlayer == PieceColor.BLACK && !game.gameOver) {
            val validMoves = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
            for (row in 0..7) {
                for (col in 0..7) {
                    if (game.board[row][col]?.color == PieceColor.BLACK) {
                        listOf(
                            Pair(row - 1, col - 1),
                            Pair(row - 1, col + 1),
                            Pair(row + 1, col - 1),
                            Pair(row + 1, col + 1)
                        ).forEach { to ->
                            if (to.first in 0..7 && to.second in 0..7 && game.movePiece(Pair(row, col), to)) {
                                validMoves.add(Pair(Pair(row, col), to))
                                _gameState.value = game.copy(board = game.board.map { it.clone() }.toTypedArray())
                            }
                        }
                    }
                }
            }
            if (validMoves.isNotEmpty()) {
                val move = validMoves[Random.nextInt(validMoves.size)]
                movePiece(move.second.first, move.second.second)
            }
        }
    }

    fun resetGame() {
        _gameState.value = CheckersGame()
    }

    fun getStats(): Triple<Int, Int, Int> {
        return Triple(gamesPlayed, redWins, blackWins)
    }
}