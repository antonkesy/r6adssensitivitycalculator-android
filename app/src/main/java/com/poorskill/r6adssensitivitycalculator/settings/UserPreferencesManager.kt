package com.poorskill.r6adssensitivitycalculator.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import androidx.preference.PreferenceManager
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.ui.Theme

class UserPreferencesManager(private val context: Context) : Settings {

  private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

  private val themeKey
    get() = context.getString(R.string.prefApplicationThemePrefKey)

  private val languageKey
    get() = context.getString(R.string.prefApplicationLanguagePrefKey)

  override var ads: Int
    get() = prefs.getInt(PREF_ADS, 50)
    set(value) = prefs.edit { putInt(PREF_ADS, value) }

  override var fov: Int
    get() = prefs.getInt(PREF_FOV, 60)
    set(value) = prefs.edit { putInt(PREF_FOV, value) }

  override var aspectRatioPos: Int
    get() = prefs.getInt(PREF_ASPECT_RATIO, 0)
    set(value) = prefs.edit { putInt(PREF_ASPECT_RATIO, value) }

  override val usage: Int
    get() = prefs.getInt(PREF_USAGE, 0)

  override fun incrementUsage() = prefs.edit { putInt(PREF_USAGE, usage + 1) }

  override var theme: Theme
    get() {
      val id = prefs.getString(themeKey, "0")?.toIntOrNull() ?: 0
      return Theme.entries.find { it.id == id } ?: Theme.System
    }
    // stored as a string id, unchanged from the ListPreference days
    set(value) = prefs.edit { putString(themeKey, value.id.toString()) }

  override var language: String
    get() = prefs.getString(languageKey, SYSTEM_LANGUAGE) ?: SYSTEM_LANGUAGE
    set(value) {
      prefs.edit { putString(languageKey, value) }
      updateLanguage()
    }

  /**
   * Applies the stored language. AppCompat persists and re-applies this itself and recreates the
   * running activities, so there is nothing to do beyond handing it the tag.
   */
  override fun updateLanguage() {
    val locales =
        if (language == SYSTEM_LANGUAGE) LocaleListCompat.getEmptyLocaleList()
        else LocaleListCompat.forLanguageTags(language)
    if (locales != AppCompatDelegate.getApplicationLocales()) {
      AppCompatDelegate.setApplicationLocales(locales)
    }
  }

  companion object {
    const val SYSTEM_LANGUAGE = "system"

    private const val PREF_ADS = "adsKey"
    private const val PREF_FOV = "fovKey"
    private const val PREF_ASPECT_RATIO = "aspKey"
    private const val PREF_USAGE = "useKey"
  }
}
