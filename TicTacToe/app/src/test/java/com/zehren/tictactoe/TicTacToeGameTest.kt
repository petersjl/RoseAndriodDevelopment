package com.zehren.tictactoe

import org.junit.Test

import org.junit.Assert.*

/**
 * Unit test for TicTacToeGame, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TicTacToeGameTest {
    @Test
    fun boardIsReset() {
        val game = TicTacToeGame()
        for (row in 0 until TicTacToeGame.NUM_ROWS){
            for (col in 0 until TicTacToeGame.NUM_COLUMNS){
                assertEquals("-", game.stringForButtonAt(row, col))
            }
        }
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
    }


    @Test
    fun pressRegisters(){
        val game = TicTacToeGame()
        game.pressButtonAt(1, 2)
        println("Pressed 1, 2")
        game.printGameString()
        assertEquals("X", game.stringForButtonAt(1, 2))
        assertEquals(TicTacToeGame.GameState.O_TURN, game.getGameState())
        game.pressButtonAt(2, 2)
        println("Pressed 2, 2")
        game.printGameString()
        assertEquals("O", game.stringForButtonAt(2, 2))
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
    }

    @Test
    fun detectWinEasy() {
        val game = TicTacToeGame()
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
        game.pressButtonAt(0, 0) // X
        assertEquals(TicTacToeGame.GameState.O_TURN, game.getGameState())
        game.pressButtonAt(2, 0) // O
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
        game.pressButtonAt(0, 1) // X
        assertEquals(TicTacToeGame.GameState.O_TURN, game.getGameState())
        game.pressButtonAt(2, 2) // O
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
        game.pressButtonAt(0, 2) // X
        assertEquals(TicTacToeGame.GameState.X_WIN, game.getGameState())
    }

    @Test
    fun detectWinForce() {
        val game = TicTacToeGame()
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
        game.pressButtonAt(1, 1)
        assertEquals(TicTacToeGame.GameState.O_TURN, game.getGameState())
        game.pressButtonAt(0, 1)
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
        game.pressButtonAt(0, 0)
        assertEquals(TicTacToeGame.GameState.O_TURN, game.getGameState())
        game.pressButtonAt(2, 2)
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
        game.pressButtonAt(2, 0)
        assertEquals(TicTacToeGame.GameState.O_TURN, game.getGameState())
        game.pressButtonAt(0, 2)
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
        game.pressButtonAt(1, 0)
        assertEquals(TicTacToeGame.GameState.X_WIN, game.getGameState())
    }

    @Test
    fun detectTie() {
        val game = TicTacToeGame()
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
        game.pressButtonAt(1, 1)
        assertEquals(TicTacToeGame.GameState.O_TURN, game.getGameState())
        game.pressButtonAt(0, 1)
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
        game.pressButtonAt(0, 0)
        assertEquals(TicTacToeGame.GameState.O_TURN, game.getGameState())
        game.pressButtonAt(2, 2)
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
        game.pressButtonAt(2, 0)
        assertEquals(TicTacToeGame.GameState.O_TURN, game.getGameState())
        game.pressButtonAt(0, 2)
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
        game.pressButtonAt(1, 2)
        assertEquals(TicTacToeGame.GameState.O_TURN, game.getGameState())
        game.pressButtonAt(1, 0)
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
        game.pressButtonAt(2, 1)
        assertEquals(TicTacToeGame.GameState.TIE_GAME, game.getGameState())
    }

    @Test
    fun detectXWinsBottomLeftToUpperRightDiagonal() {
        val game = TicTacToeGame()
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
        game.pressButtonAt(0, 2)
        assertEquals(TicTacToeGame.GameState.O_TURN, game.getGameState())
        game.pressButtonAt(0, 1)
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
        game.pressButtonAt(1, 1)
        assertEquals(TicTacToeGame.GameState.O_TURN, game.getGameState())
        game.pressButtonAt(2, 2)
        assertEquals(TicTacToeGame.GameState.X_TURN, game.getGameState())
        game.pressButtonAt(2, 0)
        assertEquals(TicTacToeGame.GameState.X_WIN, game.getGameState())
    }

}