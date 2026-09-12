package com.poorskill.r6adssensitivitycalculator.settings

import com.poorskill.r6adssensitivitycalculator.converter.data.AdsScope
import com.poorskill.r6adssensitivitycalculator.ui.Theme

interface Settings {
  var ads: Int
  var fov: Int
  var aspectRatioPos: Int
  var theme: Theme
  /** Setting this also applies the locale — see [updateLanguage]. */
  var language: String
  /**
   * Which scopes the main screen lists; [AdsScope.DEFAULT_VISIBLE] until changed. Never empty:
   * the last one cannot be switched off.
   */
  var visibleScopes: Set<AdsScope>

  val usage: Int

  fun incrementUsage()

  fun updateLanguage()
}
