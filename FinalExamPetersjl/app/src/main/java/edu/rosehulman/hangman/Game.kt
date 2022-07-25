package edu.rosehulman.hangman

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

/**
 * Matt Boutell
 * Rose-Hulman Institute of Technology.
 * Covered by MIT license.
 */
class Game(
    val creator: String = "",
    val secretWord: String = "",
    var displayWord: String = "",
    var guesses: String = "",
    var lastGuesser: String = ""
) {

    @get:Exclude
    var id: String = ""

    private val iconIds = intArrayOf(
        R.drawable.hangman0,
        R.drawable.hangman1,
        R.drawable.hangman2,
        R.drawable.hangman3,
        R.drawable.hangman4,
        R.drawable.hangman5,
        R.drawable.hangman6
    )

    /**
     * Shows guessed characters from the secret word, while
     * leaving un-guessed characters as asterisks (*)
     * If guesses is empty, will just generate a string of
     * asterisks of the same length as the secret word.
     *  Mutates the field and returns the updated value.
     */
    fun buildDisplayWord(): String {
        displayWord =
            secretWord.toList()
                .map { if (it in guesses) it else "*" }
                .joinToString(separator = "")
        return displayWord
    }

    /**
     * Updates the guesses by sorting and removing duplicates. Mutates
     * the field and returns the updated value.
     */
    fun organizeGuesses(): String {
        // Convert string to a list so can remove dups and sort, then back to a string.
        // distinct() here:
        // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/distinct.html
        guesses = guesses.toList().distinct().sorted().joinToString(separator = "")
        return guesses
    }

    /**
     * Returns the number of guessed characters that are
     * not in the secret word. Caps it at 6, the highest valid index into the icon array
     */
    private fun getCappedNumberOfMissedGuesses(): Int {
        var badGuesses = organizeGuesses().count { it !in secretWord }
        badGuesses = Math.min(badGuesses, iconIds.size - 1) // caps at last hangman pic (lost game)
        return badGuesses
    }

    fun determineStateName(): String {
        val position = determineStateIndex()
        return arrayOf("Won", "Lost", "Active")[position]
    }

    fun determineStateColor(context: Context): Int {
        val position = determineStateIndex()
        return arrayOf(ContextCompat.getColor(context,R.color.gameWon), ContextCompat.getColor(context,R.color.gameFailed), Color.WHITE)[position]
    }

    private fun determineStateIndex(): Int {
        return when {
            "*" !in buildDisplayWord() -> 0
            getCappedNumberOfMissedGuesses() >= 6 -> 1
            else -> 2
        }
    }

    fun determineIconId(): Int {
        return iconIds[getCappedNumberOfMissedGuesses()]
    }

    fun buildLastGuesser(): String {
        return if (lastGuesser.isEmpty()) {
            "no guesses yet"
        } else {
            "last guesser was $lastGuesser"
        }
    }

    fun tryLetter(char: String): Boolean{
        return guesses.contains(char, true)
    }

    companion object {
        fun from(snapshot: DocumentSnapshot): Game {
            val game = snapshot.toObject(Game::class.java)!!
            game.id = snapshot.id
            return game
        }
    }
}