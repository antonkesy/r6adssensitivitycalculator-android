package com.poorskill.r6adssensitivitycalculator.ui.activities

import android.os.Bundle
import com.poorskill.r6adssensitivitycalculator.ui.screens.AboutScreen

class AboutActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setThemedContent {
      AboutScreen(this@AboutActivity, onBack = { onBackPressedDispatcher.onBackPressed() })
    }
  }
}
