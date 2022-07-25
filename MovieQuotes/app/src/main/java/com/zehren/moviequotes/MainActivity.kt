package com.zehren.moviequotes

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.provider.SettingsSlicesContract
import android.util.Log
import android.view.LayoutInflater
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_add.view.*
import kotlinx.android.synthetic.main.dialog_set_author.view.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter: MovieQuoteAdapter
    lateinit var author: String
    lateinit var settingsRef: DocumentReference
    lateinit var settingsListener: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            adapter.showAddDialog()
        }

        adapter = MovieQuoteAdapter(this)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = adapter

        settingsRef = FirebaseFirestore.getInstance().collection("settings").document("settings")
        settingsListener = settingsRef.addSnapshotListener {snapshot: DocumentSnapshot?, err: FirebaseFirestoreException? ->
            if (err != null) {
                Log.e("Firebase Error", "Listener error: ${err.toString()}")
            }else{
                author = snapshot!!.get("author") as String
                toolbar.title = author + " MovieQuotes"
            }

        }

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                //startActivity(Intent(Settings.ACTION_SETTINGS))
                getWhichSettings()
                true
            }
            R.id.action_increase_size -> {
                changeSize(4)
                true
            }
            R.id.action_decrease_size ->{
                changeSize(-4)
                true
            }
            R.id.action_clear -> {
                showclearDialog()
                true
            }
            R.id.action_get_movie -> {
                getFavoriteMovie()
                true
            }
            R.id.action_get_moviequote -> {
                getFavoriteMovieQuote()
                true
            }
            R.id.action_get_all_quotes -> {
                getAllQuotes()
                true
            }
            R.id.action_set_author -> {
                showAuthorDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getFavoriteMovie() {
        val favoriteQuoteRef = FirebaseFirestore.getInstance().collection("favorites").document("moviequote")
        favoriteQuoteRef.get().addOnSuccessListener { snapshot: DocumentSnapshot ->
            val movie = (snapshot["movie"] ?: "") as String
            Toast.makeText(this, movie, Toast.LENGTH_LONG).show()
        }
    }

    private fun getFavoriteMovieQuote() {
        val favoriteQuoteRef = FirebaseFirestore.getInstance().collection("favorites").document("moviequote")
        favoriteQuoteRef.get().addOnSuccessListener { snapshot: DocumentSnapshot ->
            val mq = snapshot.toObject(MovieQuote:: class.java)
            adapter.add(mq!!)
        }
    }

    private fun getAllQuotes() {
        val quotesRef = FirebaseFirestore.getInstance().collection("quotes")
        quotesRef.get().addOnSuccessListener { snapshot: QuerySnapshot ->
            for (doc in snapshot){
                val mq = doc.toObject(MovieQuote:: class.java)
                adapter.add(mq)
            }
        }
    }

    private fun showAuthorDialog(){
        val builder = AlertDialog.Builder(this)

        // Set options
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_set_author, null, false)
        view.author_edit_text.setText(author)
        builder
            .setView(view)
            .setPositiveButton(android.R.string.ok) {_, _ ->
                settingsRef.update("author", view.author_edit_text.text.toString())
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
            .show()
    }

    private fun getWhichSettings(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Which settings?")
        builder.setItems(R.array.settings_types){_, index ->
            val settingsType = when (index) {
                0 -> Settings.ACTION_SOUND_SETTINGS
                1 -> Settings.ACTION_SEARCH_SETTINGS
                else -> Settings.ACTION_SETTINGS
            }
            startActivity(Intent(settingsType))
        }
        builder.show()
    }

    private fun changeSize(delta: Int){
//        var currentSize = quote_text.textSize / resources.displayMetrics.scaledDensity
//        currentSize += delta
//        quote_text.textSize = currentSize
//        movie_text.textSize = currentSize
    }

    private fun showclearDialog(){
//        val builder = AlertDialog.Builder(this)
//
//        // Set options
//        builder.setTitle("Confirm delete?")
//        builder.setMessage(getString(R.string.confirm_delete_message))
//        builder.setPositiveButton(android.R.string.ok) {_, _ ->
//            updateQuote(MovieQuote("Quote", "Movie"))
//        }
//        builder.setNegativeButton(android.R.string.cancel, null)
//
//        builder.create().show()
    }
}