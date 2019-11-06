package jfuturedev.cinemaanalytics.parser

import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class ChinaFilmsParser : FilmsParser() {
    companion object {
        private const val TITLE_INDEX = 0
        private const val DIRECTOR_INDEX = 1
        private const val GENRES_INDEX = 3
    }

    override fun getQuarters(document: Document): Elements {
        return document.select("section[data-mw-section-id=2] table")
    }

    override fun getTitle(data: Elements, index: Int): String {
        return data[TITLE_INDEX + index].text()
    }

    override fun getDirector(data: Elements, index: Int): String {
        return data[DIRECTOR_INDEX + index].text()
    }

    override fun getGenres(data: Elements, index: Int): String {
        return data[GENRES_INDEX + index].text()
    }
}