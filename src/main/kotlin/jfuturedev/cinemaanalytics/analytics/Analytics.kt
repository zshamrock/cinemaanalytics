package jfuturedev.cinemaanalytics.analytics

import jfuturedev.cinemaanalytics.domain.Film
import jfuturedev.cinemaanalytics.domain.Genre
import java.text.NumberFormat
import java.util.Locale

class Analytics(private val chinaFilms: List<Film>, private val usaFilms: List<Film>) {
    companion object {
        @JvmField
        val US_NUMBER_FORMAT: NumberFormat = NumberFormat.getNumberInstance(Locale.US)
    }

    fun runDynamics(genres: List<Genre>): Report {
        val chinaGroup = groupByYear(chinaFilms)
        val usaGroup = groupByYear(usaFilms)
        return Report(genres.map { genre ->
            countByGenre(chinaGroup, genre).zip(countByGenre(usaGroup, genre)).joinToString(separator = " ") { pair ->
                "${genre.capitalize()}, ${pair.first.first}: China - ${pair.first.second} films; USA - ${pair.second.second}."
            }
        })
    }

    private fun groupByYear(films: List<Film>) = films.groupBy { it.year }

    private fun countByGenre(group: Map<Int, List<Film>>, genre: Genre) =
        group.mapValues { entry -> entry.value.count { it.genres.contains(genre) } }.toList()

    fun runTopDirectors(top: Int): Report {
        val films = (chinaFilms + usaFilms).sortedByDescending { it.gross }.take(top)
        return Report(films.map {
            val gross = US_NUMBER_FORMAT.format(it.gross)
            "${it.director} (${it.country}) / ${it.title} (${it.month} ${it.day}, ${it.year}) / $$gross"
        })
    }
}

data class Report(private val lines: List<String>) {
    fun print() {
        lines.forEach { println(it) }
    }
}