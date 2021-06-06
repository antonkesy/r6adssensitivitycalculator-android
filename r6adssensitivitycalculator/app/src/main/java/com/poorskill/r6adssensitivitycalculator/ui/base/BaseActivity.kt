package com.poorskill.r6adssensitivitycalculator.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.ui.settings.Theme.*
import com.poorskill.r6adssensitivitycalculator.ui.settings.UserPreferencesManager

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserPreferencesManager.updateLanguage(this)
        when (val themePref = UserPreferencesManager.getApplicationTheme(this)) {
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
