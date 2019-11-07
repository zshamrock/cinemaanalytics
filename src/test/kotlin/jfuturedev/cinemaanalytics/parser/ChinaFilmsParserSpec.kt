package jfuturedev.cinemaanalytics.parser

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import jfuturedev.cinemaanalytics.domain.Genre
import jfuturedev.cinemaanalytics.domain.Movie
import java.time.Month

class ChinaFilmsParserSpec : StringSpec({
    "parse local file" {
        val movies =
            ChinaFilmsParser().parse(2018, ChinaFilmsParserSpec::class.java.getResource("/data/china-2018.html").path)
        movies.size shouldBe 367
        movies[0] shouldBe Movie(2018, Month.JANUARY, 5, "Come On Teacher", "Wu Shengji", Genre.DRAMA)
        movies[movies.size - 1] shouldBe Movie(
            2018,
            Month.DECEMBER,
            31,
            "Long Day's Journey into Night",
            "Bi Gan",
            Genre.DRAMA
        )
        movies[94] shouldBe Movie(2018, Month.MAY, 4, "Hong Kong Rescue", "Liu Yijun", Genre.ACTION)
    }
})