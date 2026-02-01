package com.example.angrykitten_checkers.data

import com.example.angrykitten_checkers.model.PieceColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CheckersAI {

    suspend fun findBestMove(
        game: CheckersGame,
        depth: Int
    ): Pair<Pair<Int, Int>, Pair<Int, Int>>? = withContext(Dispatchers.Default) {
        val moves = game.getAllValidMoves()
        
        if (moves.isEmpty()) return@withContext null
        if (moves.size == 1) return@withContext moves[0]

        var bestMove: Pair<Pair<Int, Int>, Pair<Int, Int>>? = null
        var bestValue = Int.MIN_VALUE

        val maximizingPlayer = game.currentPlayer 

        for (move in moves) {
            val simulatedGame = game.makeMove(move.first, move.second) ?: continue
            val value = minimax(simulatedGame, depth - 1, Int.MIN_VALUE, Int.MAX_VALUE, false, maximizingPlayer)
            
            if (value > bestValue) {
                bestValue = value
                bestMove = move
            }
        }
        
        if (bestMove == null && moves.isNotEmpty()) {
            bestMove = moves[0]
        }
        bestMove
    }

    private fun minimax(
        game: CheckersGame,
        depth: Int,
        alpha: Int,
        beta: Int,
        isMaximizing: Boolean,
        aiColor: PieceColor
    ): Int {
        if (depth == 0 || game.gameOver) {
            return evaluate(game, aiColor)
        }

        val validMoves = game.getAllValidMoves()
        if (validMoves.isEmpty()) {
             return evaluate(game, aiColor)
        }

        var currentAlpha = alpha
        var currentBeta = beta

        if (isMaximizing) {
            var maxEval = Int.MIN_VALUE
            for (move in validMoves) {
                val simGame = game.makeMove(move.first, move.second) ?: continue
                val turnChanged = simGame.currentPlayer != game.currentPlayer
                
                val eval = minimax(simGame, if (turnChanged) depth - 1 else depth, currentAlpha, currentBeta, !turnChanged, aiColor)
                maxEval = maxOf(maxEval, eval)
                currentAlpha = maxOf(currentAlpha, eval)
                if (currentBeta <= currentAlpha) break
            }
            return maxEval
        } else {
            var minEval = Int.MAX_VALUE
            for (move in validMoves) {
                val simGame = game.makeMove(move.first, move.second) ?: continue
                val turnChanged = simGame.currentPlayer != game.currentPlayer
                
                val eval = minimax(simGame, if (turnChanged) depth - 1 else depth, currentAlpha, currentBeta, turnChanged, aiColor)
                minEval = minOf(minEval, eval)
                currentBeta = minOf(currentBeta, eval)
                if (currentBeta <= currentAlpha) break
            }
            return minEval
        }
    }

    private fun evaluate(game: CheckersGame, aiColor: PieceColor): Int {
        if (game.gameOver) {
            return if (game.winner == aiColor) 10000 else -10000
        }

        var score = 0
        for (row in 0..7) {
            for (col in 0..7) {
                val piece = game.board[row][col] ?: continue
                val value = if (piece.isKing) 5 else 1
                
                if (piece.color == aiColor) {
                    score += value
                    if (col in 2..5 && row in 2..5) score += 1
                } else {
                    score -= value
                    if (col in 2..5 && row in 2..5) score -= 1
                }
            }
        }
        return score
    }
}
