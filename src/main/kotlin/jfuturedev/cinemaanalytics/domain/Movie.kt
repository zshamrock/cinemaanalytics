package jfuturedev.cinemaanalytics.domain

import java.time.Month

data class Movie(
    val year: Int,
    val month: Month,
    val day: Int,
    val title: String,
    val director: String,
    val genre: Genre
)