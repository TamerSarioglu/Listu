package com.tamersarioglu.listu.presentation.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tamersarioglu.listu.domain.model.topanimemodel.Anime
import com.tamersarioglu.listu.domain.model.topanimemodel.Image
import com.tamersarioglu.listu.domain.model.topanimemodel.ImageSet
import com.tamersarioglu.listu.presentation.ui.theme.ListuTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AnimeCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleAnime = Anime(
        malId = 1,
        url = "https://example.com",
        images = ImageSet(
            jpg = Image(imageUrl = "https://example.com/image.jpg"),
            webp = Image(imageUrl = "https://example.com/image.webp")
        ),
        title = "Test Anime",
        titleEnglish = "Test Anime English",
        type = "TV",
        episodes = 12,
        status = "Finished Airing",
        score = 8.5,
        rank = 100,
        popularity = 50,
        members = 10000,
        favorites = 500
    )

    @Test
    fun animeCard_displaysCorrectTitle() {
        composeTestRule.setContent {
            ListuTheme {
                AnimeCard(
                    anime = sampleAnime,
                    onAnimeClick = { },
                    isFavorite = false,
                    onFavoriteClick = null
                )
            }
        }

        composeTestRule
            .onNodeWithText("Test Anime")
            .assertIsDisplayed()
    }

    @Test
    fun animeCard_displaysEnglishTitle() {
        composeTestRule.setContent {
            ListuTheme {
                AnimeCard(
                    anime = sampleAnime,
                    onAnimeClick = { },
                    isFavorite = false,
                    onFavoriteClick = null
                )
            }
        }

        composeTestRule
            .onNodeWithText("Test Anime English")
            .assertIsDisplayed()
    }

    @Test
    fun animeCard_displaysType() {
        composeTestRule.setContent {
            ListuTheme {
                AnimeCard(
                    anime = sampleAnime,
                    onAnimeClick = { },
                    isFavorite = false,
                    onFavoriteClick = null
                )
            }
        }

        composeTestRule
            .onNodeWithText("TV")
            .assertIsDisplayed()
    }

    @Test
    fun animeCard_displaysEpisodeCount() {
        composeTestRule.setContent {
            ListuTheme {
                AnimeCard(
                    anime = sampleAnime,
                    onAnimeClick = { },
                    isFavorite = false,
                    onFavoriteClick = null
                )
            }
        }

        composeTestRule
            .onNodeWithText("• 12 eps")
            .assertIsDisplayed()
    }

    @Test
    fun animeCard_displaysScore() {
        composeTestRule.setContent {
            ListuTheme {
                AnimeCard(
                    anime = sampleAnime,
                    onAnimeClick = { },
                    isFavorite = false,
                    onFavoriteClick = null
                )
            }
        }

        composeTestRule
            .onNodeWithText("8.5")
            .assertIsDisplayed()
    }

    @Test
    fun animeCard_clickTriggersCallback() {
        var clickedAnimeId: Int? = null

        composeTestRule.setContent {
            ListuTheme {
                AnimeCard(
                    anime = sampleAnime,
                    onAnimeClick = { clickedAnimeId = it },
                    isFavorite = false,
                    onFavoriteClick = null
                )
            }
        }

        composeTestRule
            .onNodeWithText("Test Anime")
            .performClick()

        assert(clickedAnimeId == 1)
    }

    @Test
    fun animeCard_showsFavoriteButton_whenCallbackProvided() {
        composeTestRule.setContent {
            ListuTheme {
                AnimeCard(
                    anime = sampleAnime,
                    onAnimeClick = { },
                    isFavorite = false,
                    onFavoriteClick = { }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Add to favorites")
            .assertIsDisplayed()
    }

    @Test
    fun animeCard_hidesFavoriteButton_whenCallbackNotProvided() {
        composeTestRule.setContent {
            ListuTheme {
                AnimeCard(
                    anime = sampleAnime,
                    onAnimeClick = { },
                    isFavorite = false,
                    onFavoriteClick = null
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Add to favorites")
            .assertDoesNotExist()
    }

    @Test
    fun animeCard_showsFilledHeart_whenFavorited() {
        composeTestRule.setContent {
            ListuTheme {
                AnimeCard(
                    anime = sampleAnime,
                    onAnimeClick = { },
                    isFavorite = true,
                    onFavoriteClick = { }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Remove from favorites")
            .assertIsDisplayed()
    }
}