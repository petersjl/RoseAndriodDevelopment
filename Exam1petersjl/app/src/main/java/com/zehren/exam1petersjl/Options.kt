package com.zehren.exam1petersjl

data class Options(var message: String, var color: Int, var font: Int){
    companion object{
        val PREFS = "FONT_PREFERENCES"
        val KEY_MES = "KEY_OPTIONS_MESSAGE"
        val KEY_COLOR = "KEY_OPTIONS_COLOR"
        val KEY_FONT = "KEY_OPTIONS_FONT"
    }
}