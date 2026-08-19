package com.poorskill.r6adssensitivitycalculator.ui.screens

import android.content.ClipboardManager
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.poorskill.r6adssensitivitycalculator.converter.PersistentSensitivityConverter
import com.poorskill.r6adssensitivitycalculator.settings.FakeSettings
import com.poorskill.r6adssensitivitycalculator.ui.Theme
import com.poorskill.r6adssensitivitycalculator.ui.theme.R6Theme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Drives the real MainScreen composable under Robolectric. The screen is composed directly rather
 * than by launching MainActivity, whose onCreate reaches for Play in-app update/review.
 */
@RunWith(AndroidJUnit4::class)
class MainScreenTest {

  @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

  private val settings = FakeSettings()

  private fun showScreen() {
    val converter = PersistentSensitivityConverter(settings)
    compose.setContent { R6Theme(Theme.System) { MainScreen(converter, compose.activity) } }
  }

  @Test
  fun showsEveryScopeAndItsConvertedValue() {
    showScreen()

    // assertExists, not assertIsDisplayed: the lower rows sit below the fold on a phone-sized
    // screen and the content scrolls
    listOf("ADS 1x", "ADS 1.5x", "ADS 2x", "ADS 2.5x", "ADS 3x", "ADS 4x", "ADS 5x", "ADS 12x")
        .forEach { compose.onNodeWithText(it).assertExists() }

    // defaults: ADS 50, FOV 60, 16:9 — same numbers R6Y5S3SensitivityConverterTest pins
    compose.onNodeWithText("33").assertIsDisplayed()
    compose.onNodeWithText("83").assertExists()
  }

  @Test
  fun movingTheAdsSliderUpdatesResultsAndPersists() {
    showScreen()

    sliders()[0].performSemanticsAction(SemanticsActions.SetProgress) { it(100f) }

    compose.onNodeWithText("67").assertIsDisplayed() // ADS 1x at ADS 100 / FOV 60
    compose.onNodeWithText("167").assertExists() // ADS 12x
    assertEquals(100, settings.ads)
  }

  @Test
  fun movingTheFovSliderPersists() {
    showScreen()

    sliders()[1].performSemanticsAction(SemanticsActions.SetProgress) { it(90f) }

    assertEquals(90, settings.fov)
  }

  /** The ADS slider first, then the FOV one — the order they are laid out in. */
  private fun sliders() =
      compose.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsActions.SetProgress))

  @Test
  fun tappingAResultRowCopiesThatValue() {
    showScreen()

    compose.onNodeWithText("ADS 1x").performClick()

    assertEquals("33", currentClipboardText())
  }

  @Test
  fun copyValuesPutsTheWholeSharePayloadOnTheClipboard() {
    showScreen()

    compose.onNodeWithText("Copy Values").performScrollTo().performClick()

    assertEquals(
        """
        ADS 1x = 33
        ADS 1.5x = 53
        ADS 2x = 53
        ADS 2.5x = 54
        ADS 3x = 54
        ADS 4x = 54
        ADS 5x = 54
        ADS 12x = 83
        """
            .trimIndent(),
        currentClipboardText()
    )
  }

  @Test
  fun pickingAnAspectRatioPersistsItsIndex() {
    showScreen()

    compose.onNodeWithText("16:9").performClick()
    compose.onNodeWithText("4:3").performClick()

    assertEquals(2, settings.aspectRatioPos)
  }

  private fun currentClipboardText(): String? {
    val clipboard =
        compose.activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return clipboard.primaryClip?.getItemAt(0)?.text?.toString()
  }
}
