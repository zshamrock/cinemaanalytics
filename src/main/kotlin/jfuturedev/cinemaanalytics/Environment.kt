package jfuturedev.cinemaanalytics

import java.util.*

class Environment {
    companion object {
        private const val DEFAULT_VALUE = ""
    }

    private val properties: Properties = Properties()

    init {
        properties.load(Application::class.java.getResourceAsStream("/application.properties"))
    }

    /**
     * Gets property by name with the following priority order:
     * - system properties
     * - environment variable
     * - application.properties
     */
    fun getProperty(name: String): String {
        return System.getProperty(
            name,
            System.getenv(name.toUpperCase(Locale.ROOT).replace(".", "_")) ?: properties.getProperty(
                name,
                DEFAULT_VALUE
            )
        )
    }
}