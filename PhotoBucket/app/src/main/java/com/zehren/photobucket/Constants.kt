package com.zehren.photobucket

object Constants {
    val urls = arrayOf(
        "https://i.pinimg.com/originals/f5/1d/08/f51d08be05919290355ac004cdd5c2d6.png",
        "https://static.pokemonpets.com/images/monsters-images-800-800/133-Eevee.png",
        "https://img.pokemondb.net/artwork/large/squirtle.jpg",
        "https://vignette.wikia.nocookie.net/sonicpokemon/images/e/e0/Charmander_AG_anime.png/revision/latest/scale-to-width-down/340?cb=20130714191911",
        "https://static.pokemonpets.com/images/monsters-images-800-800/1-Bulbasaur.png")

    fun randomImageUrl(): String{
        return urls[(Math.random() * urls.size).toInt()]
    }
}