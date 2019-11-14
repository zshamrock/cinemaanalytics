package jfuturedev.cinemaanalytics.analytics

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import jfuturedev.cinemaanalytics.domain.Country
import jfuturedev.cinemaanalytics.domain.Film
import jfuturedev.cinemaanalytics.domain.Genre
import java.time.Month

private val chinaFilms = listOf(
    // 2017
    Film(Country.CHINA, 2017, Month.JANUARY, 1, "T1", "D1", setOf(Genre.DRAMA, Genre.COMEDY), 256779),
    Film(Country.CHINA, 2017, Month.JANUARY, 2, "T2", "D2", setOf(Genre.HISTORY, Genre.SCI_FI), 456908),
    Film(Country.CHINA, 2017, Month.JANUARY, 3, "T3", "D3", setOf(Genre.SCI_FI, Genre.FANTASY), 3456123),
    Film(Country.CHINA, 2017, Month.JANUARY, 4, "T4", "D4", setOf(Genre.SCI_FI, Genre.FANTASY), 625837),
    Film(Country.CHINA, 2017, Month.JANUARY, 5, "", "", setOf(Genre.SCI_FI, Genre.FANTASY, Genre.COMEDY)),
    Film(Country.CHINA, 2017, Month.JANUARY, 6, "", "", setOf(Genre.FANTASY)),
    // 2018
    Film(Country.CHINA, 2018, Month.FEBRUARY, 2, "", "", setOf(Genre.HISTORY, Genre.SCI_FI)),
    Film(Country.CHINA, 2018, Month.FEBRUARY, 3, "", "", setOf(Genre.SCI_FI, Genre.COMEDY)),
    Film(Country.CHINA, 2018, Month.FEBRUARY, 4, "", "", setOf(Genre.SCI_FI, Genre.FANTASY)),
    Film(Country.CHINA, 2018, Month.FEBRUARY, 1, "", "", setOf(Genre.HISTORY, Genre.COMEDY)),
    Film(Country.CHINA, 2018, Month.FEBRUARY, 5, "T5", "D5", setOf(Genre.SCI_FI, Genre.COMEDY), 89635352),
    Film(Country.CHINA, 2018, Month.FEBRUARY, 6, "", "", setOf(Genre.FANTASY)),
    // 2019
    Film(Country.CHINA, 2019, Month.MARCH, 1, "", "", setOf(Genre.FANTASY, Genre.COMEDY)),
    Film(Country.CHINA, 2019, Month.MARCH, 2, "", "", setOf(Genre.FANTASY, Genre.SCI_FI)),
    Film(Country.CHINA, 2019, Month.MARCH, 3, "", "", setOf(Genre.SCI_FI, Genre.COMEDY)),
    Film(Country.CHINA, 2019, Month.MARCH, 4, "", "", setOf(Genre.COMEDY, Genre.DRAMA)),
    Film(Country.CHINA, 2019, Month.MARCH, 5, "", "", setOf(Genre.DRAMA, Genre.FANTASY, Genre.COMEDY)),
    Film(Country.CHINA, 2019, Month.MARCH, 6, "", "", setOf(Genre.DRAMA))
)

