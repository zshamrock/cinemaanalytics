package jfuturedev.cinemaanalytics.parser

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row
import jfuturedev.cinemaanalytics.domain.Genre
import jfuturedev.cinemaanalytics.domain.Movie
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.time.Month

class USAFilmsParserSpec : StringSpec({
    "parse local file" {
        val movies =
            USAFilmsParser().parse(2017, USAFilmsParserSpec::class.java.getResource("/data/usa-2017.html").path)
        movies.size shouldBe 238
        movies[0] shouldBe Movie(2017, Month.JANUARY, 6, "Underworld: Blood Wars", "Anna Foerster", Genre.ACTION)
        movies[movies.size - 1] shouldBe Movie(
            2017,
            Month.DECEMBER,
            25,
            "Phantom Thread",
            "Paul Thomas Anderson",
            Genre.DRAMA
        )
        movies[94] shouldBe Movie(2017, Month.MAY, 19, "Everything, Everything", "Stella Meghie", Genre.DRAMA)
    }

    "parse director" {
        val parser = USAFilmsParser()
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
})