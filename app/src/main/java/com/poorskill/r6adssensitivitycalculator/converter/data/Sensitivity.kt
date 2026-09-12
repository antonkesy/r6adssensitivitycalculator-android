package com.poorskill.r6adssensitivitycalculator.converter.data

/** The converted ADS value for every [AdsScope]. */
data class Sensitivity(val values: Map<AdsScope, Int>) {

  operator fun get(scope: AdsScope) = values.getValue(scope)

  /** Values in [AdsScope] declaration order. */
  fun asArray() = AdsScope.entries.map { this[it] }.toIntArray()

  /**
   * The share/clipboard text for the given scopes, one `ADS 8x = 68` line each, always in scope
   * order regardless of the set's iteration order.
   */
  fun format(scopes: Set<AdsScope>) =
      AdsScope.entries
          .filter { it in scopes }
          .joinToString("\n") { "${it.displayName} = ${this[it]}" }

  override fun toString() = format(AdsScope.entries.toSet())
}
