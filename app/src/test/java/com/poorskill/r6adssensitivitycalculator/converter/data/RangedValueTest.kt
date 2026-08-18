package com.poorskill.r6adssensitivitycalculator.converter.data

import org.junit.Assert.assertEquals
import org.junit.Test

class RangedValueTest {

  @Test
  fun notifiesOnEveryChange() {
    val seen = mutableListOf<Int>()
    val value = RangedValue(min = 1, max = 100, value = 50) { seen += it }

    value.value = 51
    value.value = 60

    assertEquals(listOf(51, 60), seen)
    assertEquals(60, value.value)
  }

  @Test
  fun writingTheSameValueStillNotifies() {
    // Delegates.observable fires on assignment, not on difference. Persisting a redundant value is
    // harmless, but a caller that counts saves should know.
    val seen = mutableListOf<Int>()
    val value = RangedValue(min = 1, max = 100, value = 50) { seen += it }

    value.value = 50

    assertEquals(listOf(50), seen)
  }

  @Test
  fun doesNotClampByItself() {
    // Clamping is the UI's job (ui/components/ValueSlider.kt bounds the slider). If that ever moves
    // in here, this test is the one to delete deliberately.
    val value = RangedValue(min = 1, max = 100, value = 50)

    value.value = 999

    assertEquals(999, value.value)
  }

  @Test
  fun worksWithoutAListener() {
    val value = RangedValue(min = 0, max = 10, value = 5)

    value.value = 7

    assertEquals(7, value.value)
  }
}
