package com.poorskill.r6adssensitivitycalculator.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.settings.Settings
import com.poorskill.r6adssensitivitycalculator.settings.UserPreferencesManager
import com.poorskill.r6adssensitivitycalculator.ui.Theme.*

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settings: Settings = UserPreferencesManager(this)
        settings.updateLanguage()
        when (val themePref = settings.getTheme()) {
            Light -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Dark -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            System -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                setTheme(
                    when (themePref) {
                        BlackIce -> R.style.ThemeBlackIce
                        DustLine -> R.style.ThemeDustLine
                        SkullRain -> R.style.ThemeSkullRain
                        VelvetShell -> R.style.ThemeVelvetShell
                        RedCrow -> R.style.ThemeRedCrow
                        Health -> R.style.ThemeHealth
                        else -> R.style.Theme_Base
                    }
                )
            }
        }
    }


    override fun onRestart() {
        recreate()
        super.onRestart()
    }

}
