package com.poorskill.r6adssensitivitycalculator.ui.theme

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.poorskill.r6adssensitivitycalculator.ui.Theme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class R6ThemeTest {

  @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

  @Test
  fun everyThemeRendersAndHasItsOwnBackground() {
    var theme by mutableStateOf(Theme.System)
    val backgrounds = mutableMapOf<Theme, Color>()

    compose.setContent {
      R6Theme(theme) {
        backgrounds[theme] = MaterialTheme.colorScheme.background
        Text("content")
      }
    }

    Theme.entries.forEach { entry ->
      compose.runOnUiThread { theme = entry }
      compose.waitForIdle()
      compose.onNodeWithText("content").assertExists()
    }

    // the six season palettes must not collapse into one another
    val seasons = Theme.entries.filter { it.id >= Theme.BlackIce.id }
    assertEquals(seasons.size, seasons.mapNotNull { backgrounds[it] }.distinct().size)
    assertNotEquals(backgrounds[Theme.Light], backgrounds[Theme.Dark])
  }

  @Test
  fun themeIdsAreStable() {
    // These ids are the strings sitting in SharedPreferences and in R.array.theme_values.
    // Renumbering silently resets everyone's theme.
    assertEquals(
        listOf(0, 1, 2, 3, 4, 5, 6, 7, 8),
        Theme.entries.map { it.id }
    )
    assertTrue(Theme.entries.map { it.id }.distinct().size == Theme.entries.size)
  }
}
