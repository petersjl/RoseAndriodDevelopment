package com.zehren.tictactoe

import android.content.Context
import android.os.Debug
import java.io.PrintStream

class TicTacToeGame {
    enum class Mark {
        MARK_NONE,
        MARK_X,
        MARK_O
    }
    enum class GameState {
        X_TURN,
        O_TURN,
        X_WIN,
        O_WIN,
        TIE_GAME
    }

    private var board: Array<Array<Mark>> = Array(NUM_ROWS) { Array(NUM_COLUMNS) {Mark.MARK_NONE} }
    private var gameState: GameState = GameState.X_TURN
    private lateinit var context: Context

    companion object {
        val NUM_ROWS = 3
        val NUM_COLUMNS = 3
    }

    constructor(context: Context){
        this.context = context
    }

    constructor()

    init {
        resetGame()
    }

    public fun resetGame(){
        board = Array(NUM_ROWS) { Array(NUM_COLUMNS) {Mark.MARK_NONE} }
        gameState = GameState.X_TURN
    }

    public fun getGameState(): GameState{
        return gameState
    }

    public fun stringForButtonAt(row: Int, column: Int): String{
        if (row !in 0 until NUM_ROWS || column !in 0 until NUM_COLUMNS) return ""
        if (board[row][column] == Mark.MARK_X)return "X"
        if (board[row][column] == Mark.MARK_O) return "O"
        else return ""
    }

    public fun pressButtonAt(row: Int, col: Int){
        if (row !in 0 until NUM_ROWS || col !in 0 until NUM_COLUMNS) return
        if (board[row][col] != Mark.MARK_NONE) return
        if (gameState == GameState.X_TURN){
            board[row][col] = Mark.MARK_X
            gameState = GameState.O_TURN
            checkForWin()
        }else if (gameState == GameState.O_TURN){
            board[row][col] = Mark.MARK_O
            gameState = GameState.X_TURN
            checkForWin()
        }
    }

    public fun printGameString(){
        var str = "Current Game: "
        for (row in 0 until NUM_ROWS){
            for (col in 0 until NUM_COLUMNS){
                str = str + stringForButtonAt(row, col) + ", "
            }
        }
        println(str)
    }

    public fun stringForGameState(): String{
        return when (gameState) {
            GameState.X_TURN -> context.getString(R.string.x_turn)
            GameState.O_TURN -> context.getString(R.string.o_turn)
            GameState.X_WIN -> context.getString(R.string.x_win)
            GameState.O_WIN -> context.getString(R.string.o_win)
            GameState.TIE_GAME -> context.getString(R.string.tie_game)
        }
    }

    fun checkForWin() {
        if (gameState != GameState.X_TURN && gameState != GameState.O_TURN) {
            return
        }

        if (didPieceWin(Mark.MARK_X)) {
            gameState = GameState.X_WIN
        } else if (didPieceWin(Mark.MARK_O)) {
            gameState = GameState.O_WIN
        } else if (isBoardFull()) {
            gameState = GameState.TIE_GAME
        }
    }

    private fun isBoardFull(): Boolean {
        for (row in 0 until 3) {
            for (column in 0 until 3) {
                if (board[row][column] == Mark.MARK_NONE) {
                    return false
                }
            }
        }
        return true
    }

    private fun didPieceWin(mark: Mark): Boolean {
        return didPieceWinAcross(mark) ||
                didPieceWinDown(mark) ||
                didPieceWinMainDiagonal(mark) ||
                didPieceWinOffDiagonal(mark)

    }

    private fun didPieceWinAcross(mark: Mark): Boolean {
        for (row in 0 until 3) {
            var winHere = true
            for (column in 0 until 3) {
                if (board[row][column] != mark) {
                    winHere = false
                }
            }
            if (winHere) {
                return true
            }
        }
        return false
    }

    private fun didPieceWinDown(mark: Mark): Boolean {
        for (column in 0 until 3) {
            var winHere = true
            for (row in 0 until 3) {
                if (board[row][column] != mark) {
                    winHere = false
                }
            }
            if (winHere) {
                return true
            }
        }
        return false
    }

    private fun didPieceWinMainDiagonal(mark: Mark): Boolean {
        var winHere = true
        for (row in 0 until 3) {
            if (board[row][row] != mark) {
                winHere = false
            }
        }
        return winHere

    }

    private fun didPieceWinOffDiagonal(mark: Mark): Boolean {
        var winHere = true
        for (row in 0 until 3) {
            if (board[row][2 - row] != mark) {
                winHere = false
            }
        }
        return winHere
    }


}