package com.zehren.exam1petersjl

data class FontWrapper(val name: String) {
    // strips the .*tf extension, assuming the font name contains no other dot.
    var displayName: String = name.split(".")[0]
    override fun toString(): String {
        return "name = $name ::: displayName = $displayName"
    }
}
