package jfuturedev.cinemaanalytics

import jfuturedev.cinemaanalytics.parser.ChinaFilmsParser

class Application {
    companion object {
        const val DEBUG = true

        @JvmStatic
        fun main(args: Array<String>) {
            val parser = ChinaFilmsParser()
//            val movies = parser.parse(2017, "List of Chinese films of 2017", "923009452")
//            val movies = parser.parse(2018, "List of Chinese films of 2018", "900037799")
            val movies = parser.parse(2019, "List of Chinese films of 2019", "924830579")
            movies.forEach { println(it) }
        }
    }
}