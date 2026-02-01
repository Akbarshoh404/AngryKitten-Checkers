package com.example.angrykitten_checkers.data

import com.example.angrykitten_checkers.model.Piece
import com.example.angrykitten_checkers.model.PieceColor
import kotlin.math.abs

data class CheckersGame(
    val board: List<List<Piece?>> = createInitialBoard(),
    val currentPlayer: PieceColor = PieceColor.RED,
    val selectedPiece: Pair<Int, Int>? = null,
    val gameStatus: String = "White's Turn",
    val gameOver: Boolean = false,
    val winner: PieceColor? = null
) {
    companion object {
        fun createInitialBoard(): List<List<Piece?>> {
            return List(8) { row ->
                List(8) { col ->
                    when {
                        (row + col) % 2 == 1 && row < 3 -> Piece(PieceColor.BLACK, false)
                        (row + col) % 2 == 1 && row > 4 -> Piece(PieceColor.RED, false)
                        else -> null
                    }
                }
            }
        }
    }

    // Pure function: Returns a NEW Game state with the move applied
    fun makeMove(from: Pair<Int, Int>, to: Pair<Int, Int>): CheckersGame? {
        val (fromRow, fromCol) = from
        val (toRow, toCol) = to
        
        // Validation (basic)
        if (!isValidMove(from, to)) return null
        
        val movingPiece = board[fromRow][fromCol] ?: return null
        
        // 1. Create modified board
        val newBoard = board.map { it.toMutableList() }.toMutableList()
        
        // Move logic
        newBoard[toRow][toCol] = movingPiece
        newBoard[fromRow][fromCol] = null // Crucial: This explicitly clears the old spot
        
        val rowDiff = toRow - fromRow
        val colDiff = toCol - fromCol
        var captureOccurred = false
        
        // Capture logic
        if (abs(rowDiff) == 2) {
            val capturedRow = fromRow + rowDiff / 2
            val capturedCol = fromCol + colDiff / 2
            newBoard[capturedRow][capturedCol] = null
            captureOccurred = true
        }

        // Promotion logic
        if ((movingPiece.color == PieceColor.RED && toRow == 0) || 
            (movingPiece.color == PieceColor.BLACK && toRow == 7)) {
            newBoard[toRow][toCol] = movingPiece.copy(isKing = true)
        }

        val immutableBoard = newBoard.map { it.toList() } // Seal it back to immutable

        // 2. Determine next state
        var nextPlayer = currentPlayer
        var nextSelectedPiece: Pair<Int, Int>? = null
        var nextStatus = gameStatus

        // Double jump logic
        val canCaptureAgain = if (captureOccurred) {
             // Check if the piece at new position can capture further
             canCaptureFrom(immutableBoard, toRow, toCol, movingPiece.isKing || (newBoard[toRow][toCol]?.isKing == true), currentPlayer)
        } else false

        if (canCaptureAgain) {
             nextSelectedPiece = Pair(toRow, toCol)
             nextStatus = "${if (currentPlayer == PieceColor.RED) "Red" else "Black"} Must Jump Again"
        } else {
             nextPlayer = if (currentPlayer == PieceColor.RED) PieceColor.BLACK else PieceColor.RED
             nextStatus = "${if (nextPlayer == PieceColor.RED) "Red" else "Black"}'s Turn"
        }

        val nextGame = this.copy(
            board = immutableBoard,
            currentPlayer = nextPlayer,
            selectedPiece = nextSelectedPiece,
            gameStatus = nextStatus
        )
        
        return nextGame.checkGameOver()
    }
    
    // Legacy helper to keep View Model somewhat similar, but now returns Game
    // Wait, the VM called `movePiece(from, to)` which returned Boolean.
    // I should adapt to that OR allow VM to be refactored.
    // I will refactor VM to use `makeMove` which returns nullable CheckersGame.

    private fun isValidMove(from: Pair<Int, Int>, to: Pair<Int, Int>): Boolean {
        val validMoves = getValidMoves(from.first, from.second)
        return to in validMoves
    }

    fun getValidMoves(row: Int, col: Int): List<Pair<Int, Int>> {
        val piece = board[row][col] ?: return emptyList()
        if (piece.color != currentPlayer) return emptyList()

        val captures = getCaptureMoves(board, row, col, piece.isKing, piece.color, opponentColor())
        
        // Mandatory capture rule at global level
        // Does ANY piece have a capture?
        val anyCapture = hasMandatoryCaptures(currentPlayer)
        
        if (anyCapture) {
             return captures // If mandatory captures exist globally, only captures are valid for this piece
             // Note: If 'captures' is empty but 'anyCapture' is true, this piece has 0 moves.
        }
        
        val nonCaptures = getNonCaptureMoves(board, row, col, piece.isKing, piece.color)
        return captures + nonCaptures
    }

    // Static-like helpers that take board state explicitly for reusability during simulation
    private fun getCaptureMoves(currentBoard: List<List<Piece?>>, row: Int, col: Int, isKing: Boolean, color: PieceColor, oppColor: PieceColor): List<Pair<Int, Int>> {
        val directions = if (isKing) {
            listOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1)
        } else if (color == PieceColor.RED) {
            listOf(-1 to -1, -1 to 1) 
        } else {
            listOf(1 to -1, 1 to 1) 
        }

        val captures = mutableListOf<Pair<Int, Int>>()
        for ((dRow, dCol) in directions) {
            val cRow = row + 2 * dRow
            val cCol = col + 2 * dCol
            val midRow = row + dRow
            val midCol = col + dCol

            if (cRow in 0..7 && cCol in 0..7) {
                val midPiece = currentBoard[midRow][midCol]
                val destPiece = currentBoard[cRow][cCol]
                
                if (midPiece != null && midPiece.color == oppColor && destPiece == null) {
                    captures.add(cRow to cCol)
                }
            }
        }
        return captures
    }

    private fun getNonCaptureMoves(currentBoard: List<List<Piece?>>, row: Int, col: Int, isKing: Boolean, color: PieceColor): List<Pair<Int, Int>> {
        val directions = if (isKing) {
            listOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1)
        } else if (color == PieceColor.RED) {
            listOf(-1 to -1, -1 to 1) 
        } else {
            listOf(1 to -1, 1 to 1) 
        }

        val moves = mutableListOf<Pair<Int, Int>>()
        for ((dRow, dCol) in directions) {
            val nRow = row + dRow
            val nCol = col + dCol
            if (nRow in 0..7 && nCol in 0..7 && currentBoard[nRow][nCol] == null) {
                moves.add(nRow to nCol)
            }
        }
        return moves
    }
    
    private fun canCaptureFrom(boardState: List<List<Piece?>>, row: Int, col: Int, isKing: Boolean, color: PieceColor): Boolean {
        return getCaptureMoves(boardState, row, col, isKing, color, if(color == PieceColor.RED) PieceColor.BLACK else PieceColor.RED).isNotEmpty()
    }

    fun hasMandatoryCaptures(playerColor: PieceColor): Boolean {
        for (r in 0..7) {
            for (c in 0..7) {
                val p = board[r][c]
                if (p != null && p.color == playerColor) {
                    val oppColor = if (playerColor == PieceColor.RED) PieceColor.BLACK else PieceColor.RED
                    if (getCaptureMoves(board, r, c, p.isKing, playerColor, oppColor).isNotEmpty()) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun opponentColor(): PieceColor {
        return if (currentPlayer == PieceColor.RED) PieceColor.BLACK else PieceColor.RED
    }

    private fun checkGameOver(): CheckersGame {
        val redPieces = board.flatten().count { it?.color == PieceColor.RED }
        val blackPieces = board.flatten().count { it?.color == PieceColor.BLACK }
        
        // This 'getAllValidMoves' check is expensive, maybe optimize?
        // For MVP, checking counts is fast. Checking moves is needed for "No Moves" win condition.
        val hasMoves = getAllValidMoves().isNotEmpty()
        
        if (redPieces == 0 || (currentPlayer == PieceColor.RED && !hasMoves)) {
            return this.copy(gameOver = true, winner = PieceColor.BLACK, gameStatus = "Black Wins!")
        } else if (blackPieces == 0 || (currentPlayer == PieceColor.BLACK && !hasMoves)) {
            return this.copy(gameOver = true, winner = PieceColor.RED, gameStatus = "Red Wins!")
        }
        return this
    }

    fun getAllValidMoves(): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        val allMoves = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
        for (r in 0..7) {
            for (c in 0..7) {
                if (board[r][c]?.color == currentPlayer) {
                    getValidMoves(r, c).forEach { to ->
                        allMoves.add((r to c) to to)
                    }
                }
            }
        }
        return allMoves
    }
}