package com.poorskill.r6adssensitivitycalculator.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.poorskill.r6adssensitivitycalculator.ui.settings.Theme.*
import com.poorskill.r6adssensitivitycalculator.ui.settings.PlayerPreferences

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlayerPreferences.updateLanguage(this)
        when (PlayerPreferences.getApplicationThemePreferences(this)) {
            Light -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Dark -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            System -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    override fun onRestart() {
        recreate()
        super.onRestart()
    }

}
