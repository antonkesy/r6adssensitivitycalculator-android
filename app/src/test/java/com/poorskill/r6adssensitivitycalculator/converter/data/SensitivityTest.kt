package com.poorskill.r6adssensitivitycalculator.converter.data

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class SensitivityTest {

  private val sensitivity =
      Sensitivity(x1 = 1, x1_5 = 2, x2 = 3, x2_5 = 4, x3 = 5, x4 = 6, x5 = 7, x12 = 8)

  @Test
  fun asArrayIsInScopeOrder() {
    // The UI indexes into this by row position, so the order has to match the on-screen labels.
    assertArrayEquals(intArrayOf(1, 2, 3, 4, 5, 6, 7, 8), sensitivity.asArray())
  }

  @Test
  fun toStringIsTheSharePayload() {
    // This exact text is what Copy Values and Share put on the clipboard.
    assertEquals(
        """
        ADS 1x = 1
        ADS 1.5x = 2
        ADS 2x = 3
        ADS 2.5x = 4
        ADS 3x = 5
        ADS 4x = 6
        ADS 5x = 7
        ADS 12x = 8
        """
            .trimIndent(),
        sensitivity.toString()
    )
  }
}
