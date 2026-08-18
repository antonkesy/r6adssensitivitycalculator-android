package com.poorskill.r6adssensitivitycalculator.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.poorskill.r6adssensitivitycalculator.settings.Settings
import com.poorskill.r6adssensitivitycalculator.settings.UserPreferencesManager
import com.poorskill.r6adssensitivitycalculator.ui.theme.R6Theme
import com.poorskill.r6adssensitivitycalculator.ui.theme.appTheme

/**
 * Loads the stored settings and applies the language, then hands the screen to Compose. Still an
 * AppCompatActivity because that is what backports
 * [androidx.appcompat.app.AppCompatDelegate.setApplicationLocales] below API 33.
 */
open class BaseActivity : AppCompatActivity() {

  protected lateinit var settings: Settings

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    settings = UserPreferencesManager(this)
    settings.updateLanguage()
    appTheme.value = settings.getTheme()
  }

  protected fun setThemedContent(content: @Composable () -> Unit) = setContent {
    val theme by appTheme
    R6Theme(theme) { content() }
  }
}
