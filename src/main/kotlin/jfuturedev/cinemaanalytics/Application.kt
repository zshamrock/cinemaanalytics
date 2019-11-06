package jfuturedev.cinemaanalytics

import jfuturedev.cinemaanalytics.parser.ChinaFilmsParser

class Application {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // "List of Chinese films of 2017", "923009452"
            // "List of Chinese films of 2018", "900037799"
            //
            val movies = ChinaFilmsParser().parse(2018, "List of Chinese films of 2018", "900037799")
            movies.forEach { println(it) }
        }
    }
}