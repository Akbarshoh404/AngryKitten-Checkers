package com.example.angrykitten_checkers.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.angrykitten_checkers.data.CheckersAI
import com.example.angrykitten_checkers.data.CheckersGame
import com.example.angrykitten_checkers.model.Difficulty
import com.example.angrykitten_checkers.model.GameMode
import com.example.angrykitten_checkers.model.PieceColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CheckersViewModel(application: Application) : AndroidViewModel(application) {
    private val _gameState = MutableStateFlow(CheckersGame())
    val gameState: StateFlow<CheckersGame> = _gameState
    
    // Game Settings
    private val _difficulty = MutableStateFlow(Difficulty.MEDIUM)
    val difficulty: StateFlow<Difficulty> = _difficulty

    private val _gameMode = MutableStateFlow(GameMode.PvP)
    val gameMode: StateFlow<GameMode> = _gameMode
    
    // Stats
    private val sharedPrefs = application.getSharedPreferences("checkers_stats", Context.MODE_PRIVATE)
    private var gamesPlayed = 0
    private var redWins = 0
    private var blackWins = 0

    // Prevent interactions while AI is thinking
    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking

    // Feedback for mandatory captures
    private val _mustCapture = MutableStateFlow(false)
    val mustCapture: StateFlow<Boolean> = _mustCapture

    init {
        loadStats()
    }

    private fun loadStats() {
        gamesPlayed = sharedPrefs.getInt("games_played", 0)
        redWins = sharedPrefs.getInt("red_wins", 0)
        blackWins = sharedPrefs.getInt("black_wins", 0)
    }

    private fun saveStats() {
        with(sharedPrefs.edit()) {
            putInt("games_played", gamesPlayed)
            putInt("red_wins", redWins)
            putInt("black_wins", blackWins)
            apply()
        }
    }

    fun setDifficulty(level: Difficulty) {
        _difficulty.value = level
        resetGame()
    }

    fun setGameMode(mode: GameMode) {
        _gameMode.value = mode
        resetGame()
    }

    fun selectPiece(row: Int, col: Int) {
        if (_isAiThinking.value) return 
        val game = _gameState.value
        if (game.gameOver) return
        
        // Fix: In PvP, Black (Player 2) must be allowed to select.
        // Only block Black if it's strictly AI's turn in PvAI mode.
        if (_gameMode.value == GameMode.PvAI && game.currentPlayer == PieceColor.BLACK) return 

        // Update mandatory capture status for UI feedback
        // We check if ANY capture is available for the current player
        val hasMandatory = game.hasMandatoryCaptures(game.currentPlayer)
        _mustCapture.value = hasMandatory

        if (game.selectedPiece == Pair(row, col)) {
            _gameState.value = game.copy(selectedPiece = null)
            return
        }
        
        if (game.board[row][col]?.color == game.currentPlayer) {
            val validMoves = game.getValidMoves(row, col)
            if (validMoves.isNotEmpty()) {
                _gameState.value = game.copy(selectedPiece = Pair(row, col))
                _mustCapture.value = false // Selected a valid moving piece
            } else if (hasMandatory) {
                // Determine if this specific piece has BUT implies others might
                // Actually `getValidMoves` returns empty if this piece can't capture but others can.
                // So if validMoves is empty BUT hasMandatory is true, it means "You must pick a capturing piece"
                _mustCapture.value = true
                _gameState.value = game.copy(selectedPiece = null) // Do not select invalid piece
            } else {
                 // No moves, no mandatory captures. Just can't move.
                 // Maybe select it anyway to show 0 moves? Standard behavior is usually yes.
                 // But for checkers, often we only select movable pieces.
                 // I'll allow selection but it will have no dots.
                 _gameState.value = game.copy(selectedPiece = Pair(row, col))
            }
        }
    }

    fun movePiece(toRow: Int, toCol: Int) {
        if (_isAiThinking.value) return
        val game = _gameState.value
        game.selectedPiece?.let { from ->
             // NEW API: makeMove returns new state or null
             val newGame = game.makeMove(from, Pair(toRow, toCol))
             
             if (newGame != null) {
                 _gameState.value = newGame
                 checkStats(newGame)
                 _mustCapture.value = false // Reset warning on successful move
                 
                 // Trigger AI ONLY if it's PvAI mode and Black's turn
                 if (_gameMode.value == GameMode.PvAI && !newGame.gameOver && newGame.currentPlayer == PieceColor.BLACK) {
                     makeAIMove()
                 }
             } else {
                 _gameState.value = game.copy(selectedPiece = null)
             }
        }
    }

    private fun makeAIMove() {
        _isAiThinking.value = true
        viewModelScope.launch {
            delay(500)
            
            val game = _gameState.value
            val bestMove = CheckersAI.findBestMove(game, _difficulty.value.depth)
            
            if (bestMove != null) {
                val newGame = game.makeMove(bestMove.first, bestMove.second)
                
                if (newGame != null) {
                    _gameState.value = newGame
                    checkStats(newGame)
                    
                    if (newGame.currentPlayer == PieceColor.BLACK && !newGame.gameOver) {
                        makeAIMove() 
                    } else {
                         _isAiThinking.value = false
                    }
                } else {
                    _isAiThinking.value = false
                }
            } else {
                _isAiThinking.value = false
            }
        }
    }

    private fun checkStats(game: CheckersGame) {
        if (game.gameOver) {
             gamesPlayed++
             if (game.winner == PieceColor.RED) redWins++
             else if (game.winner == PieceColor.BLACK) blackWins++
             saveStats()
             _isAiThinking.value = false
        }
    }

    fun resetGame() {
        _gameState.value = CheckersGame()
        _isAiThinking.value = false
    }

    fun getStats(): Triple<Int, Int, Int> {
        return Triple(gamesPlayed, redWins, blackWins)
    }
}