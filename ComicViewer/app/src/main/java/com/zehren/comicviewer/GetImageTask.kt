package com.zehren.comicviewer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask

class GetImageTask(private var comicDisplayer: ComicDisplayer):AsyncTask<String, Void, Bitmap>() {
    override fun doInBackground(vararg urls: String?): Bitmap {
        val inStream = java.net.URL(urls[0]).openStream()
        val bitmap = BitmapFactory.decodeStream(inStream)
        return bitmap
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        comicDisplayer.showComic(result)
    }

    interface ComicDisplayer{
        fun showComic(bitmap: Bitmap?)
    }
}