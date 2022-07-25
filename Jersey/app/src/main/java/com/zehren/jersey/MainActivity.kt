package com.zehren.jersey

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.zehren.jersey.Jersey
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.edit_dialog.view.*

class MainActivity : AppCompatActivity() {
    private var j = Jersey()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val prefs = getSharedPreferences(Jersey.PREFS, Context.MODE_PRIVATE)

        j.name = prefs.getString(Jersey.KEY_NAME, getString(R.string.default_jersey_name)).toString()
        j.number = prefs.getInt(Jersey.KEY_NUMBER, getString(R.string.default_jersey_number).toInt())
        j.isRed = prefs.getBoolean(Jersey.KEY_RED, true)
        updateView()

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            showEditDialog()
        }
    }

    private fun showEditDialog(){
        val builder = AlertDialog.Builder(this)

        // Set options
        builder.setTitle(R.string.edit_dialog_title)
        val view = LayoutInflater.from(this).inflate(R.layout.edit_dialog, null, false)
        view.name_edit_view.setText(j.name)
        view.number_edit_view.setText(j.number.toString())
        view.red_button.isChecked = j.isRed
        builder.setView(view)

        builder.setPositiveButton(android.R.string.ok){_, _ ->
            j.name = view.name_edit_view.text.toString()
            j.number = view.number_edit_view.text.toString().toInt()
            j.isRed = view.red_button.isChecked
            updateView()
        }

        builder.setNegativeButton(android.R.string.cancel, null)

        builder.show()
    }

    private fun updateView(){
        jersey_name.text = j.name
        jersey_number.text = j.number.toString()
        jersey_image.setImageResource(if (j.isRed) R.drawable.red_jersey else R.drawable.blue_jersey)
    }

    //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
    //                    .setAction("Action", null).show()

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
            R.id.action_reset -> {
                val old = Jersey()
                old.name = j.name
                old.number = j.number
                old.isRed = j.isRed
                j = Jersey()
                updateView()
                val snackbar = Snackbar.make(findViewById(R.id.main_activity), R.string.reset_message,Snackbar.LENGTH_LONG).setAction("UNDO") {
                    j.name = old.name
                    j.number = old.number
                    j.isRed = old.isRed
                    updateView()
                }
                snackbar.show()
                true
            }
            R.id.action_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        val prefs = getSharedPreferences(Jersey.PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(Jersey.KEY_NAME, j.name)
        editor.putInt(Jersey.KEY_NUMBER, j.number)
        editor.putBoolean(Jersey.KEY_RED, j.isRed)
        editor.commit()
    }
}