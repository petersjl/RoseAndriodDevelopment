package edu.rosehulman.historicaldocs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DocListFragment.OnDocSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            val fragment = AboutFragment()
            val ft = supportFragmentManager.beginTransaction()
            ft.add(R.id.fragment_container, fragment)
            ft.commit()
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Just in case we wanted to add the app bar later.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var switchTo: Fragment? = null
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_about -> {
                // TODO: Launch the AboutFragment
                switchTo = AboutFragment()
            }
            R.id.nav_docs -> {
                // TODO: Launch a DocListFragment
                // (which later will launch DocDetailFragments)
                switchTo = DocListFragment()
            }
            R.id.nav_settings -> startActivity(Intent(Settings.ACTION_SETTINGS))
        }
        if (switchTo != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container, switchTo)
            while (supportFragmentManager.backStackEntryCount > 0){
                supportFragmentManager.popBackStackImmediate()
            }
            ft.commit()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDocSelected(doc: Doc) {
        val docFragment = DocDetailFragment.newInstance(doc)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, docFragment)
        ft.addToBackStack("detail")
        ft.commit()
    }


}
