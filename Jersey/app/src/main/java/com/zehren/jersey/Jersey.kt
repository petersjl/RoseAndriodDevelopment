package com.zehren.jersey

data class Jersey(var name: String = "ANDROID", var number: Int = 17, var isRed: Boolean = true){
    companion object {
        val PREFS = "JERSEY_PREFS"
        val KEY_NAME = "KEY_JERSEY_NAME"
        val KEY_NUMBER = "KEY_JERSEY_NUMBER"
        val KEY_RED = "KEY_JERSEY_ISRED"
    }
}