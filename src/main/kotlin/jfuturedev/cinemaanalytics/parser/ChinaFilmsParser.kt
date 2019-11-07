package jfuturedev.cinemaanalytics.parser

import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class ChinaFilmsParser : FilmsParser() {
    companion object {
        private const val RANKING_TITLE_INDEX = 1
        private const val RANKING_GROSS_INDEX = 2
        private const val TITLE_INDEX = 0
        private const val DIRECTOR_INDEX = 1
        private const val GENRES_INDEX = 3
    }

    override fun getRankings(document: Document): Elements {
        return document.select("section[data-mw-section-id=1] table")
    }

    override fun parseRanking(data: Elements): Pair<String, String> {
        return Pair(data[RANKING_TITLE_INDEX].text(), data[RANKING_GROSS_INDEX].text())
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