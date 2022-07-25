package edu.rosehulman.hangman

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_detail.view.*

class GameDetailFragment(var game: Game): Fragment() {

    private val gameRef = FirebaseFirestore.getInstance().collection("games").document(game.id)
    lateinit var listener: ListenerRegistration

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        view.detail_input.addTextChangedListener(afterTextChanged = {text: Editable? ->
            if (text.toString() == "") return@addTextChangedListener
            checkAndUpdate(text.toString())
            view.detail_input.text.clear()
        })


        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        listener = gameRef.addSnapshotListener { doc: DocumentSnapshot?, error: FirebaseFirestoreException? ->
            if (doc == null) {
                Log.e(Constants.ERR, "Error setting up game listener: $error")
            } else {
                if (!doc.exists()){
                    (context as MainActivity).unexpectedDelete()
                    return@addSnapshotListener
                }
                this.game = Game.from(doc)
                Log.d(Constants.TAG, "The new game is : $game")
                view?.detail_game_state?.text = "${game.determineStateName()}, ${game.buildLastGuesser()}"
                view?.detail_image?.setImageResource(game.determineIconId())
                view?.detail_guesses?.text = "Guesses:\n${game.organizeGuesses()}"
                view?.detail_word?.text = "Word: ${game.buildDisplayWord()}"
                if (game.displayWord == game.secretWord || game.determineStateName() == "Lost") {
                    view?.detail_input?.isEnabled = false
                    view?.detail_input?.hint = "GameOver"
                } else {
                    view?.detail_input?.isEnabled = true
                    view?.detail_input?.hint = "Guess a letter"
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        listener.remove()
    }

    private fun checkAndUpdate(char: String){
        if (game.tryLetter(char)){
            Log.d(Constants.TAG, "Letter $char was already used")
            return
        } else {
            Log.d(Constants.TAG, "Letter $char has not been used. Updating firebase")
            game.guesses += char
            Log.d(Constants.TAG, "New guesses are ${game.guesses}")
            game.organizeGuesses()
            game.lastGuesser = FirebaseAuth.getInstance().currentUser?.displayName!!
            gameRef.set(game, SetOptions.merge())
        }
    }

    interface OnUnexpectedDeleteListener {
        fun unexpectedDelete()
    }
}