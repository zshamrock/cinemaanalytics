package jfuturedev.cinemaanalytics

import jfuturedev.cinemaanalytics.domain.RemoteSource
import jfuturedev.cinemaanalytics.parser.ChinaFilmsParser
import jfuturedev.cinemaanalytics.parser.FilmsParser
import jfuturedev.cinemaanalytics.parser.USAFilmsParser
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class Application {
    companion object {
        private const val SOURCES_SEPARATOR = ";"
        private const val SOURCE_DATA_SEPARATOR = ","
        private const val SOURCE_YEAR_INDEX = 0
        private const val SOURCE_TITLE_INDEX = 1
        private const val SOURCE_REVISION_INDEX = 2

        @JvmStatic
        fun main(args: Array<String>) {
            val environment = Environment()
            val application = Application()
            application.parse(ChinaFilmsParser(environment), environment.getProperty("china.sources"))
            application.parse(USAFilmsParser(environment), environment.getProperty("usa.sources"))
        }
    }

    private fun parse(parser: FilmsParser, sources: String) {
        sources.split(SOURCES_SEPARATOR).map {
            val data = it.split(SOURCE_DATA_SEPARATOR)
            RemoteSource(data[SOURCE_YEAR_INDEX].toInt(), data[SOURCE_TITLE_INDEX], data[SOURCE_REVISION_INDEX])
        }.forEach {
            logger.info { "Parsing source $it" }
            parser.parse(it)
        }
    }
}