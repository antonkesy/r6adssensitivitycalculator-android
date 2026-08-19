package com.poorskill.r6adssensitivitycalculator.converter

import com.poorskill.r6adssensitivitycalculator.converter.data.AspectRatios
import com.poorskill.r6adssensitivitycalculator.converter.data.RangedValue
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Values pinned from the pre-Compose build: the UI rewrite must not move the numbers. */
class R6Y5S3SensitivityConverterTest {

  private fun convert(ads: Int, fov: Int, aspectIndex: Int = 0): IntArray =
      R6Y5S3SensitivityConverter(
              RangedValue(min = 1, max = 100, value = ads),
              RangedValue(min = 60, max = 90, value = fov),
              AspectRatios(aspectIndex)
          )
          .calculate()
          .asArray()

  @Test
  fun defaultInput() {
    // ADS 50, FOV 60, 16:9 — the out-of-the-box state
    assertArrayEquals(intArrayOf(33, 53, 53, 54, 54, 54, 54, 83), convert(50, 60))
  }

  @Test
  fun rangeExtremes() {
    assertArrayEquals(intArrayOf(0, 1, 1, 1, 1, 1, 1, 1), convert(1, 60))
    assertArrayEquals(intArrayOf(67, 106, 107, 108, 109, 109, 109, 167), convert(100, 60))
    assertArrayEquals(intArrayOf(0, 1, 1, 1, 1, 1, 1, 1), convert(1, 90))
    assertArrayEquals(intArrayOf(70, 118, 120, 122, 124, 124, 126, 193), convert(100, 90))
  }

  @Test
  fun aspectRatioIndexIsClamped() {
    assertEquals(AspectRatios(0).current, AspectRatios(-1).current)
    assertEquals(AspectRatios(7).current, AspectRatios(99).current)
    assertEquals("21:9", AspectRatios(7).current.name)
  }

  /**
   * The 150° horizontal-FOV clamp inside [R6Y5S3SensitivityConverter.calculate] is unreachable:
   * the widest supported setup (FOV 90 at 21:9) only reaches ~133.6°. If this ever fails, the
   * clamp has come alive and the pinned values above are no longer the whole story.
   */
  @Test
  fun horizontalFovClampIsUnreachable() {
    val widest = AspectRatios.ALL.maxOf { it.value }
    val horizontalFov = Math.toDegrees(2 * Math.atan(Math.tan(Math.toRadians(90.0 / 2)) * widest))
    assertTrue("horizontal FOV was $horizontalFov", horizontalFov < 150)

    // ...which is why every aspect ratio currently yields the same result.
    val reference = convert(50, 75, aspectIndex = 0)
    AspectRatios.ALL.indices.forEach { assertArrayEquals(reference, convert(50, 75, it)) }
  }
}
