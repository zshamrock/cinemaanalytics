# cinemaanalytics [![Build Status](https://travis-ci.org/zshamrock/cinemaanalytics.svg?branch=master)](https://travis-ci.org/zshamrock/cinemaanalytics) 

JFuture 2019 Cinema Analytics System Challenge

## Challenge

Imagine that you're a developer working on Cinema Analytics System, who needs to collect data and make calculations on the following items:
 
1. Display the dynamics of film releases in China and the United States over the past 3 years. Please take into account 5 different film genres.
 
   Example:
   Fiction, 2017: China — 23 films; USA — 42. Fiction, 2018: China — 10 films; USA — 22.
 
2. Make the list of top 5 Directors of the highest-rated films.

## Assumptions

1. The data is collected for 2019, 2018 and 2017 years, as 2019 is almost over.
Due to automatic data parsing and loading it is easy to add support for 2016 year instead of 2019 by using the corresponding 
link to the Wikipedia page.

2. When setting the gross (in USD) value for the film the mapping is done by the film's title, so there is the potential 
chance of duplicate films with the same title per year per country.

3. The actual output format for the "top 5 Directors of the highest-rated films" has not been explicitly specified, so 
it is set to "Director (Country) / Title / Gross (in USD)".

4. Criteria for choosing the highest-rated films is based on the gross (in USD) value of the film.

## Information gathering and calculation

- Data for the films is taken from Wikipedia pages using [Wikipedia REST API](https://en.wikipedia.org/api/rest_v1/).

- For each of the page the corresponding [GET /page/title/{title}](https://en.wikipedia.org/api/rest_v1/#/Page%20content/get_page_title__title_)
has been called manually to locate revision value (`rev` in the response), so the data is "frozen" and parsing can rely on known page HTML format.

- Each page is then programmatically fetched using Wikipedia REST API and parsed for the corresponding data, both films 
and rankings (gross value in USD). HTML parsing is done using [jsoup](https://jsoup.org/) Java library.

- Data is "flushed" (see configuration parameter below) using `analytics.action=flush` option when running into the local
JSON data files. Which are then used for the analytics queries, unless `analytics.mode=online` when local data is ignored, 
and the data is fetched from Wikipedia.

- There are subtle differences in both China and USA Wikipedia films pages, so there are different parsers for each of the countries.

- In addition, there are subtle differences in parsing current 2019 year data, and passed years, like 2018 and 2017, this is
also reflected in the parser implementations.

Here is the list of the Wikipedia pages used to fetch and parse the data (these are also controlled by `china.sources` and `usa.sources` 
configuration parameters described below):

- [List of Chinese films of 2017](https://en.wikipedia.org/wiki/List_of_Chinese_films_of_2017)
- [List of Chinese films of 2018](https://en.wikipedia.org/wiki/List_of_Chinese_films_of_2018)
- [List of Chinese films of 2019](https://en.wikipedia.org/wiki/List_of_Chinese_films_of_2019)
- [List of American films of 2017](https://en.wikipedia.org/wiki/List_of_American_films_of_2017)
- [List of American films of 2018](https://en.wikipedia.org/wiki/List_of_American_films_of_2018)
- [List of American films of 2019](https://en.wikipedia.org/wiki/List_of_American_films_of_2019)

## Configuration

The application supports a few configuration parameters:

|parameter| values | default | description |
|---------|--------|---------|-------------|
|`analytics.action`| `flush` - (it then ignores the `analytics.mode` property, i.e. it is always run in online mode)|`run`|action to run|
|                  | `run` - (run actual analytics)                                                                |     |
|`analytics.mode`  | `online` - (fetches the data from the Wikipedia REST API)| `offline` | either read local data or remote |              
|                  | `offline` - (reads the data from the locally stored JSON data files)|  |                                  |
|`analytics.genres`| list of genres (from `Genre`enum separated by comma) | `COMEDY,DRAMA,SCI_FI,HISTORY,FANTASY`| genres to use in the dynamics of film releases analytics |
|`analytics.topdirectors`| number | 5 | number of top directors |
|`useragent.email`| email of user running the analytics in the `online` mode | `jug@jprof.by` | Wikipedia REST API requires to set the contact information in the `User-Agent` header |
|`china.sources`| list of Wikipedia pages to retrieve data from | see `application.properties` | list of china films pages in Wikipedia |
|`usa.sources`| list of Wikipedia pages to retrieve data from | see `application.properties` | list of usa films pages in Wikipedia |

*Note*: if you are using `analytics.mode=online` be sure you change the value of the `useragent.email` unless you want to use the default value!

To set the parameter on `run` either (in the priority order):

- set JVM system property by adding `-D` for the running command, ex.: `gradle -Danalytics.action=flush run` 
or `gradle -Danalytics.mode=online -Danalytics.genres=DRAMA,COMEDY,NATURE run`, etc.
- OS specific environment variable (in that case upper case the property name and replace `.` with `_`, 
i.e. `analytics.action` becomes `ANALYTICS_ACTION`
- change the value directly in the `application.properties`
          
## Running

```
gradle -q run
```

It assumes you have Gradle installed. Otherwise it is easy to install it with [SDKMAN](https://sdkman.io/): `sdk install gradle`.

## Output

```
Comedy, 2017: China - 99 films; USA - 81. Comedy, 2018: China - 100 films; USA - 68. Comedy, 2019: China - 29 films; USA - 75.
Drama, 2017: China - 146 films; USA - 109. Drama, 2018: China - 169 films; USA - 117. Drama, 2019: China - 51 films; USA - 102.
Sci-Fi, 2017: China - 5 films; USA - 30. Sci-Fi, 2018: China - 5 films; USA - 25. Sci-Fi, 2019: China - 4 films; USA - 21.
History, 2017: China - 12 films; USA - 3. History, 2018: China - 4 films; USA - 4. History, 2019: China - 3 films; USA - 1.
Fantasy, 2017: China - 34 films; USA - 19. Fantasy, 2018: China - 32 films; USA - 20. Fantasy, 2019: China - 10 films; USA - 20.
------------------------------
Anthony and Joe Russo (USA) / Avengers: Endgame / $858,373,000
Wu Jing (CHINA) / Wolf Warriors 2 / $854,248,869
Yu Yang (CHINA) / Nezha / $710,400,000
Ryan Coogler (USA) / Black Panther / $700,059,566
Anthony Russo and Joe Russo (USA) / Avengers: Infinity War / $678,815,482
```

## License

MIT License.