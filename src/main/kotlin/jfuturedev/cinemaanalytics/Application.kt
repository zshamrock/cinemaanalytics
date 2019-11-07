package jfuturedev.cinemaanalytics

import jfuturedev.cinemaanalytics.domain.Film
import jfuturedev.cinemaanalytics.domain.RemoteSource
import jfuturedev.cinemaanalytics.parser.ChinaFilmsParser
import jfuturedev.cinemaanalytics.parser.FilmsParser
import jfuturedev.cinemaanalytics.parser.USAFilmsParser
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import mu.KotlinLogging
import java.nio.charset.StandardCharsets
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

        @JvmStatic
        fun main(args: Array<String>) {
            val application = Application(Environment())
            application.run()
        }
    }

    private val json = Json(JsonConfiguration.Stable.copy(prettyPrint = true))

    private fun run() {
        val action = Action.valueOf(environment.getProperty("analytics.action").toUpperCase(Locale.ROOT))
        when (action) {
            Action.FLUSH -> {
                val base = Paths.get("src", "main", "resources", "data")
                val chinaFilms = parse(ChinaFilmsParser(environment), environment.getProperty("china.sources"))
                flush(chinaFilms, base.resolve("chinafilms.json"))
                val usaFilms = parse(USAFilmsParser(environment), environment.getProperty("usa.sources"))
                flush(usaFilms, base.resolve("usafilms.json"))
            }
            Action.RUN -> {
            }
        }
    }

    private fun flush(films: List<Film>, path: Path) {
        Files.write(
            path,
            json.stringify(Film.serializer().list, films).toByteArray(StandardCharsets.UTF_8),
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE
        )
    }

    private fun parse(parser: FilmsParser, sources: String): List<Film> {
        return sources.split(SOURCES_SEPARATOR).map {
            val data = it.split(SOURCE_DATA_SEPARATOR)
            RemoteSource(data[SOURCE_YEAR_INDEX].toInt(), data[SOURCE_TITLE_INDEX], data[SOURCE_REVISION_INDEX])
        }.flatMap {
            logger.info { "Parsing source $it" }
            parser.parse(it)
        }
    }
}