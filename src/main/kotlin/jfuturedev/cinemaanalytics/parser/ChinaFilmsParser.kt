package jfuturedev.cinemaanalytics.parser

import jfuturedev.cinemaanalytics.domain.Genre
import jfuturedev.cinemaanalytics.domain.Movie
import org.jsoup.Jsoup
import java.time.Month

class ChinaFilmsParser {
    companion object {
        private const val HEADER = 1
        private const val TITLE_INDEX = 0
        private const val DIRECTOR_INDEX = 1
        private const val GENRES_INDEX = 3
    }

    fun parse(year: Int, title: String, revision: String): List<Movie> {
        val document = Jsoup.connect("https://en.wikipedia.org/api/rest_v1/page/html/$title/$revision")
            // TODO: Replace with the provided email address from the application.properties
            .userAgent("")
            .get()
        val quarters = document.select("section[data-mw-section-id=2] table")
        var openingMonthRowSpan = 0
        var openingDayRowSpan = 0
        var totalSkipped = 0
        var month: String = Month.JANUARY.name
        var day: Int = 1
        return quarters.map { it.select("tr") }.flatMap { rows ->
            rows.drop(HEADER).mapNotNull { row ->
                val data = row.children()
                var index = 0
                if (openingMonthRowSpan == 0) {
                    month = data[index].text().replace(" ", "")
                    openingMonthRowSpan = data[index++].attr("rowspan").toInt()
                }
                if (openingDayRowSpan == 0) {
                    day = data[index].text().toInt()
                    openingDayRowSpan = data[index++].attr("rowspan").toInt()
                }
                val genres = data[GENRES_INDEX + index].text()
                val genre = Genre.parse(genres)
                val movie = if (genre == null) {
                    totalSkipped++
                    println("Skip movie with unsupported genres $genres / $totalSkipped")
                    null
                } else {
                    Movie(
                        year,
                        Month.valueOf(month),
                        day,
                        data[TITLE_INDEX + index].text(),
                        data[DIRECTOR_INDEX + index].text(),
                        genre
                    )
                }
                openingDayRowSpan--
                openingMonthRowSpan--
                movie
            }
        }
    }
}