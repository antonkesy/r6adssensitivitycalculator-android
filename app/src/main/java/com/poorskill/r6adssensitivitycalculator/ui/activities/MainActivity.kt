package com.poorskill.r6adssensitivitycalculator.ui.activities

import android.os.Bundle
import com.poorskill.r6adssensitivitycalculator.converter.PersistentSensitivityConverter
import com.poorskill.r6adssensitivitycalculator.services.google.GoogleServices
import com.poorskill.r6adssensitivitycalculator.ui.screens.MainScreen

class MainActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val converter = PersistentSensitivityConverter(settings)

    // Results are live now, so there is no calculate button to count: usage means launches.
    settings.incrementUsage()
    with(GoogleServices(this, settings)) {
      checkInAppUpdate()
      checkInAppReview()
    }

    setThemedContent { MainScreen(converter, this@MainActivity) }
  }
}
