package jfuturedev.cinemaanalytics.domain

sealed class Source(open val year: Int)

data class RemoteSource(override val year: Int, val title: String, val revision: String) : Source(year)

data class LocalSource(override val year: Int, val path: String) : Source(year)