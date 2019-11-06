package jfuturedev.cinemaanalytics

import jfuturedev.cinemaanalytics.parser.USAFilmsParser

class Application {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
//            val parser = ChinaFilmsParser()
            val parser = USAFilmsParser()
//            val movies = parser.parse(2017, "List of Chinese films of 2017", "923009452")
//            val movies = parser.parse(2018, "List of Chinese films of 2018", "900037799")
//            val movies = parser.parse(2019, "List of Chinese films of 2019", "924830579")
//            val movies = parser.parse(2017, "List of American films of 2017", "919547236")
//            val movies = parser.parse(2018, "List of American films of 2018", "924688872")
            val movies = parser.parse(2019, "List of American films of 2019", "924869832")
            movies.forEach { println(it) }
        }
    }
}