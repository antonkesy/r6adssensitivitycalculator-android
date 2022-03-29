package com.poorskill.r6adssensitivitycalculator.settings

import com.poorskill.r6adssensitivitycalculator.ui.settings.Theme

interface Settings {
    fun getAspectRatioPos(): Int
    fun putAspectRatio(newAspectRatio: Int)
    fun getADS(): Int
    fun putADS(newADS: Int)
    fun getFOV(): Int
    fun putFOV(newFOV: Int)
    fun getUsage(): Int
    fun incrementUsage()
    fun getTheme(): Theme
    fun updateLanguage()
}