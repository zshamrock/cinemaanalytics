package jfuturedev.cinemaanalytics.domain

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class GenreSpec : StringSpec({
    "parse genres" {
        forall(
            row("Drama", setOf(Genre.DRAMA)),
            row("Drama / Action / Romance", setOf(Genre.DRAMA, Genre.ACTION, Genre.ROMANCE)),
            row(
                "Drama / Children / History / Computer / War",
                setOf(Genre.DRAMA, Genre.CHILDREN, Genre.HISTORY, Genre.WAR)
            ),
            row(
                "Neo-Noir / Post-apocalyptic / Sci-Fi / Science fiction / Song and Dance/ Comedy-Drama / Martial Arts / Political Satire Comedy",
                setOf(
                    Genre.NEO_NOIR,
                    Genre.POST_APOCALYPTIC,
                    Genre.SCI_FI,
                    Genre.SCIENCE_FICTION,
                    Genre.SONG_AND_DANCE,
                    Genre.COMEDY_DRAMA,
                    Genre.MARTIAL_ARTS,
                    Genre.POLITICAL_SATIRE_COMEDY
                )
            ),
            row("Computer", setOf()),
            row("Drama, Action, Romance", setOf(Genre.DRAMA, Genre.ACTION, Genre.ROMANCE)),
            row(
                "Drama, Children, History, Computer, War",
                setOf(Genre.DRAMA, Genre.CHILDREN, Genre.HISTORY, Genre.WAR)
            ),
            row(
                "Neo-Noir, Post-apocalyptic, Sci-Fi, Science fiction, Song and Dance, Comedy-Drama, Martial Arts, Political Satire Comedy",
                setOf(
                    Genre.NEO_NOIR,
                    Genre.POST_APOCALYPTIC,
                    Genre.SCI_FI,
                    Genre.SCIENCE_FICTION,
                    Genre.SONG_AND_DANCE,
                    Genre.COMEDY_DRAMA,
                    Genre.MARTIAL_ARTS,
                    Genre.POLITICAL_SATIRE_COMEDY
                )
            )
        ) { genresLine, genresSet ->
            Genre.parse(genresLine) shouldBe genresSet
        }
    }
})