package com.poorskill.r6adssensitivitycalculator.converter

import com.poorskill.r6adssensitivitycalculator.converter.data.Sensitivity

interface SensitivityConverter {
  fun calculate(): Sensitivity
}
