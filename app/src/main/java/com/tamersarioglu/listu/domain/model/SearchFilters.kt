package com.tamersarioglu.listu.domain.model

data class SearchFilters(
    val query: String = "",
    val type: AnimeType? = null,
    val status: AnimeStatus? = null,
    val rating: AgeRating? = null,
    val genre: Genre? = null,
    val studio: Studio? = null,
    val year: Int? = null,
    val season: AnimeSeason? = null,
    val minScore: Double? = null,
    val maxScore: Double? = null,
    val minEpisodes: Int? = null,
    val maxEpisodes: Int? = null,
    val sortBy: SortOption = SortOption.SCORE,
    val sortOrder: SortOrder = SortOrder.DESCENDING
)

enum class AnimeType(val displayName: String, val apiValue: String) {
    TV("TV Series", "tv"),
    MOVIE("Movie", "movie"),
    OVA("OVA", "ova"),
    SPECIAL("Special", "special"),
    ONA("ONA", "ona"),
    MUSIC("Music", "music")
}

enum class AnimeStatus(val displayName: String, val apiValue: String) {
    AIRING("Currently Airing", "airing"),
    COMPLETE("Completed", "complete"),
    UPCOMING("Upcoming", "upcoming")
}

enum class AgeRating(val displayName: String, val apiValue: String) {
    G("G - All Ages", "g"),
    PG("PG - Children", "pg"),
    PG13("PG-13 - Teens 13+", "pg13"),
    R17("R - 17+ (violence & profanity)", "r17"),
    R_PLUS("R+ - Mild Nudity", "r"),
    RX("Rx - Hentai", "rx")
}

enum class AnimeSeason(val displayName: String, val apiValue: String) {
    WINTER("Winter", "winter"),
    SPRING("Spring", "spring"),
    SUMMER("Summer", "summer"),
    FALL("Fall", "fall")
}

enum class SortOption(val displayName: String, val apiValue: String) {
    TITLE("Title", "title"),
    TYPE("Type", "type"),
    RATING("Rating", "rating"),
    START_DATE("Start Date", "start_date"),
    END_DATE("End Date", "end_date"),
    EPISODES("Episodes", "episodes"),
    SCORE("Score", "score"),
    SCORED_BY("Scored By", "scored_by"),
    RANK("Rank", "rank"),
    POPULARITY("Popularity", "popularity"),
    MEMBERS("Members", "members"),
    FAVORITES("Favorites", "favorites")
}

enum class SortOrder(val displayName: String, val apiValue: String) {
    ASCENDING("Ascending", "asc"),
    DESCENDING("Descending", "desc")
}

// Predefined popular genres
data class Genre(
    val malId: Int,
    val name: String,
    val url: String = ""
) {
    companion object {
        val POPULAR_GENRES = listOf(
            Genre(1, "Action"),
            Genre(2, "Adventure"),
            Genre(4, "Comedy"),
            Genre(8, "Drama"),
            Genre(10, "Fantasy"),
            Genre(14, "Horror"),
            Genre(7, "Mystery"),
            Genre(22, "Romance"),
            Genre(24, "Sci-Fi"),
            Genre(27, "Shounen"),
            Genre(25, "Shoujo"),
            Genre(41, "Seinen"),
            Genre(43, "Josei"),
            Genre(36, "Slice of Life"),
            Genre(30, "Sports"),
            Genre(37, "Supernatural"),
            Genre(38, "Military"),
            Genre(40, "Psychological")
        )
    }
}

// Predefined popular studios
data class Studio(
    val malId: Int,
    val name: String,
    val url: String = ""
) {
    companion object {
        val POPULAR_STUDIOS = listOf(
            Studio(1, "Studio Pierrot"),
            Studio(11, "Madhouse"),
            Studio(18, "Toei Animation"),
            Studio(23, "Sunrise"),
            Studio(44, "Shaft"),
            Studio(56, "A-1 Pictures"),
            Studio(569, "MAPPA"),
            Studio(858, "Wit Studio"),
            Studio(112, "Gainax"),
            Studio(14, "Sunrise Beyond"),
            Studio(31, "Gallop"),
            Studio(6, "Gainax"),
            Studio(10, "Production I.G"),
            Studio(28, "OLM"),
            Studio(48, "Studio Deen")
        )
    }
}