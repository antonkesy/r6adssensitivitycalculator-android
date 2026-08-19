package com.poorskill.r6adssensitivitycalculator.settings

import com.poorskill.r6adssensitivitycalculator.ui.Theme

interface Settings {
  var ads: Int
  var fov: Int
  var aspectRatioPos: Int
  var theme: Theme
  /** Setting this also applies the locale — see [updateLanguage]. */
  var language: String

  val usage: Int

  fun incrementUsage()

  fun updateLanguage()
}
