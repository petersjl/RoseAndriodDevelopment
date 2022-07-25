package com.zehren.exam1petersjl

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import kotlinx.android.synthetic.main.fragment_options.*

class OptionsFragment : Fragment(){

    lateinit var myContext : Context
    lateinit var options: Options

    lateinit var colorButton: Button
    lateinit var saveButton: Button
    lateinit var messageText: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context
        options = (myContext as OptionsProvider).getAvailableOptions()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_options, container, false)
    }

    override fun onStart() {
        super.onStart()
        colorButton = button_change_color
        saveButton = button_save
        messageText = edit_text
        messageText.text = options.message
        colorButton.setOnClickListener{changeColor()}
        saveButton.setOnClickListener{saveMessage()}
        setButtonText()
        updateColors()
    }

    fun changeColor(){
        ColorPickerDialogBuilder
            .with(myContext)
            .setTitle("Choose font color")
            .initialColor(options.color)
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setPositiveButton("ok", ColorPickerClickListener(){dialog, selectedColor, allColors ->
                options.color = selectedColor
                updateColors()
            })
            .setNegativeButton("cancel", null)
            .build()
            .show()
    }

    fun saveMessage(){
        var newMessage = messageText.text.toString()
        if (newMessage == "") {
            newMessage = Constants.LOREM
            messageText.text = newMessage
        }
        options.message = newMessage
        setButtonText()
    }

    fun setButtonText(){
        var str = ""
        if (options.message.length > 10){
            str = "[" + options.message.substring(0,9) + "]"
        }else{
            str = "[" + options.message + "]"
        }
        saveButton.text = str
    }

    fun updateColors(){
        ViewCompat.setBackgroundTintList(saveButton, ColorStateList.valueOf(options.color))
        edit_text.setTextColor(options.color)
        saveButton.setTextColor(if (options.color == Color.BLACK) Color.WHITE else Color.BLACK)
    }

    interface OptionsProvider{
        fun getAvailableOptions(): Options
    }
}