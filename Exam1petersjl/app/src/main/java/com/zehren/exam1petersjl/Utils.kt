package com.zehren.exam1petersjl

import android.content.Context
import android.util.Log
object Utils {
    fun detectInstalledFontNames(context: Context): List<FontWrapper> {
        val fontWrappers = ArrayList<FontWrapper>()
        val fontNames = context.assets.list("")!! // no subfolder since only fonts
        for (name in fontNames) {
            Log.d(Constants.TAG, "font: $name") // oops, some nonfont system files too
            if (".otf" in name || ".ttf" in name) {
                fontWrappers.add(FontWrapper(name))
            }
        }
        return fontWrappers
    }
}
