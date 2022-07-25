package com.zehren.photobucket

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.replace
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_set_title.view.*
import com.zehren.photobucket.R.menu.menu_main

class MainActivity : AppCompatActivity(), PhotoBucketAdapter.OnPicSelectedListener, SplashFragment.OnLoginButtonPressedListener {
    lateinit var settingsRef: DocumentReference
    lateinit var settingsListener: ListenerRegistration
    private val auth = FirebaseAuth.getInstance()
    lateinit var authStateListener: FirebaseAuth.AuthStateListener

    private var showingAll = false

    private var picList: PicListFragment? = null

    // Request code for launching the sign in Intent.
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        initializeListeners()
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }

    private fun initializeListeners() {
        authStateListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
            val user = auth.currentUser
            if (user != null){
                switchToMovieQuoteFragment(user.uid)
            }else {
                switchToSplashFragment()
            }
        }

        settingsRef = FirebaseFirestore.getInstance().collection("settings").document("settings")
        settingsListener = settingsRef.addSnapshotListener{ snapshot, err ->
            if (err != null) {
                Log.e("picserror", "Error listening for settings")
            } else {
                toolbar.title = snapshot!!.get("title") as String
            }
        }

    }

    private fun switchToSplashFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, SplashFragment())
        ft.commit()
    }

    private fun switchToMovieQuoteFragment(uid: String) {
        val ft = supportFragmentManager.beginTransaction()
        picList = PicListFragment.newInstance(uid)
        ft.replace(R.id.fragment_container, picList!!)
        ft.commit()
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
            R.id.action_title -> {showTitleDialog(); true}
            R.id.which_cards -> {swapCards(); true}
            R.id.logout -> {auth.signOut(); true}
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun swapCards(){
        if (picList == null) return
        showingAll = !showingAll
        Log.d("checkthis", "Switching to ${if (showingAll) "showing all" else "showing mine"}")
        picList!!.setUpListener(showingAll)
        toolbar.menu.findItem(R.id.which_cards).setTitle(if (showingAll) "Show mine" else "Show all")
    }

    fun showTitleDialog(){
        val builder = AlertDialog.Builder(this)

        // Set options
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_set_title, null, false)
        view.title_edit_text.setText(toolbar.title)
        builder
            .setView(view)
            .setPositiveButton(android.R.string.ok) {_, _ ->
                settingsRef.update("title", view.title_edit_text.text.toString())
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
            .show()
    }

    override fun picSelected(pic: Pic) {
        val picFragment = PicDetailFragment.newInstance(pic)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, picFragment)
        ft.addToBackStack("detail")
        ft.commit()
    }

    override fun onLoginButtonPressed() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.pikachu)
                .build(),
            RC_SIGN_IN)
    }
}