package jfuturedev.cinemaanalytics

import jfuturedev.cinemaanalytics.analytics.Analytics
import jfuturedev.cinemaanalytics.domain.Film
import jfuturedev.cinemaanalytics.domain.Genre
import jfuturedev.cinemaanalytics.domain.JsonSource
import jfuturedev.cinemaanalytics.domain.RemoteSource
import jfuturedev.cinemaanalytics.domain.Source
import jfuturedev.cinemaanalytics.parser.ChinaFilmsParser
import jfuturedev.cinemaanalytics.parser.FilmsParser
import jfuturedev.cinemaanalytics.parser.USAFilmsParser
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import kotlinx.serialization.toUtf8Bytes
import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.Locale

private val logger = KotlinLogging.logger {}

class Application(private val environment: Environment) {
    companion object {
        private const val SOURCES_SEPARATOR = ";"
        private const val SOURCE_DATA_SEPARATOR = ","
        private const val SOURCE_YEAR_INDEX = 0
        private const val SOURCE_TITLE_INDEX = 1
        private const val SOURCE_REVISION_INDEX = 2

        private const val ANALYTICS_ACTION_PROPERTY = "analytics.action"
        private const val ANALYTICS_MODE_PROPERTY = "analytics.mode"
        private const val ANALYTICS_GENRES_PROPERTY = "analytics.genres"
        private const val ANALYTICS_TOP_DIRECTORS_PROPERTY = "analytics.topdirectors"

        private const val CHINA_SOURCES_PROPERTY = "china.sources"
        private const val USA_SOURCES_PROPERTY = "usa.sources"

        @JvmField
        val BASE_DATA_PATH: Path = Paths.get("src", "main", "resources", "data")

        private const val CHINA_FILMS_JSON = "chinafilms.json"
        private const val USA_FILMS_JSON = "usafilms.json"

        @JvmStatic
        fun main(args: Array<String>) {
            val application = Application(Environment())
            application.run()
        }
    }

    private val json = Json(JsonConfiguration.Stable.copy(prettyPrint = true, indent = "  "))

    private fun run() {
        val action = Action.valueOf(environment.getProperty(ANALYTICS_ACTION_PROPERTY).toUpperCase(Locale.ROOT))
        logger.info { "Running $action command" }
        val chinaParser = ChinaFilmsParser(json, environment)
        val chinaRemoteSources = buildRemoteSources(environment.getProperty(CHINA_SOURCES_PROPERTY))
        val usaParser = USAFilmsParser(json, environment)
        val usaRemoteSources = buildRemoteSources(environment.getProperty(USA_SOURCES_PROPERTY))
        when (action) {
            Action.FLUSH -> {
                flush(parse(chinaParser, chinaRemoteSources), BASE_DATA_PATH.resolve(CHINA_FILMS_JSON))
                flush(parse(usaParser, usaRemoteSources), BASE_DATA_PATH.resolve(USA_FILMS_JSON))
            }
            Action.RUN -> {
                val mode = Mode.valueOf(environment.getProperty(ANALYTICS_MODE_PROPERTY).toUpperCase(Locale.ROOT))
                logger.info { "Running in $mode mode" }
                val chinaFilms = when (mode) {
                    Mode.OFFLINE -> {
                        parse(chinaParser, listOf(JsonSource(BASE_DATA_PATH.resolve(CHINA_FILMS_JSON))))
                    }
                    Mode.ONLINE -> {
                        parse(chinaParser, chinaRemoteSources)
                    }
                }
                val usaFilms = when (mode) {
                    Mode.OFFLINE -> {
                        parse(usaParser, listOf(JsonSource(BASE_DATA_PATH.resolve(USA_FILMS_JSON))))
                    }
                    Mode.ONLINE -> {
                        parse(usaParser, usaRemoteSources)
                    }
                }
                val analytics = Analytics(chinaFilms, usaFilms)
                analytics
                    .runDynamics(environment.getProperty(ANALYTICS_GENRES_PROPERTY).split(",").map { Genre.valueOf(it) })
                    .print()
                println("-".repeat(30))
                analytics.runTopDirectors(environment.getProperty(ANALYTICS_TOP_DIRECTORS_PROPERTY).toInt()).print()
            }
        }
    }

    private fun buildRemoteSources(sources: String): List<RemoteSource> {
        return sources.split(SOURCES_SEPARATOR).map {
            val data = it.split(SOURCE_DATA_SEPARATOR)
            RemoteSource(data[SOURCE_YEAR_INDEX].toInt(), data[SOURCE_TITLE_INDEX], data[SOURCE_REVISION_INDEX])
        }
    }

    private fun flush(films: List<Film>, path: Path) {
        Files.write(
            path,
            json.stringify(Film.serializer().list, films).toUtf8Bytes(),
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE
        )
    }

    private fun parse(parser: FilmsParser, sources: List<Source>): List<Film> {
        return sources.flatMap {
            logger.info { "Parsing source $it" }
            parser.parse(it)
        }
    }
}