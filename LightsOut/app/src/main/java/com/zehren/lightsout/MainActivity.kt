package com.zehren.lightsout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val game = LighsOutGame()
    var buttons: Array<Button?> = arrayOfNulls<Button>(game.numLights)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for(i in 0 until game.numLights){
            val id = resources.getIdentifier("button$i", "id", packageName)
            Log.d("MSG", "Setting button $i")
            buttons[i] = findViewById(id)
            buttons[i]?.setOnClickListener {
                game.pressedLightAtIndex(i)
                updateView()
            }
        }

        if (savedInstanceState != null){
            game.lightStates = savedInstanceState.get("states") as BooleanArray
            game.gameWon = savedInstanceState.get("gameState") as Boolean
            game.numMoves = savedInstanceState.get("numMoves") as Int
        }

        new_game_button.setOnClickListener {
            for (button in buttons){
                button?.isEnabled = true
            }
            game.randomize()
            updateView()
        }

        updateView()

    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBooleanArray("states", game.lightStates)
        outState.putBoolean("gameState", game.gameWon)
        outState.putInt("numMoves", game.numMoves)
    }

    private fun updateView(){
        for (i in 0 until game.numLights){
            buttons[i]?.text = if (game.lightStates[i]) "1" else "0"
        }
        if (game.gameWon) {
            for(button in buttons){
                button?.isEnabled = false
            }
            game_state_text_view.text = getString(R.string.win_message)
            return
        }
        if (game.numMoves == 0) game_state_text_view.text = getString(R.string.starting_string)
        else game_state_text_view.text = resources.getQuantityString(R.plurals.message_plurals, game.numMoves, game.numMoves)

    }
}