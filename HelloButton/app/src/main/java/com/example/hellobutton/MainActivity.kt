package com.example.hellobutton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var numClicks = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            numClicks++
            updateView()
        }
    }
    private fun updateView(){
        text_view.text = resources.getQuantityString(R.plurals.message_plurals, numClicks, numClicks)
        if (numClicks >= 10){
            val color = ContextCompat.getColor(this, R.color.colorBackground)
            text_view.setTextColor(color)
        }
    }
}