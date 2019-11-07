package jfuturedev.cinemaanalytics.domain

import java.util.Locale

private val genresMap =
    Genre.values().associateBy { genre -> genre.title.ifEmpty { genre.name }.toLowerCase(Locale.ROOT) }

enum class Genre(val title: String = "", alias: Set<Genre> = emptySet()) {
    ACTION,
    ADVENTURE,
    ANIMATION,
    ANIME,
    BIBLICAL,
    BIOGRAPHICAL,
    BIOGRAPHY(alias = setOf(BIOGRAPHICAL)),
    BIOPIC,
    CHILDREN,
    CHILD(alias = setOf(CHILDREN)),
    COMEDY,
    COMEDY_DRAMA("Comedy-Drama"),
    COSTUME,
    CRIME,
    DRAMEDY(alias = setOf(COMEDY_DRAMA)),
    DISASTER,
    DOCUMENTARY,
    DRAMA,
    EPIC,
    FAITH,
    FAMILY,
    FANTASY,
    HEIST,
    HISTORY,
    HISTORICAL(alias = setOf(HISTORY)),
    HORROR,
    KIDS(alias = setOf(CHILDREN)),
    LOVE,
    MARTIAL,
    MARTIAL_ARTS("Martial Arts", alias = setOf(MARTIAL)),
    MOCKUMENTARY,
    MOVEMENT,
    MUSIC,
    MUSICAL,
    MYSTERY,
    NATURE,
    NEO_NOIR("Neo-Noir"),
    POLITICAL_SATIRE_COMEDY("Political Satire Comedy"),
    POST_APOCALYPTIC("Post-apocalyptic"),
    PUPPET,
    ROMANCE,
    SCI_FI("Sci-Fi"),
    SCIENCE_FICTION("Science fiction", alias = setOf(SCI_FI)),
    SONG_AND_DANCE("Song and Dance"),
    SPORT,
    SPORTS(alias = setOf(SPORT)),
    SPY,
    STORY,
    SUPERHERO,
    SUPERNATURAL,
    SUSPENSE,
    TEEN,
    TERROR,
    THRILLER,
    WAR,
    WESTERN,
    ZOMBIE;

    companion object {
        @JvmStatic
        fun parse(genresLine: String): Set<Genre> {
            return genresLine.split(",", "/").map { it.trim().toLowerCase(Locale.ROOT) }.mapNotNull { genresMap[it] }
                .toSet()
        }
    }
}