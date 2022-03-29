package com.poorskill.r6adssensitivitycalculator.calculator

interface SensitivityConverter {
    fun calculateNewAdsSensitivity(oldAds: Int, fov: Int, aspectRatio: Double): IntArray
}