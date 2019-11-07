package jfuturedev.cinemaanalytics.parser

import jfuturedev.cinemaanalytics.domain.Genre
import jfuturedev.cinemaanalytics.domain.Movie
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.File
import java.nio.charset.StandardCharsets
import java.time.Month
import java.util.*

private val logger = KotlinLogging.logger {}

abstract class FilmsParser {
    companion object {
        private const val BASE_ENDPOINT_REST_URL = "https://en.wikipedia.org/api/rest_v1/page/html"
        private const val HEADER = 1
        private const val DEFAULT_ROWSPAN = "1"
    }

    private fun getDocument(title: String, revision: String): Document {
        return Jsoup.connect("$BASE_ENDPOINT_REST_URL/$title/$revision")
            // TODO: Replace with the provided email address from the application.properties
            .userAgent("")
            .get()
    }

    fun parse(year: Int, path: String): List<Movie> {
        return parse(year, Jsoup.parse(File(path), StandardCharsets.UTF_8.name()))
    }

    fun parse(year: Int, title: String, revision: String): List<Movie> {
        return parse(year, getDocument(title, revision))
    }

    private fun parse(year: Int, document: Document): List<Movie> {
        val quarters = getQuarters(document)
        var openingMonthRowSpan = 0
        var openingDayRowSpan = 0
        var totalSkipped = 0
        var month: String = Month.JANUARY.name
        var day: Int = 1
        return quarters.take(4).map { it.select("tr") }.flatMap { rows ->
            rows.drop(HEADER).mapNotNull { row ->
                val data = row.children()
                if (data.isEmpty()) {
                    null
                } else {
                    var index = 0
                    if (openingMonthRowSpan == 0) {
                        month = data[index].text().replace(" ", "").toUpperCase(Locale.ROOT)
                        logger.debug { "Processing month $month" }
                        openingMonthRowSpan = data[index++].attr("rowspan").toInt()
                        // The data is not always correct, i.e. defining wrong rowspan value different from the actual
                        // number of rows spanned, so we reset days when the new month starts
                        openingDayRowSpan = 0
                    }
                    if (openingDayRowSpan == 0) {
                        day = data[index].text().toInt()
                        logger.debug { "Processing day $day" }
                        openingDayRowSpan = data[index++].attr("rowspan").ifEmpty { DEFAULT_ROWSPAN }.toInt()
                    }
                    val genres = getGenres(data, index)
                    val genre = Genre.parse(genres)
                    val movie = if (genre == null) {
                        totalSkipped++
                        logger.warn { "Skip movie with unsupported genres $genres / $totalSkipped" }
                        null
                    } else {
                        Movie(
                            year,
                            Month.valueOf(month),
                            day,
                            getTitle(data, index),
                            getDirector(data, index),
                            genre
                        )
                    }
                    openingDayRowSpan--
                    openingMonthRowSpan--
                    movie?.let { logger.debug { it } }
                    movie
                }
            }
        }
    }

    abstract fun getQuarters(document: Document): Elements

    abstract fun getTitle(data: Elements, index: Int): String

    abstract fun getDirector(data: Elements, index: Int): String

    abstract fun getGenres(data: Elements, index: Int): String
}