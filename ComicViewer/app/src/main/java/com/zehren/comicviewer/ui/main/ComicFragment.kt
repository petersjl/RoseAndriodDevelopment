package com.zehren.comicviewer.ui.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.zehren.comicviewer.*
import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * A placeholder fragment containing a simple view.
 */
class ComicFragment : Fragment(), GetComicTask.ComicConsumer, GetImageTask.ComicDisplayer{

    private var comicWrapper: ComicWrapper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            comicWrapper = it.getParcelable<ComicWrapper>(ARG_COMIC)
            var urlString = "https://xkcd.com/${comicWrapper?.issueNumber}/info.0.json"
            GetComicTask(this).execute(urlString)
        }
        setHasOptionsMenu(true)

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        comicWrapper?.color?.let { view.setBackgroundResource(it) }
        view.comic_title.text = comicWrapper?.comic?.safe_title
        view.comic_image.setImageBitmap(comicWrapper?.image)
        return view
    }

    override fun onComicLoaded(comic: Comic?) {
        comicWrapper?.comic = comic
        view?.comic_title?.text = comic?.safe_title
        GetImageTask(this).execute(comic?.img)
    }

    override fun showComic(bitmap: Bitmap?) {
        comicWrapper?.image = bitmap
        view?.comic_image?.setImageBitmap(bitmap)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val builder = AlertDialog.Builder(this.activity)
        builder.setTitle("Mouseover text for ${comicWrapper?.issueNumber}")
        builder.setMessage(comicWrapper?.comic?.alt)
        builder.show()
        return true
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_COMIC = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(comicWrapper: ComicWrapper): ComicFragment {
            return ComicFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_COMIC, comicWrapper)
                }
            }
        }
    }
}