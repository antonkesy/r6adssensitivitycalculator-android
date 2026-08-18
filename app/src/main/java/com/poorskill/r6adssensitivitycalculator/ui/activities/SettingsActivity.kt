package com.poorskill.r6adssensitivitycalculator.ui.activities

import android.os.Bundle
import com.poorskill.r6adssensitivitycalculator.ui.screens.SettingsScreen

class SettingsActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setThemedContent {
      SettingsScreen(settings, onBack = { onBackPressedDispatcher.onBackPressed() })
    }
  }
}
