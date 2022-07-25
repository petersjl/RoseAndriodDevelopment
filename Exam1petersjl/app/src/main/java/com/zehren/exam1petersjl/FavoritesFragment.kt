package com.zehren.exam1petersjl

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoritesFragment(var fonts: List<FontWrapper>): Fragment() {

    lateinit var myContext : Context
    lateinit var options: Options
    lateinit var title: TextView
    lateinit var messageText: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context
        options = (myContext as OptionsFragment.OptionsProvider).getAvailableOptions()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onStart() {
        super.onStart()
        title = favorites_title
        messageText = favorites_text
        val font = fonts[options.font]
        title.text = font.displayName
        val tf = Typeface.createFromAsset(myContext.assets, font.name)
        messageText.typeface = tf
        messageText.setTextColor(options.color)
        messageText.text = options.message
        messageText.movementMethod = ScrollingMovementMethod()
    }

}