package jfuturedev.cinemaanalytics.domain

import java.util.*

enum class Genre(val title: String = "", alias: Set<Genre> = emptySet()) {
    ACTION,
    ADVENTURE,
    ANIMATION,
    BIOGRAPHICAL,
    BIOGRAPHY(alias = setOf(BIOGRAPHICAL)),
    CHILD,
    CHILDREN,
    COMEDY,
    CRIME,
    DISASTER,
    DOCUMENTARY,
    DRAMA,
    FAMILY,
    FANTASY,
    HISTORY,
    HORROR,
    LOVE,
    MUSIC,
    MUSICAL,
    MYSTERY,
    NEO_NOIR("Neo-Noir"),
    POST_APOCALYPTIC("Post-apocalyptic"),
    ROMANCE,
    SCI_FI("Sci-Fi"),
    SONG_AND_DANCE("Song and Dance"),
    SUSPENSE,
    THRILLER,
    WAR,
    WESTERN;

    companion object {
        @JvmStatic
        fun parse(genres: String): Genre? {
            return Genre.values().firstOrNull { genre ->
                genres.contains(genre.title.ifEmpty { genre.name.toLowerCase(Locale.ROOT) }, true)
            }
        }
    }
}