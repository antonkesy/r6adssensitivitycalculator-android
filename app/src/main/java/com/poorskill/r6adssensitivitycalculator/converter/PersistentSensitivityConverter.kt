package com.poorskill.r6adssensitivitycalculator.converter

import com.poorskill.r6adssensitivitycalculator.converter.data.AspectRatios
import com.poorskill.r6adssensitivitycalculator.converter.data.RangedValue
import com.poorskill.r6adssensitivitycalculator.converter.data.Sensitivity
import com.poorskill.r6adssensitivitycalculator.settings.Settings

class PersistentSensitivityConverter(private val settings: Settings) : SensitivityConverter {
  val ads =
      RangedValue(min = 1, max = 100, value = settings.getADS()) { new -> settings.putADS(new) }
  val fov =
      RangedValue(min = 60, max = 90, value = settings.getFOV()) { new -> settings.putFOV(new) }
  val aspectRatio: AspectRatios =
      AspectRatios(settings.getAspectRatioPos()) { new -> settings.putAspectRatio(new) }

  private val converter = R6Y5S3SensitivityConverter(ads, fov, aspectRatio)

  override fun calculate(): Sensitivity {
    return converter.calculate()
  }
}
