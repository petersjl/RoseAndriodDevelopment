package com.zehren.comicviewer.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zehren.comicviewer.ComicWrapper
import com.zehren.comicviewer.R

private var COMICS = arrayOf(
    ComicWrapper(0),
    ComicWrapper(1),
    ComicWrapper(2),
    ComicWrapper(3),
    ComicWrapper(4)
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class ComicsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return ComicFragment.newInstance(COMICS[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Issue ${COMICS[position].issueNumber}"
    }

    override fun getCount(): Int {
        return COMICS.size
    }

    fun addNewComic(){
        COMICS += ComicWrapper(COMICS.size + 1)
        this.notifyDataSetChanged()
    }
}