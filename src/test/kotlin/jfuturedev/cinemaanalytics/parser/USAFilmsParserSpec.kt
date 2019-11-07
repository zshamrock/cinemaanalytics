package jfuturedev.cinemaanalytics.parser

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row
import jfuturedev.cinemaanalytics.Environment
import jfuturedev.cinemaanalytics.domain.Film
import jfuturedev.cinemaanalytics.domain.Genre
import jfuturedev.cinemaanalytics.domain.LocalSource
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.File
import java.nio.charset.StandardCharsets
import java.time.Month

private val topFilms = mapOf(
    "Star Wars: The Last Jedi" to 620_181_382L,
    "Beauty and the Beast" to 504_014_165L,
    "Wonder Woman" to 412_563_408L,
    "Jumanji: Welcome to the Jungle" to 404_515_480L,
    "Guardians of the Galaxy Vol. 2" to 389_813_101L,
    "Spider-Man: Homecoming" to 334_201_140L,
    "It" to 327_481_748L,
    "Thor: Ragnarok" to 315_058_289L,
    "Despicable Me 3" to 264_624_300L,
    "Justice League" to 229_024_295L
)

private val dataPath = USAFilmsParserSpec::class.java.getResource("/data/usa-2017.html").path

class USAFilmsParserSpec : StringSpec({
    "parse local file" {
        val films = USAFilmsParser(Json(JsonConfiguration.Stable), Environment()).parse(LocalSource(2017, dataPath))
        films.size shouldBe 238
        films[0] shouldBe Film(
            2017,
            Month.JANUARY,
            6,
            "Underworld: Blood Wars",
            "Anna Foerster",
            setOf(Genre.ACTION, Genre.HORROR)
        )
        films[films.size - 1] shouldBe Film(
            2017,
            Month.DECEMBER,
            25,
            "Phantom Thread",
            "Paul Thomas Anderson",
            setOf(Genre.DRAMA)
        )
        films[94] shouldBe Film(
            2017,
            Month.MAY,
            19,
            "Everything, Everything",
            "Stella Meghie",
            setOf(Genre.DRAMA, Genre.ROMANCE)
        )
        films.sortedByDescending { it.gross }.take(10).associate { Pair(it.title, it.gross) } shouldBe topFilms
    }

    "parse director" {
        val parser = USAFilmsParser(Json(JsonConfiguration.Stable), Environment())
        forall(
            row("Pierre Morel (director); Chad St. John (screenplay); Jennifer Garner, John Ortiz", "Pierre Morel"),
            row(
                "Shane Black (director/screenplay); Fred Dekker (screenplay); Boyd Holbrook, Trevante Rhodes",
                "Shane Black"
            ),
            row(
                "Yann Demange (director); Logan Miller, Steve Kloves, Noah Miller, Andy Weiss (screenplay); Matthew McConaughey",
                "Yann Demange"
            ),
            row(
                "Yann Demange (director); Logan Miller, Steve Kloves, Noah Miller, Andy Weiss (screenplay); Matthew McConaughey",
                "Yann Demange"
            ),
            row("Michael Moore (director/screenplay)", "Michael Moore")
        ) { crew, director ->
            parser.getDirector(
                Elements(listOf(Element("td"), Element("td"), Element("td").text(crew))),
                0
            ) shouldBe director
        }
    }

    "parse rankings" {
        val parser = USAFilmsParser(Json(JsonConfiguration.Stable), Environment())
        val document = Jsoup.parse(File(dataPath), StandardCharsets.UTF_8.name())
        val rankings = parser.parseRankings(document)
        rankings shouldBe topFilms
    }
})