private val usaFilms = listOf(
    // 2017
    Film(Country.USA, 2017, Month.JANUARY, 1, "", "", setOf(Genre.HISTORY, Genre.COMEDY)),
    Film(Country.USA, 2017, Month.JANUARY, 2, "", "", setOf(Genre.HISTORY, Genre.SCI_FI)),
    Film(Country.USA, 2017, Month.JANUARY, 3, "T6", "D6", setOf(Genre.COMEDY, Genre.FANTASY), 36548976),
    Film(Country.USA, 2017, Month.JANUARY, 4, "", "", setOf(Genre.COMEDY, Genre.FANTASY)),
    Film(Country.USA, 2017, Month.JANUARY, 5, "", "", setOf(Genre.SCI_FI, Genre.FANTASY, Genre.COMEDY)),
    Film(Country.USA, 2017, Month.JANUARY, 6, "", "", setOf(Genre.HISTORY)),
    // 2018
    Film(Country.USA, 2018, Month.FEBRUARY, 1, "", "", setOf(Genre.DRAMA, Genre.COMEDY)),
    Film(Country.USA, 2018, Month.FEBRUARY, 2, "", "", setOf(Genre.DRAMA, Genre.SCI_FI)),
    Film(Country.USA, 2018, Month.FEBRUARY, 3, "T7", "D7", setOf(Genre.DRAMA, Genre.COMEDY), 976534),
    Film(Country.USA, 2018, Month.FEBRUARY, 4, "", "", setOf(Genre.SCI_FI, Genre.FANTASY)),
    Film(Country.USA, 2018, Month.FEBRUARY, 5, "", "", setOf(Genre.HISTORY, Genre.COMEDY)),
    Film(Country.USA, 2018, Month.FEBRUARY, 6, "", "", setOf(Genre.COMEDY)),
    // 2019
    Film(Country.USA, 2019, Month.MARCH, 1, "", "", setOf(Genre.HISTORY, Genre.COMEDY)),
    Film(Country.USA, 2019, Month.MARCH, 2, "", "", setOf(Genre.HISTORY, Genre.SCI_FI)),
    Film(Country.USA, 2019, Month.MARCH, 3, "", "", setOf(Genre.SCI_FI, Genre.COMEDY)),
    Film(Country.USA, 2019, Month.MARCH, 4, "", "", setOf(Genre.COMEDY, Genre.HISTORY)),
    Film(Country.USA, 2019, Month.MARCH, 5, "", "", setOf(Genre.FANTASY, Genre.SCI_FI)),
    Film(Country.USA, 2019, Month.MARCH, 6, "", "", setOf(Genre.DRAMA))
)

class AnalyticsSpec : StringSpec({
    "display dynamics of film releases" {
        Analytics(chinaFilms, usaFilms)
            .runDynamics(listOf(Genre.COMEDY, Genre.DRAMA, Genre.SCI_FI, Genre.HISTORY, Genre.FANTASY)) shouldBe Report(
            listOf(
                "Comedy, 2017: China - 2 films; USA - 4. Comedy, 2018: China - 3 films; USA - 4. Comedy, 2019: China - 4 films; USA - 3.",
                "Drama, 2017: China - 1 films; USA - 0. Drama, 2018: China - 0 films; USA - 3. Drama, 2019: China - 3 films; USA - 1.",
                "Sci-Fi, 2017: China - 4 films; USA - 2. Sci-Fi, 2018: China - 4 films; USA - 2. Sci-Fi, 2019: China - 2 films; USA - 3.",
                "History, 2017: China - 1 films; USA - 3. History, 2018: China - 2 films; USA - 1. History, 2019: China - 0 films; USA - 3.",
                "Fantasy, 2017: China - 4 films; USA - 3. Fantasy, 2018: China - 2 films; USA - 1. Fantasy, 2019: China - 3 films; USA - 1."
            )
        )
    }

    "display top 5 directors of the highest-rated films" {
        Analytics(chinaFilms, usaFilms).runTopDirectors(5) shouldBe Report(
            listOf(
                "D5 (CHINA) / T5 (FEBRUARY 5, 2018) / $89,635,352 / [Comedy, Sci-Fi]",
                "D6 (USA) / T6 (JANUARY 3, 2017) / $36,548,976 / [Comedy, Fantasy]",
                "D3 (CHINA) / T3 (JANUARY 3, 2017) / $3,456,123 / [Fantasy, Sci-Fi]",
                "D7 (USA) / T7 (FEBRUARY 3, 2018) / $976,534 / [Comedy, Drama]",
                "D4 (CHINA) / T4 (JANUARY 4, 2017) / $625,837 / [Fantasy, Sci-Fi]"
            )
        )
    }
})