package com.poorskill.r6adssensitivitycalculator.converter

import com.poorskill.r6adssensitivitycalculator.converter.data.AspectRatios
import com.poorskill.r6adssensitivitycalculator.converter.data.RangedValue
import com.poorskill.r6adssensitivitycalculator.converter.data.Sensitivity
import com.poorskill.r6adssensitivitycalculator.settings.Settings

class PersistentSensitivityConverter(settings: Settings) : SensitivityConverter {
  val ads = RangedValue(min = 1, max = 100, value = settings.ads) { settings.ads = it }
  val fov = RangedValue(min = 60, max = 90, value = settings.fov) { settings.fov = it }
  val aspectRatio = AspectRatios(settings.aspectRatioPos) { settings.aspectRatioPos = it }

  private val converter = R6Y5S3SensitivityConverter(ads, fov, aspectRatio)

  override fun calculate() = converter.calculate()
}
