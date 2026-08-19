package com.poorskill.r6adssensitivitycalculator.converter

import com.poorskill.r6adssensitivitycalculator.converter.data.AspectRatios
import com.poorskill.r6adssensitivitycalculator.converter.data.RangedValue
import com.poorskill.r6adssensitivitycalculator.converter.data.Sensitivity
import kotlin.math.atan
import kotlin.math.tan

class R6Y5S3SensitivityConverter(
    val ads: RangedValue,
    val fov: RangedValue,
    var aspectRatio: AspectRatios
) : SensitivityConverter {

  override fun calculate(): Sensitivity {
    val horizontalFOV = calculateHorizontalFOV(fov.value.toDouble(), aspectRatio.current.value)
    val verticalFOV =
        if (horizontalFOV > 150) calculateVerticalFOV(aspectRatio.current.value)
        else fov.value.toDouble()

    val result =
        IntArray(FOV_MULTIPLIER.size) { i ->
          calculateNewAds(
              ADS_MULTIPLIER[i],
              calculateFOVAdjustment(FOV_MULTIPLIER[i], verticalFOV),
              ads.value
          )
        }

    return Sensitivity(
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

  private fun calculateFOVAdjustment(fovMultiplier: Double, verticalFOV: Double) =
      tan(Math.toRadians(fovMultiplier * verticalFOV / 2.0)) / tan(Math.toRadians(verticalFOV / 2.0))

  private fun calculateNewAds(adsMultiplier: Double, fovAdjustment: Double, oldAds: Int) =
      (adsMultiplier / fovAdjustment * oldAds).toInt()

  /** Vertical FOV, in degrees, at which the horizontal FOV hits the 150° cap. */
  private fun calculateVerticalFOV(aspectRatio: Double) =
      Math.toDegrees(2 * atan(tan(Math.toRadians(75.0)) / aspectRatio))

  private fun calculateHorizontalFOV(verticalFOV: Double, aspectRatio: Double) =
      Math.toDegrees(2 * atan(tan(Math.toRadians(verticalFOV / 2.0)) * aspectRatio))

  private companion object {
    val FOV_MULTIPLIER = doubleArrayOf(0.9, 0.59, 0.49, 0.42, 0.35, 0.3, 0.22, 0.092)
    val ADS_MULTIPLIER = doubleArrayOf(0.6, 0.59, 0.49, 0.42, 0.35, 0.3, 0.22, 0.14)
  }
}
