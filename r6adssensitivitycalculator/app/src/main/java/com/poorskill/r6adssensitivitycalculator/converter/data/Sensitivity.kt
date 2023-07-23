package com.poorskill.r6adssensitivitycalculator.converter.data

data class Sensitivity(
    val x1: Int,
    val x1_5: Int,
    val x2: Int,
    val x2_5: Int,
    val x3: Int,
    val x4: Int,
    val x5: Int,
    val x12: Int
) {
  fun asArray(): IntArray {
    return intArrayOf(x1, x1_5, x2, x2_5, x3, x4, x5, x12)
  }
}

