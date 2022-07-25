package com.zehren.comicviewer

data class Comic(
    var num: Int,
    var month: Int,
    var day: Int,
    var year: Int,
    var link: String,
    var news: String,
    var transcript: String,
    var safe_title: String,
    var alt: String,
    var img: String,
    var title: String
)
