package com.poorskill.r6adssensitivitycalculator.converter.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AspectRatiosTest {

  @Test
  fun theListIsStable() {
    // The selected ratio is persisted by index, so reordering or inserting an entry silently
    // changes what every existing user has selected.
    assertEquals(
        listOf("16:9", "16:10", "4:3", "3:2", "5:3", "5:4", "19:10", "21:9"),
        AspectRatios.ALL.map { it.name }
    )
  }

  @Test
  fun namesMatchTheirRatios() {
    AspectRatios.ALL.forEach { ratio ->
      val (width, height) = ratio.name.split(":").map { it.toDouble() }
      assertEquals(ratio.name, width / height, ratio.value, 1e-9)
    }
  }

  @Test
  fun outOfRangeIndicesClamp() {
    val all = AspectRatios.ALL

    assertEquals(all.first(), AspectRatios(-5).current)
    assertEquals(all.last(), AspectRatios(all.size).current)
    assertEquals(all.last(), AspectRatios(Int.MAX_VALUE).current)
  }

  @Test
  fun notifiesOnSelection() {
    val seen = mutableListOf<Int>()
    val ratios = AspectRatios(0) { seen += it }

    ratios.currentIndex = 4

    assertEquals(listOf(4), seen)
    assertEquals("5:3", ratios.current.name)
  }

  @Test
  fun everyRatioIsWiderThanTall() {
    assertTrue(AspectRatios.ALL.all { it.value >= 1.0 })
  }
}
