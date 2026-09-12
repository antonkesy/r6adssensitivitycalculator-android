package com.poorskill.r6adssensitivitycalculator.converter.data

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class SensitivityTest {

  /** 1x = 1, 1.5x = 2, ... 12x = 10 */
  private val sensitivity =
      Sensitivity(AdsScope.entries.withIndex().associate { (i, scope) -> scope to i + 1 })

  @Test
  fun getIndexesByScope() {
    assertEquals(1, sensitivity[AdsScope.X1])
    assertEquals(9, sensitivity[AdsScope.X8])
    assertEquals(10, sensitivity[AdsScope.X12])
  }

  @Test
  fun asArrayIsInScopeOrder() {
    // The UI indexes into this by row position, so the order has to match the on-screen labels.
    assertArrayEquals(intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), sensitivity.asArray())
  }

  @Test
  fun toStringIsTheSharePayload() {
    // This exact text is what Copy Values and Share put on the clipboard with every scope shown.
    assertEquals(
        """
        ADS 1x = 1
        ADS 1.5x = 2
        ADS 2x = 3
        ADS 2.5x = 4
        ADS 3x = 5
        ADS 3.5x = 6
        ADS 4x = 7
        ADS 5x = 8
        ADS 8x = 9
        ADS 12x = 10
        """
            .trimIndent(),
        sensitivity.toString()
    )
  }

  @Test
  fun formatOnlyListsTheGivenScopesInScopeOrder() {
    assertEquals(
        "ADS 1x = 1\nADS 8x = 9\nADS 12x = 10",
        sensitivity.format(setOf(AdsScope.X12, AdsScope.X1, AdsScope.X8))
    )
  }
}
