package jfuturedev.cinemaanalytics.domain

import java.time.Month

data class Film(
    val year: Int,
    val month: Month,
    val day: Int,
    val title: String,
    val director: String,
    val genres: Set<Genre>,
    val gross: Long = UNKNOWN_GROSS
) {
    companion object {
        const val UNKNOWN_GROSS = 0L
    }
}