package com.zehren.photobucket

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.common.base.Strings
import java.lang.Exception
import java.net.URL

class GetPicTask(private var picDisplayer: PicDisplayer): AsyncTask<String, Void, Bitmap>() {
    override fun doInBackground(vararg urls: String?): Bitmap {
        val inStream = java.net.URL(urls[0]).openStream()
        val bitmap = BitmapFactory.decodeStream(inStream)
        return bitmap
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        picDisplayer.showPic(result)
    }

    interface PicDisplayer {
        fun showPic(bitmap: Bitmap?)
    }
}