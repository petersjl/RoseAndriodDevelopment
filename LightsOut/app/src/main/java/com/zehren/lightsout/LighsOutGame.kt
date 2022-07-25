package com.zehren.lightsout

import java.util.*
import kotlin.random.Random.Default.nextInt

class LighsOutGame {
    val numLights = 7
    var lightStates: BooleanArray = BooleanArray(7) {false }
    var gameWon = false
    var numMoves = 0

    init {
        randomize()
    }

    //Randomizes game by pressing a random number of buttons
    //Pressing buttons ensures the game is winnable
    fun randomize(){
        gameWon = false
        var num = Random().nextInt(7) + 3 //Pick a random number of presses
        for (UNUSED in 0..num) {
            if (pressedLightAtIndex(Random().nextInt(7))){ //Press a random button
                gameWon = false
            }
        }

        //If the game randomly wins, randomize again
        if (!lightStates.contains(true)){
            randomize()
        }
        numMoves = 0
    }

    public fun pressedLightAtIndex(index: Int): Boolean{
        //If it's already won do nothing
        if (gameWon) return true

        //Change the specified index
        lightStates[index] = !lightStates[index]

        //If it's not the far left, change the left
        if (index !=0) lightStates[index -1] = !lightStates[index - 1]

        //If it's not the far right, change the right
        if (index < numLights - 1) lightStates[index + 1] = !lightStates[index + 1]

        numMoves++

        //Check to see if the game is won
        if(!lightStates.contains(true)){
            gameWon = true
            return true
        }
        return false
    }

}