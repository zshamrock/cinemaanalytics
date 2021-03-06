package jfuturedev.cinemaanalytics.domain

import kotlinx.serialization.Serializable
import java.time.Month

@Serializable
data class Film(
    val country: Country,
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