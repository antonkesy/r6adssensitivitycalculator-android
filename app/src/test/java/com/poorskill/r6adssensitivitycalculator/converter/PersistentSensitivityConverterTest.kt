package com.poorskill.r6adssensitivitycalculator.converter

import com.poorskill.r6adssensitivitycalculator.settings.FakeSettings
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * The write-through contract the Compose screens rely on: they hold their own state and push each
 * edit into the converter, and that push is the only thing that saves it.
 */
class PersistentSensitivityConverterTest {

  @Test
  fun readsInitialValuesFromSettings() {
    val converter =
        PersistentSensitivityConverter(FakeSettings(ads = 71, fov = 75, aspectRatioPos = 3))

    assertEquals(71, converter.ads.value)
    assertEquals(75, converter.fov.value)
    assertEquals(3, converter.aspectRatio.currentIndex)
  }

  @Test
  fun everyEditIsPersisted() {
    val settings = FakeSettings()
    val converter = PersistentSensitivityConverter(settings)

    converter.ads.value = 80
    converter.fov.value = 85
    converter.aspectRatio.currentIndex = 7

    assertEquals(
        listOf<Pair<String, Any>>("ads" to 80, "fov" to 85, "aspectRatio" to 7),
        settings.writes
    )
    assertEquals(80, settings.ads)
    assertEquals(85, settings.fov)
    assertEquals(7, settings.aspectRatioPos)
  }

  @Test
  fun calculateUsesTheEditedValues() {
    val converter = PersistentSensitivityConverter(FakeSettings(ads = 50, fov = 60))
    assertArrayEquals(intArrayOf(33, 53, 53, 54, 54, 54, 54, 83), converter.calculate().asArray())

    converter.ads.value = 100

    assertArrayEquals(
        intArrayOf(67, 106, 107, 108, 109, 109, 109, 167),
        converter.calculate().asArray()
    )
  }

  @Test
  fun rangesMatchWhatTheGameAccepts() {
    val converter = PersistentSensitivityConverter(FakeSettings())

    assertEquals(1, converter.ads.min)
    assertEquals(100, converter.ads.max)
    assertEquals(60, converter.fov.min)
    assertEquals(90, converter.fov.max)
  }
}
