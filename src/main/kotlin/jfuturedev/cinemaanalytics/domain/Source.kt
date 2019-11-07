package jfuturedev.cinemaanalytics.domain

sealed class Source(val year: Int)

class RemoteSource(year: Int, val title: String, val revision: String) : Source(year)

class LocalSource(year: Int, val path: String) : Source(year)