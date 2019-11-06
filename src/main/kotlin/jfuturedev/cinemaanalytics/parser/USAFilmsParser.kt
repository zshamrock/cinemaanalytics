package jfuturedev.cinemaanalytics.parser

import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class USAFilmsParser : FilmsParser() {
    companion object {
        private const val TITLE_INDEX = 0
        private const val DIRECTOR_INDEX = 2
        private const val GENRES_INDEX = 3
    }

    override fun getQuarters(document: Document): Elements {
        return document.select(
            "section[data-mw-section-id=2] table, " +
                    "section[data-mw-section-id=3] table, " +
                    "section[data-mw-section-id=4] table, " +
                    "section[data-mw-section-id=5] table"
        )
    }

    override fun getTitle(data: Elements, index: Int): String {
        return data[TITLE_INDEX + index].text()
    }

    override fun getDirector(data: Elements, index: Int): String {
        return data[DIRECTOR_INDEX + index].text().substringBefore("(").trim()
    }

    override fun getGenres(data: Elements, index: Int): String {
        return data[GENRES_INDEX + index].text()
    }
}