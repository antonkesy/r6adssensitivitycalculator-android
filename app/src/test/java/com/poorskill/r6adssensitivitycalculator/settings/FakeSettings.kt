package com.poorskill.r6adssensitivitycalculator.settings

import com.poorskill.r6adssensitivitycalculator.ui.Theme

/**
 * In-memory [Settings] for tests. Records what was written so a test can assert that a UI action
 * actually persisted, rather than only that it changed the screen.
 */
class FakeSettings(
    ads: Int = 50,
    fov: Int = 60,
    aspectRatioPos: Int = 0,
    usage: Int = 0,
    theme: Theme = Theme.System,
    language: String = "system"
) : Settings {

  val writes = mutableListOf<Pair<String, Any>>()

  override var ads: Int = ads
    set(value) {
      field = value
      writes += "ads" to value
    }

  override var fov: Int = fov
    set(value) {
      field = value
      writes += "fov" to value
    }

  override var aspectRatioPos: Int = aspectRatioPos
    set(value) {
      field = value
      writes += "aspectRatio" to value
    }

  override var theme: Theme = theme
    set(value) {
      field = value
      writes += "theme" to value
    }

  override var language: String = language
    set(value) {
      field = value
      writes += "language" to value
    }

  override var usage: Int = usage
    private set

  override fun incrementUsage() {
    usage++
    writes += "usage" to usage
  }

  /** The real implementation calls AppCompatDelegate; nothing to do off-device. */
  override fun updateLanguage() = Unit
}
