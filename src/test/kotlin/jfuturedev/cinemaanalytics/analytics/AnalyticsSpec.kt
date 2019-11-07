package jfuturedev.cinemaanalytics.analytics

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import jfuturedev.cinemaanalytics.domain.Film
import jfuturedev.cinemaanalytics.domain.Genre
import java.time.Month

class AnalyticsSpec : StringSpec({
    "display dynamics of film releases" {
        Analytics(
            listOf(
                // 2017
                Film(2017, Month.JANUARY, 1, "", "", setOf(Genre.DRAMA, Genre.COMEDY)),
                Film(2017, Month.JANUARY, 2, "", "", setOf(Genre.HISTORY, Genre.SCI_FI)),
                Film(2017, Month.JANUARY, 3, "", "", setOf(Genre.SCI_FI, Genre.FANTASY)),
                Film(2017, Month.JANUARY, 4, "", "", setOf(Genre.SCI_FI, Genre.FANTASY)),
                Film(2017, Month.JANUARY, 5, "", "", setOf(Genre.SCI_FI, Genre.FANTASY, Genre.COMEDY)),
                Film(2017, Month.JANUARY, 6, "", "", setOf(Genre.FANTASY)),
                // 2018
                Film(2018, Month.JANUARY, 1, "", "", setOf(Genre.HISTORY, Genre.COMEDY)),
                Film(2018, Month.JANUARY, 2, "", "", setOf(Genre.HISTORY, Genre.SCI_FI)),
                Film(2018, Month.JANUARY, 3, "", "", setOf(Genre.SCI_FI, Genre.COMEDY)),
                Film(2018, Month.JANUARY, 4, "", "", setOf(Genre.SCI_FI, Genre.FANTASY)),
                Film(2018, Month.JANUARY, 5, "", "", setOf(Genre.SCI_FI, Genre.COMEDY)),
                Film(2018, Month.JANUARY, 6, "", "", setOf(Genre.FANTASY)),
                // 2019
                Film(2019, Month.JANUARY, 1, "", "", setOf(Genre.FANTASY, Genre.COMEDY)),
                Film(2019, Month.JANUARY, 2, "", "", setOf(Genre.FANTASY, Genre.SCI_FI)),
                Film(2019, Month.JANUARY, 3, "", "", setOf(Genre.SCI_FI, Genre.COMEDY)),
                Film(2019, Month.JANUARY, 4, "", "", setOf(Genre.COMEDY, Genre.DRAMA)),
                Film(2019, Month.JANUARY, 5, "", "", setOf(Genre.DRAMA, Genre.FANTASY, Genre.COMEDY)),
                Film(2019, Month.JANUARY, 6, "", "", setOf(Genre.DRAMA))
            ),
            listOf(
                // 2017
                Film(2017, Month.JANUARY, 1, "", "", setOf(Genre.HISTORY, Genre.COMEDY)),
                Film(2017, Month.JANUARY, 2, "", "", setOf(Genre.HISTORY, Genre.SCI_FI)),
                Film(2017, Month.JANUARY, 3, "", "", setOf(Genre.COMEDY, Genre.FANTASY)),
                Film(2017, Month.JANUARY, 4, "", "", setOf(Genre.COMEDY, Genre.FANTASY)),
                Film(2017, Month.JANUARY, 5, "", "", setOf(Genre.SCI_FI, Genre.FANTASY, Genre.COMEDY)),
                Film(2017, Month.JANUARY, 6, "", "", setOf(Genre.HISTORY)),
                // 2018
                Film(2018, Month.JANUARY, 1, "", "", setOf(Genre.DRAMA, Genre.COMEDY)),
                Film(2018, Month.JANUARY, 2, "", "", setOf(Genre.DRAMA, Genre.SCI_FI)),
                Film(2018, Month.JANUARY, 3, "", "", setOf(Genre.DRAMA, Genre.COMEDY)),
                Film(2018, Month.JANUARY, 4, "", "", setOf(Genre.SCI_FI, Genre.FANTASY)),
                Film(2018, Month.JANUARY, 5, "", "", setOf(Genre.HISTORY, Genre.COMEDY)),
                Film(2018, Month.JANUARY, 6, "", "", setOf(Genre.COMEDY)),
                // 2019
                Film(2019, Month.JANUARY, 1, "", "", setOf(Genre.HISTORY, Genre.COMEDY)),
                Film(2019, Month.JANUARY, 2, "", "", setOf(Genre.HISTORY, Genre.SCI_FI)),
                Film(2019, Month.JANUARY, 3, "", "", setOf(Genre.SCI_FI, Genre.COMEDY)),
                Film(2019, Month.JANUARY, 4, "", "", setOf(Genre.COMEDY, Genre.HISTORY)),
                Film(2019, Month.JANUARY, 5, "", "", setOf(Genre.FANTASY, Genre.SCI_FI)),
                Film(2019, Month.JANUARY, 6, "", "", setOf(Genre.DRAMA))
            )
        ).runDynamics(listOf(Genre.COMEDY, Genre.DRAMA, Genre.SCI_FI, Genre.HISTORY, Genre.FANTASY)) shouldBe Report(
            listOf(
                "Comedy, 2017: China - 2 films; USA - 4. Comedy, 2018: China - 3 films; USA - 4. Comedy, 2019: China - 4 films; USA - 3.",
                "Drama, 2017: China - 1 films; USA - 0. Drama, 2018: China - 0 films; USA - 3. Drama, 2019: China - 3 films; USA - 1.",
                "Sci-Fi, 2017: China - 4 films; USA - 2. Sci-Fi, 2018: China - 4 films; USA - 2. Sci-Fi, 2019: China - 2 films; USA - 3.",
                "History, 2017: China - 1 films; USA - 3. History, 2018: China - 2 films; USA - 1. History, 2019: China - 0 films; USA - 3.",
                "Fantasy, 2017: China - 4 films; USA - 3. Fantasy, 2018: China - 2 films; USA - 1. Fantasy, 2019: China - 3 films; USA - 1."
            )
        )
    }
})