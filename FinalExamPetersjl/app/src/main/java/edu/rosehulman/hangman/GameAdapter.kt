package edu.rosehulman.hangman

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

class GameAdapter(var context: Context, var uid: String): RecyclerView.Adapter<GameViewHolder>() {

    private var games = ArrayList<Game>()
    private var gamesRef = FirebaseFirestore.getInstance().collection("games")
    var listener: ListenerRegistration? = null

    init {
        listener = gamesRef.addSnapshotListener{snapshot: QuerySnapshot?, err: FirebaseFirestoreException? ->
            if (snapshot == null){
                Log.e("gameerror", "Error in game listener: \n${err.toString()}")
            } else{
                for (docChange in snapshot.documentChanges){
                    when(docChange.type) {
                        DocumentChange.Type.ADDED -> {
                            games.add(0, Game.from(docChange.document))
                            notifyItemInserted(0)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            val newGame = Game.from(docChange.document)
                            val index = findGame(newGame)
                            if (index == -1) Log.e("gameerror", "Error finding the changed game")
                            else {
                                games[index] = newGame
                                notifyItemChanged(index)
                            }
                        }
                        DocumentChange.Type.REMOVED -> {
                            val newGame = Game.from(docChange.document)
                            val index = findGame(newGame)
                            if (index == -1) Log.e("gameerror", "Error finding the changed game")
                            else {
                                games.removeAt(index)
                                notifyItemRemoved(index)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.game_card, parent, false)
        return GameViewHolder(view, this, context)
    }

    override fun getItemCount() = games.size

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(games[position])
    }

    private fun findGame(game: Game): Int{
        for (i in 0 until games.size) {
            if (games[i].id == game.id) {
                return i
            }
        }
        return -1
    }

    fun showDetail(position: Int){
        (context as OnGameSelectedListener).gameSelected(games[position])
    }

    fun remove(position: Int){
        gamesRef.document(games[position].id).delete()
    }

    interface OnGameSelectedListener{
        fun gameSelected(game: Game)
    }
}