package com.zehren.exam1petersjl

import android.content.Context
import android.graphics.Color
import android.graphics.Path
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zehren.exam1petersjl.fontsrecycler.FontsFragment
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, OptionsFragment.OptionsProvider {
    lateinit var fonts : List<FontWrapper>

    lateinit var options: Options

    var currentFragment: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        nav_view.setOnNavigationItemSelectedListener(this)
        fonts = Utils.detectInstalledFontNames(this)

        val prefs = getSharedPreferences(Options.PREFS, Context.MODE_PRIVATE)

        val message = prefs.getString(Options.KEY_MES, Constants.LOREM).toString()
        val color = prefs.getInt(Options.KEY_COLOR, Color.BLACK)
        val font = prefs.getInt(Options.KEY_FONT, 0)
        options = Options(message, color, font)
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
            R.id.secret_button -> {
                Log.d(Constants.TAG, "Pressed the button")
                if (options.message.contains("robot")){
                    Toast.makeText(this, "Found a robot", Toast.LENGTH_LONG).show()
                    options.font = 13
                    currentFragment?.let { onNavigationItemSelected(it) }
                }else{
                    Toast.makeText(this, "Did not find a robot", Toast.LENGTH_LONG).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var switchTo : Fragment? = null
        currentFragment = item
        when (item.itemId){
            R.id.nav_options ->     { switchTo = OptionsFragment() }
            R.id.nav_fonts ->       { switchTo = FontsFragment(fonts) }
            R.id.nav_favorites ->   { switchTo = FavoritesFragment(fonts) }
        }
        if (switchTo != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container, switchTo)
            while (supportFragmentManager.backStackEntryCount > 0){
                supportFragmentManager.popBackStackImmediate()
            }
            ft.commit()
        }
        return true
    }

    fun updateFavorite(){

    }

    override fun onPause() {
        super.onPause()
        val editor = getSharedPreferences(Options.PREFS, Context.MODE_PRIVATE).edit()
        editor.putString(Options.KEY_MES, options.message)
        editor.putInt(Options.KEY_COLOR, options.color)
        editor.putInt(Options.KEY_FONT, options.font)
        editor.commit()
    }

    override fun getAvailableOptions() = options


}