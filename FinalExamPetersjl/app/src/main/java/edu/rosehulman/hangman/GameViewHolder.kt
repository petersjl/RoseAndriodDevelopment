package edu.rosehulman.hangman

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.game_card.view.*

class GameViewHolder(itemView: View, adapter: GameAdapter, private var context: Context) :
    RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener {
            adapter.showDetail(adapterPosition)
        }
        itemView.setOnLongClickListener {
            adapter.remove(adapterPosition)
            true
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(game: Game){
        itemView.card_image.setImageResource(game.determineIconId())
        itemView.card_word.text = game.buildDisplayWord()
        itemView.card_creator.text = game.creator
        itemView.card_guesses.text = "Guesses: ${game.organizeGuesses()}"
        itemView.card_last_guess.text = game.buildLastGuesser()
        itemView.setBackgroundColor(game.determineStateColor(context))
    }
}