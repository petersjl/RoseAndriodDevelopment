package com.zehren.comicviewer

import android.os.AsyncTask
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.lang.Exception
import java.net.URL

class GetComicTask(private var comicConsumer: ComicConsumer): AsyncTask<String, Void, Comic>() {
    override fun doInBackground(vararg urlStrings: String?): Comic? {
        val url = URL(urlStrings[0])
        Log.d("tag", "URL: $url")
        return try {
            val s = url.readText()
            Log.d("tag", "Read: $s")
            val mapper = ObjectMapper().registerModule(KotlinModule())
            val comic = mapper.readValue<Comic>(s)
            Log.d("tag", "As comic: ${comic.toString()}")
            comic
        }catch (e: Exception){
            Log.e("tag", "EXCEPTION: ${e.toString()}")
            null
        }
    }

    override fun onPostExecute(result: Comic?) {
        super.onPostExecute(result)
        comicConsumer.onComicLoaded(result)
    }

    interface ComicConsumer {
        fun onComicLoaded(comic: Comic?)
    }
}