package com.zehren.moviequotes

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.dialog_add.view.*

class MovieQuoteAdapter(var context: Context) : RecyclerView.Adapter<MovieQuoteViewHolder>() {
    private val movieQuotes = ArrayList<MovieQuote>()
    private val quotesRef = FirebaseFirestore.getInstance().collection("quotes")

    init {
        quotesRef
            .orderBy(MovieQuote.LAST_TOUCHED_KEY, Query.Direction.ASCENDING)
            .addSnapshotListener {snapshot: QuerySnapshot?, err: FirebaseFirestoreException? ->
                if (err != null) {
                    Log.e("quoteerror", "Listen error: ${err.toString()}")
                }
            for (docChange in snapshot!!.documentChanges) {
                when (docChange.type){
                    DocumentChange.Type.ADDED -> {
                        val movieQuote = MovieQuote.fromSnapshot(docChange.document)
                        movieQuotes.add(0,movieQuote)
                        notifyItemInserted(0)
                    }
                }
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = movieQuotes.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieQuoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_view, parent, false)
        return MovieQuoteViewHolder(view, this, context)
    }

    override fun onBindViewHolder(holder: MovieQuoteViewHolder, index: Int) {
        holder.bind(movieQuotes[index])
    }

    public fun showAddDialog(position: Int = -1){
        val builder = AlertDialog.Builder(context)

        // Set options
        builder.setTitle(if (position == -1) "Add a quote" else "Edit this quote")
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add, null, false)
        if (position != -1){
            view.quote_edit_text.setText(movieQuotes[position].quote)
            view.movie_edit_text.setText(movieQuotes[position].movie)
        }
        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok) {_, _ ->
            val mq = MovieQuote(view.quote_edit_text.text.toString(), view.movie_edit_text.text.toString())
            if (position == -1) this.add(mq) else this.edit(position, mq.movie, mq.quote)
        }
        builder.setNegativeButton(android.R.string.cancel, null)
        if (position >= 0) {
            builder.setNeutralButton("Delete") { _, _ ->
                remove(position)
            }
        }

        builder.create().show()
    }

    fun add(movieQuote: MovieQuote) {
//        movieQuotes.add(0, movieQuote)
//        notifyItemInserted(0)
        quotesRef.add(movieQuote)
    }

    private fun edit(position: Int, movie: String, quote: String){
//        movieQuotes[position] = movieQuote
//        notifyItemChanged(position)
        movieQuotes[position].movie = movie
        movieQuotes[position].quote = quote
        quotesRef.document(movieQuotes[position].id).set(movieQuotes[position])
    }

    private fun remove(position: Int){
//        movieQuotes.removeAt(position)
//        notifyItemRemoved(position)
        quotesRef.document(movieQuotes[position].id).delete()
    }

    fun selectMovieQuote(position: Int){
//        Log.d("NAH", "Swapping selected at $position")
//        movieQuotes[position].isSelected = !movieQuotes[position].isSelected
//        notifyItemChanged(position)
        quotesRef.document(movieQuotes[position].id).update("selected", !movieQuotes[position].isSelected)
    }
}