package com.zehren.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val game = TicTacToeGame(this)
    private val tttButtons: Array<Array<Button?>> = Array(TicTacToeGame.NUM_ROWS) {arrayOfNulls<Button>(TicTacToeGame.NUM_COLUMNS) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        new_game_button.setOnClickListener {
            game.resetGame()
            updateView()
        }

        for (row in 0 until TicTacToeGame.NUM_ROWS){
            for (col in 0 until TicTacToeGame.NUM_COLUMNS){
                val id = resources.getIdentifier("button$row$col", "id", packageName)
                tttButtons[row][col] = findViewById(id)
                tttButtons[row][col]?.setOnClickListener {
                    game.pressButtonAt(row, col)
                    updateView()
                    Log.d("TTT", "Pressed button at ($row, $col)")
                }
            }
        }

//        button00.setOnClickListener {
//            game.pressButtonAt(0,0)
//            updateView()
//        }
    }

    private fun updateView(){
        game_state_text_view.text = game.stringForGameState()
        for (row in 0 until TicTacToeGame.NUM_ROWS) {
            for (col in 0 until TicTacToeGame.NUM_COLUMNS) {
                tttButtons[row][col]?.text = game.stringForButtonAt(row, col)
            }
        }
    }
}