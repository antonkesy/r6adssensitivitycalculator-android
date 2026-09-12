package com.poorskill.r6adssensitivitycalculator.converter

import com.poorskill.r6adssensitivitycalculator.converter.data.AdsScope
import com.poorskill.r6adssensitivitycalculator.converter.data.AspectRatios
import com.poorskill.r6adssensitivitycalculator.converter.data.RangedValue
import com.poorskill.r6adssensitivitycalculator.converter.data.Sensitivity
import kotlin.math.atan
import kotlin.math.tan

/**
 * Ubisoft's Y5S3 conversion. The per-scope multipliers live on [AdsScope]; this class holds the
 * FOV maths that turns them into slider values.
 */
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

    return Sensitivity(
        AdsScope.entries.associateWith { scope ->
          calculateNewAds(
              scope.adsMultiplier,
              calculateFOVAdjustment(scope.fovMultiplier, verticalFOV),
              ads.value
          )
        }
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
}
