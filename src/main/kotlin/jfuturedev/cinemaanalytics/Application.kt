package jfuturedev.cinemaanalytics

import jfuturedev.cinemaanalytics.domain.RemoteSource
import jfuturedev.cinemaanalytics.domain.Source
import jfuturedev.cinemaanalytics.parser.ChinaFilmsParser
import jfuturedev.cinemaanalytics.parser.FilmsParser
import jfuturedev.cinemaanalytics.parser.USAFilmsParser

class Application {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val environment = Environment()
            val application = Application()
            application.parse(
                ChinaFilmsParser(environment),
                listOf(
                    RemoteSource(2017, "List of Chinese films of 2017", "923009452"),
                    RemoteSource(2018, "List of Chinese films of 2018", "900037799"),
                    RemoteSource(2019, "List of Chinese films of 2019", "924830579")
                )
            )
            application.parse(
                USAFilmsParser(environment),
                listOf(
                    RemoteSource(2017, "List of American films of 2017", "919547236"),
                    RemoteSource(2018, "List of American films of 2018", "924688872"),
                    RemoteSource(2019, "List of American films of 2019", "924869832")
                )
            )
        }
    }

    private fun parse(
        parser: FilmsParser, sources: List<Source>
    ) {
        sources.forEach { parser.parse(it) }
    }
}