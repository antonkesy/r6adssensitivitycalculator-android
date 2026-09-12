package com.poorskill.r6adssensitivitycalculator.converter.data

/**
 * The scopes the game has an ADS slider for, in scope order. Declaration order is what
 * [Sensitivity.asArray] and the main screen's rows follow; [name] is what the "shown scopes"
 * preference stores, so don't rename entries (same rule as `Theme.id`).
 *
 * [fovMultiplier] and [adsMultiplier] are the per-scope rows of Ubisoft's Y5S3 lookup table.
 */
enum class AdsScope(val label: String, val fovMultiplier: Double, val adsMultiplier: Double) {
  X1("1x", 0.9, 0.6),
  X1_5("1.5x", 0.59, 0.59),
  X2("2x", 0.49, 0.49),
  X2_5("2.5x", 0.42, 0.42),
  X3("3x", 0.35, 0.35),
  X4("4x", 0.3, 0.3),
  X5("5x", 0.22, 0.22),
  /**
   * Not in Ubisoft's table (the game has no 8x slider): log-interpolated between the 5x and 12x
   * rows with t = ln(8/5) / ln(12/5). The raw results land just under the next integer
   * (68.9955 at ADS 50 / FOV 60), so nudging these constants or switching `.toInt()` to rounding
   * moves the pinned test numbers.
   */
  X8("8x", 0.138, 0.173),
  X12("12x", 0.092, 0.14);

  /** The row label, e.g. "ADS 8x". Also the share/clipboard text. */
  val displayName
    get() = "ADS $label"
}
