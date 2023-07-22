package com.poorskill.r6adssensitivitycalculator.data

class AspectRatios {

  companion object {
    fun getAll(): List<AspectRatio> {
      return listOf(
          AspectRatio("16:9 (Auto)", 16.0 / 9),
          AspectRatio("16:10", 16.0 / 10),
          AspectRatio("4:3", 4.0 / 3),
          AspectRatio("3:2", 3.0 / 2),
          AspectRatio("5:3", 5.0 / 3),
          AspectRatio("5:4", 5.0 / 4),
          AspectRatio("19:10", 19.0 / 10),
          AspectRatio("21:10", 21.0 / 9)
      )
    }
  }
}
