package com.poorskill.r6adssensitivitycalculator.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
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

  @After fun resetProcessWideTheme() = compose.runOnUiThread { appTheme.value = Theme.System }

  private fun showScreen() {
    compose.setContent { R6Theme(appTheme.value) { SettingsScreen(settings, onBack = {}) } }
  }

  @Test
  fun pickingAThemePersistsItAndRepaintsImmediately() {
    showScreen()

    compose.onAllNodesWithText("System")[0].performClick() // theme row's current value
    compose.onNodeWithText("Black Ice").performClick()

    assertEquals(Theme.BlackIce, settings.getTheme())
    // no Activity.recreate(): the process-wide state is what repaints the open screens
    assertEquals(Theme.BlackIce, appTheme.value)
    compose.onNodeWithText("Black Ice").assertExists()
  }

  @Test
  fun pickingALanguageStoresTheLocaleKey() {
    showScreen()

    compose.onAllNodesWithText("System")[1].performClick() // language row's current value
    compose.onNodeWithText("German").performClick()

    assertEquals("de", settings.getLanguage())
  }

  @Test
  fun showsTheStoredSelections() {
    settings.putTheme(Theme.SkullRain)
    settings.putLanguage("ru")
    compose.runOnUiThread { appTheme.value = Theme.SkullRain }

    showScreen()

    compose.onNodeWithText("Skull Rain").assertExists()
    compose.onNodeWithText("Russian*").assertExists()
  }
}
