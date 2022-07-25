package edu.rosehulman.hangman

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_add.view.*
import java.util.*

class MainActivity : AppCompatActivity(), GameAdapter.OnGameSelectedListener, GameDetailFragment.OnUnexpectedDeleteListener{

    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    private var backstackListener = FragmentManager.OnBackStackChangedListener {
        if (supportFragmentManager.backStackEntryCount == 0) {
            fab.show()
        } else {
            fab.hide()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { _ ->
            makeNewGame()
        }

        initializeListeners()
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
            R.id.logout -> {auth.signOut(); true}
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }

    private fun initializeListeners(){
        authStateListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
            val user = auth.currentUser
            Log.d(Constants.TAG, "Current user is $user")
            if (user == null){
                supportActionBar?.hide()
                fab.hide()
                supportFragmentManager.removeOnBackStackChangedListener(backstackListener)
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.fragment_container, SignInFragment())
                ft.commit()
            } else {
                supportActionBar?.show()
                fab.show()
                supportFragmentManager.addOnBackStackChangedListener(backstackListener)
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.fragment_container, GameListFragment(user.uid))
                ft.commit()
            }
        }
    }

    private fun makeNewGame(){
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Create a new Hangman game")
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add, null, false)
        builder.setView(view)
            .setPositiveButton(android.R.string.ok) {_, _ ->
                val text = view.add_edit_text.text.toString()
                if (text == "") return@setPositiveButton
                val game = Game(creator = auth.currentUser?.displayName.toString(), secretWord = text.toLowerCase(Locale.ROOT))
                FirebaseFirestore.getInstance().collection("games").add(game)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
            .show()
    }

    override fun gameSelected(game: Game) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, GameDetailFragment(game))
        ft.addToBackStack("detail")
        ft.commit()
    }

    override fun unexpectedDelete() {
        supportFragmentManager.popBackStack()
        Toast.makeText(this, "The game you were looking at was deleted.", Toast.LENGTH_LONG).show()
    }
}