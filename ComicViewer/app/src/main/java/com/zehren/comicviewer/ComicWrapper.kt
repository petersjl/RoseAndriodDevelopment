package com.zehren.comicviewer
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Parcelable
import com.zehren.comicviewer.Utils.Utils
import kotlinx.android.parcel.Parcelize

@Parcelize
class ComicWrapper(var colorInput : Int) : Parcelable {
    val colors = arrayOf(
        android.R.color.holo_green_light,
        android.R.color.holo_blue_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_red_light)

    val issueNumber = Utils.randomCleanIssueNumber()
    val color = colors[colorInput % 4]
    var comic: Comic? = null
    var image: Bitmap? = null

}