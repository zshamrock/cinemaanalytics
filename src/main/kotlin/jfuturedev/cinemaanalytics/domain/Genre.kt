package jfuturedev.cinemaanalytics.domain

import java.util.*

enum class Genre(val title: String = "") {
    ACTION,
    ADVENTURE,
    ANIMATION,
    BIOGRAPHY,
    CHILDREN,
    COMEDY,
    CRIME,
    DOCUMENTARY,
    DRAMA,
    FAMILY,
    FANTASY,
    HORROR,
    LOVE,
    MUSIC,
    MUSICAL,
    NEO_NOIR("Neo-Noir"),
    POST_APOCALYPTIC("Post-apocalyptic"),
    ROMANCE,
    SCI_FI("Sci-Fi"),
    SONG_AND_DANCE("Song and Dance"),
    THRILLER,
    WAR;

    companion object {
        @JvmStatic
        fun parse(genres: String): Genre? {
            return Genre.values().firstOrNull { genre ->
                genres.contains(genre.title.ifEmpty { genre.name.toLowerCase(Locale.ROOT) }, true)
            }
        }
    }
}