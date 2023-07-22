package com.poorskill.r6adssensitivitycalculator.converter

import kotlin.math.*

class R6Y5S3SensitivityConverter : SensitivityConverter {
    private val fovMultiplier = doubleArrayOf(0.9, 0.59, 0.49, 0.42, 0.35, 0.3, 0.22, 0.092)
    private val adsMultiplier = doubleArrayOf(0.6, 0.59, 0.49, 0.42, 0.35, 0.3, 0.22, 0.14)

    override fun calculateNewAdsSensitivity(oldAds: Int, fov: Int, aspectRatio: Double): SensitivityConverter.Sensitivity {
        val result = IntArray(8)
        val horizontalFOV = calculateHorizontalFOV(fov.toDouble(), aspectRatio)
        val verticalFOV = if (horizontalFOV > 150) calculateVerticalFOV(aspectRatio) else fov.toDouble()

        for (i in result.indices)
            result[i] = calculateNewAds(adsMultiplier[i], calculateFOVAdjustment(fovMultiplier[i], verticalFOV), oldAds)

        return SensitivityConverter.Sensitivity(
            x1 = result[0],
            x1_5 = result[1],
            x2 = result[2],
            x2_5 = result[3],
            x3 = result[4],
            x4 = result[5],
            x5 = result[6],
            x12 = result[7]
        )
    }

    private fun calculateFOVAdjustment(fovMultiplier: Double, verticalFOV: Double): Double {
        return tan(Math.toRadians(fovMultiplier * verticalFOV / 2.0)) / tan(Math.toRadians(verticalFOV / 2.0))
    }

    private fun calculateNewAds(adsMultiplier: Double, fovAdjustment: Double, oldAds: Int): Int {
        return (adsMultiplier / fovAdjustment * oldAds).toInt()
    }

    private fun calculateVerticalFOV(aspectRatio: Double): Double {
        return 2 * atan(tan(Math.toRadians(75.0)) / aspectRatio)
    }

    private fun calculateHorizontalFOV(verticalFOV: Double, aspectRatio: Double): Double {
        return 2 * atan(tan(Math.toRadians(verticalFOV / 2.0)) * aspectRatio)
    }
}
