package edu.rosehulman.hangman

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.fragment_sign_in.view.*

class SignInFragment: Fragment() {

    lateinit var root: View
    // Request code for launching the sign in Intent.
    private val RC_SIGN_IN = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.root = inflater.inflate(R.layout.fragment_sign_in, container, false)
        root.sign_in_button.setOnClickListener {
            // Choose authentication providers
            val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

            // Create and launch sign-in intent
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setLogo(R.drawable.hangman6)
                    .build(),
                RC_SIGN_IN)
        }

        return this.root
    }
}