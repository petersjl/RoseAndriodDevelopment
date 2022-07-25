package edu.rosehulman.colorchooser

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import kotlinx.android.synthetic.main.activity_input.*

class InputActivity : AppCompatActivity() {

    private var colorMessage = ColorMessage("Hello World", Color.GRAY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)
        updateUI()

        colorMessage.message = intent.getStringExtra(ColorMessage.EXTRA_MESSAGE)
        colorMessage.backgroundColor = intent.getIntExtra(ColorMessage.EXTRA_COLOR, Color.GRAY)

        activity_input_button.setOnClickListener {
            showColorDialog()
        }
    }

    private fun updateUI() {
        activity_input_message.setText(colorMessage.message)
        activity_input_layout.setBackgroundColor(colorMessage.backgroundColor)
    }

    // From https://android-arsenal.com/details/1/1693
    private fun showColorDialog() {
        val builder = ColorPickerDialogBuilder.with(this)
        builder.setTitle("Choose HSV color")
        builder.initialColor(colorMessage.backgroundColor)
        builder.wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
        builder.density(6)
        builder.setOnColorSelectedListener { selectedColor ->
            Toast.makeText(
                this@InputActivity,
                "onColorSelected: 0x" + Integer.toHexString(selectedColor),
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setPositiveButton(android.R.string.ok) { dialog, selectedColor, allColors ->
            colorMessage.message = activity_input_message.text.toString()
            colorMessage.backgroundColor = selectedColor
            updateUI()
            // TODO: Use an intent to send info back to the
            // activity that called this one for a result.
            val resultIntent = Intent(this, MainActivity::class.java)
            resultIntent.putExtra(ColorMessage.EXTRA_MESSAGE, colorMessage.message)
            resultIntent.putExtra(ColorMessage.EXTRA_COLOR, colorMessage.backgroundColor)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
        builder.setNegativeButton(getString(android.R.string.cancel), null)
        builder.build().show()
    }
}