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
                    Genre.SONG_AND_DANCE,
                    Genre.COMEDY_DRAMA,
                    Genre.MARTIAL,
                    Genre.POLITICAL_SATIRE_COMEDY
                )
            ),
            row(
                "Biography / Child / Dramedy / Historical / Kids / Sports",
                setOf(Genre.BIOGRAPHICAL, Genre.CHILDREN, Genre.COMEDY_DRAMA, Genre.HISTORY, Genre.SPORT)
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
                    Genre.SONG_AND_DANCE,
                    Genre.COMEDY_DRAMA,
                    Genre.MARTIAL,
                    Genre.POLITICAL_SATIRE_COMEDY
                )
            ),
            row(
                "Biography, Child, Dramedy, Historical, Kids, Sports",
                setOf(Genre.BIOGRAPHICAL, Genre.CHILDREN, Genre.COMEDY_DRAMA, Genre.HISTORY, Genre.SPORT)
            )
        ) { genresLine, genresSet ->
            Genre.parse(genresLine) shouldBe genresSet
        }
    }
})