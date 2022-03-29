package com.poorskill.r6adssensitivitycalculator.converter

interface SensitivityConverter {
    fun calculateNewAdsSensitivity(oldAds: Int, fov: Int, aspectRatio: Double): IntArray
}