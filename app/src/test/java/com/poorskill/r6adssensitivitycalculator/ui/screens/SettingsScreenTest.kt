package com.poorskill.r6adssensitivitycalculator.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.poorskill.r6adssensitivitycalculator.converter.data.AdsScope
import com.poorskill.r6adssensitivitycalculator.settings.FakeSettings
import com.poorskill.r6adssensitivitycalculator.ui.Theme
import com.poorskill.r6adssensitivitycalculator.ui.theme.R6Theme
import com.poorskill.r6adssensitivitycalculator.ui.theme.appTheme
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {

  @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

  private val settings = FakeSettings()

  @After
  fun resetProcessWideState() =
      compose.runOnUiThread {
        appTheme.value = Theme.System
        visibleScopes.value = AdsScope.entries.toSet()
      }

  /**
   * Nodes inside the picker's dialog window live in a second Compose root; Robolectric does not
   * route injected touches there, so tap them through their click action instead.
   */
  private fun SemanticsNodeInteraction.tap() = performSemanticsAction(SemanticsActions.OnClick)

  private fun showScreen() {
    compose.setContent { R6Theme(appTheme.value) { SettingsScreen(settings, onBack = {}) } }
  }

  @Test
  fun pickingAThemePersistsItAndRepaintsImmediately() {
    showScreen()

    compose.onAllNodesWithText("System")[0].performClick() // theme row's current value
    compose.onNodeWithText("Black Ice").performClick()

    assertEquals(Theme.BlackIce, settings.theme)
    // no Activity.recreate(): the process-wide state is what repaints the open screens
    assertEquals(Theme.BlackIce, appTheme.value)
    compose.onNodeWithText("Black Ice").assertExists()
  }

  @Test
  fun pickingALanguageStoresTheLocaleKey() {
    showScreen()

    compose.onAllNodesWithText("System")[1].performClick() // language row's current value
    compose.onNodeWithText("German").performClick()

    assertEquals("de", settings.language)
  }

  @Test
  fun showsTheStoredSelections() {
    settings.theme = Theme.SkullRain
    settings.language = "ru"
    compose.runOnUiThread { appTheme.value = Theme.SkullRain }

    showScreen()

    compose.onNodeWithText("Skull Rain").assertExists()
    compose.onNodeWithText("Russian*").assertExists()
  }

  @Test
  fun togglingAScopeInThePickerPersistsItAndUpdatesTheMainScreenState() {
    showScreen()

    compose.onNodeWithText("9 / 9").performClick() // opens the picker
    compose.onNodeWithText("ADS 8x").assertIsOn().tap()

    val expected = AdsScope.entries.toSet() - AdsScope.X8
    assertEquals(expected, settings.visibleScopes)
    assertEquals(listOf<Pair<String, Any>>("visibleScopes" to expected), settings.writes)
    // no Activity.recreate(): the open main screen reads the same process-wide state
    assertEquals(expected, visibleScopes.value)
    compose.onNodeWithText("ADS 8x").assertIsOff()

    compose.onNodeWithText("ADS 8x").tap()

    assertEquals(AdsScope.entries.toSet(), settings.visibleScopes)
    compose.onNodeWithText("ADS 8x").assertIsOn()
  }

  @Test
  fun theRowShowsHowManyScopesAreOnAndTheScopesOnlyAppearInsideThePicker() {
    compose.runOnUiThread { visibleScopes.value = AdsScope.entries.toSet() - AdsScope.X8 }
    showScreen()

    compose.onNodeWithText("ADS 8x").assertDoesNotExist()
    compose.onNodeWithText("8 / 9").performClick()
    compose.onNodeWithText("ADS 8x").assertIsOff()

    compose.onNodeWithText("OK").tap()
    compose.onNodeWithText("ADS 8x").assertDoesNotExist()
  }

  @Test
  fun theLastVisibleScopeCannotBeTurnedOff() {
    compose.runOnUiThread { visibleScopes.value = setOf(AdsScope.X1) }
    showScreen()

    compose.onNodeWithText("1 / 9").performClick()
    compose.onNodeWithText("ADS 1x").assertIsOn().assertIsNotEnabled()
    compose.onNodeWithText("ADS 12x").assertIsOff()
  }
}
