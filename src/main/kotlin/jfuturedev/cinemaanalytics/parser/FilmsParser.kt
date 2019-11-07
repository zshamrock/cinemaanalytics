package jfuturedev.cinemaanalytics.parser

import jfuturedev.cinemaanalytics.Environment
import jfuturedev.cinemaanalytics.domain.Film
import jfuturedev.cinemaanalytics.domain.Genre
import jfuturedev.cinemaanalytics.domain.JsonSource
import jfuturedev.cinemaanalytics.domain.LocalSource
import jfuturedev.cinemaanalytics.domain.RemoteSource
import jfuturedev.cinemaanalytics.domain.Source
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.time.Month
import java.util.Locale

private val logger = KotlinLogging.logger {}

abstract class FilmsParser(private val json: Json, private val environment: Environment) {
    companion object {
        private const val BASE_ENDPOINT_REST_URL = "https://en.wikipedia.org/api/rest_v1/page/html"
        private const val HEADER = 1
        private const val DEFAULT_ROWSPAN = "1"
        private const val USER_AGENT_EMAIL_PROPERTY_NAME = "useragent.email"
    }

    private fun getDocument(title: String, revision: String): Document {
        val userAgent = environment.getProperty(USER_AGENT_EMAIL_PROPERTY_NAME)
        logger.info { "Using $userAgent as the User-Agent value for the REST API" }
        return Jsoup.connect("$BASE_ENDPOINT_REST_URL/$title/$revision")
            .userAgent(userAgent)
            .get()
    }

    fun parse(source: Source): List<Film> {
        return when (source) {
            is RemoteSource -> {
                parse(source.year, source.title, source.revision)
            }
            is LocalSource -> {
                parse(source.year, source.path)
            }
            is JsonSource -> {
                parseJson(source.path)
            }
        }
    }

    private fun parse(year: Int, path: String): List<Film> {
        return parse(year, Jsoup.parse(File(path), StandardCharsets.UTF_8.name()))
    }

    private fun parse(year: Int, title: String, revision: String): List<Film> {
        return parse(year, getDocument(title, revision))
    }

    private fun parse(year: Int, document: Document): List<Film> {
        val rankings = parseRankings(document)
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
                    val genresLine = getGenres(data, index)
                    val genres = Genre.parse(genresLine)
                    val film = if (genres.isEmpty()) {
                        totalSkipped++
                        logger.warn { "Skip film with unsupported genres $genresLine / $totalSkipped" }
                        null
                    } else {
                        val title = getTitle(data, index)
                        Film(
                            year,
                            Month.valueOf(month),
                            day,
                            title,
                            getDirector(data, index),
                            genres,
                            rankings.getOrDefault(title, Film.UNKNOWN_GROSS)
                        )
                    }
                    openingDayRowSpan--
                    openingMonthRowSpan--
                    film?.let { logger.debug { it } }
                    film
                }
            }
        }
    }

    internal fun parseRankings(document: Document): Map<String, Long> {
        val rankings = getRankings(document)
        // There is a small inconsistency between completed year, and current year data, where extra empty rows are
        // added to to highlight the title which are still in cinemas, so exclude those from parsing
        return rankings.select("tr").drop(HEADER).filterNot { it.hasClass("mw-empty-elt") }.associate { row ->
            val data = row.children()
            val ranking = parseRanking(data)
            Pair(ranking.first, parseGross(ranking.second))
        }
    }

    protected abstract fun parseRanking(data: Elements): Pair<String, String>

    private fun parseGross(gross: String): Long {
        return gross.substring(1).replace(",", "").substringBefore("[").toLong()
    }

    protected abstract fun getRankings(document: Document): Elements

    protected abstract fun getQuarters(document: Document): Elements

    protected abstract fun getTitle(data: Elements, index: Int): String

    internal abstract fun getDirector(data: Elements, index: Int): String

    protected abstract fun getGenres(data: Elements, index: Int): String

    private fun parseJson(path: Path): List<Film> {
        return json.parse(Film.serializer().list, String(Files.readAllBytes(path), StandardCharsets.UTF_8))
    }
}