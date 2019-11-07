package jfuturedev.cinemaanalytics.parser

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import jfuturedev.cinemaanalytics.domain.Film
import jfuturedev.cinemaanalytics.domain.Genre
import jfuturedev.cinemaanalytics.domain.LocalSource
import org.jsoup.Jsoup
import java.io.File
import java.nio.charset.StandardCharsets
import java.time.Month

private val topFilms = mapOf(
    "Operation Red Sea" to 607_100_000L,
    "Detective Chinatown 2" to 563_900_000L,
    "Dying to Survive" to 438_400_000L,
    "Monster Hunt 2" to 370_300_000L,
    "Hello Mr. Billionaire" to 363_000_000L,
    "Us and Them" to 225_500_000L,
    "The Island" to 192_700_000L,
    "Project Gutenberg" to 181_400_000L,
    "The Meg" to 149_100_000L,
    "How Long Will I Love U" to 127_000_000L
)

private val dataPath = ChinaFilmsParserSpec::class.java.getResource("/data/china-2018.html").path

class ChinaFilmsParserSpec : StringSpec({
    "parse local file" {
        val films = ChinaFilmsParser().parse(LocalSource(2018, dataPath))
        films.size shouldBe 367
        films[0] shouldBe Film(2018, Month.JANUARY, 5, "Come On Teacher", "Wu Shengji", Genre.DRAMA)
        films[films.size - 1] shouldBe Film(
            2018,
            Month.DECEMBER,
            31,
            "Long Day's Journey into Night",
            "Bi Gan",
            Genre.DRAMA
        )
        films[94] shouldBe Film(2018, Month.MAY, 4, "Hong Kong Rescue", "Liu Yijun", Genre.ACTION)
        films.sortedByDescending { it.gross }.take(10).associate { Pair(it.title, it.gross) } shouldBe topFilms
    }

    "parse rankings" {
        val parser = ChinaFilmsParser()
        val document = Jsoup.parse(File(dataPath), StandardCharsets.UTF_8.name())
        val rankings = parser.parseRankings(document)
        rankings shouldBe topFilms
    }
})