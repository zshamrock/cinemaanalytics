package jfuturedev.cinemaanalytics.parser

import jfuturedev.cinemaanalytics.Environment
import jfuturedev.cinemaanalytics.domain.Country
import kotlinx.serialization.json.Json
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class USAFilmsParser(json: Json, environment: Environment) : FilmsParser(json, environment) {
    companion object {
        private const val RANKING_TITLE_INDEX = 1
        private const val RANKING_GROSS_INDEX = 2
        private const val RANKING_DISTRIBUTOR_INDEX = 2
        private const val DEFAULT_RANKING_DISTRIBUTOR_ROWSPAN = "1"
        private const val TITLE_INDEX = 0
        private const val DIRECTOR_INDEX = 2
        private const val GENRES_INDEX = 3
    }

    private var rankingsRowSpan = 0

    override fun getRankings(document: Document): Elements {
        // There is a small inconsistency between completed year, and current year data, where extra table is added to
        // include the legend of "*" symbol
        return document.select("section[data-mw-section-id=1] table:last-of-type")
    }

    override fun parseRanking(data: Elements): Pair<String, String> {
        var index = 0
        if (rankingsRowSpan == 0) {
            rankingsRowSpan =
                data[RANKING_DISTRIBUTOR_INDEX].attr("rowspan").ifEmpty { DEFAULT_RANKING_DISTRIBUTOR_ROWSPAN }.toInt()
            index++
        }
        rankingsRowSpan--
        // There is a small inconsistency between completed year, and current year data, where "*" in the title denotes
        // that film is still running in cinemas worldwide
        val title = data[RANKING_TITLE_INDEX].text().substringBefore("*")
        return Pair(title, data[RANKING_GROSS_INDEX + index].text())
    }

    override fun getQuarters(document: Document): Elements {
        return document.select(
            "section[data-mw-section-id=2] table, " +
                    "section[data-mw-section-id=3] table, " +
                    "section[data-mw-section-id=4] table, " +
                    "section[data-mw-section-id=5] table"
        )
    }

    override fun getCountry(): Country {
        return Country.USA
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