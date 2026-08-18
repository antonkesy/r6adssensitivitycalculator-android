package com.poorskill.r6adssensitivitycalculator.settings

import com.poorskill.r6adssensitivitycalculator.ui.Theme

/**
 * In-memory [Settings] for tests. Records what was written so a test can assert that a UI action
 * actually persisted, rather than only that it changed the screen.
 */
class FakeSettings(
    private var ads: Int = 50,
    private var fov: Int = 60,
    private var aspectRatioPos: Int = 0,
    private var usage: Int = 0,
    private var theme: Theme = Theme.System,
    private var language: String = "system"
) : Settings {

  val writes = mutableListOf<Pair<String, Any>>()

  override fun getAspectRatioPos() = aspectRatioPos

  override fun putAspectRatio(newAspectRatio: Int) {
    aspectRatioPos = newAspectRatio
    writes += "aspectRatio" to newAspectRatio
  }

  override fun getADS() = ads

  override fun putADS(newADS: Int) {
    ads = newADS
    writes += "ads" to newADS
  }

  override fun getFOV() = fov

  override fun putFOV(newFOV: Int) {
    fov = newFOV
    writes += "fov" to newFOV
  }

  override fun getUsage() = usage

  override fun incrementUsage() {
    usage++
    writes += "usage" to usage
  }

  override fun getTheme() = theme

  override fun putTheme(theme: Theme) {
    this.theme = theme
    writes += "theme" to theme
  }

  override fun getLanguage() = language

  override fun putLanguage(languageCode: String) {
    language = languageCode
    writes += "language" to languageCode
  }

  /** The real implementation calls AppCompatDelegate; nothing to do off-device. */
  override fun updateLanguage() = Unit
}
