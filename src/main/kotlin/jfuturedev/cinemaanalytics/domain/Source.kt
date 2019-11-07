package jfuturedev.cinemaanalytics.domain

import java.nio.file.Path

sealed class Source(open val year: Int)

data class RemoteSource(override val year: Int, val title: String, val revision: String) : Source(year)

data class LocalSource(override val year: Int, val path: String) : Source(year)

data class JsonSource(val path: Path) : Source(0)