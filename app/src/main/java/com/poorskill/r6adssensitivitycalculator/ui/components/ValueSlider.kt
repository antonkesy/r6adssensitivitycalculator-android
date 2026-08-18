package com.poorskill.r6adssensitivitycalculator.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlin.math.roundToInt

/** Label, live value, slider. Drag-only — the number is read out, not typed. */
@Composable
fun ValueSlider(label: String, value: Int, min: Int, max: Int, onValueChange: (Int) -> Unit) {
  Column {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
      Text(
          label,
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.weight(1f)
      )
      Text(
          value.toString(),
          style = MaterialTheme.typography.titleLarge,
          color = MaterialTheme.colorScheme.primary
      )
    }
    Slider(
        value = value.toFloat(),
        onValueChange = { onValueChange(it.roundToInt()) },
        valueRange = min.toFloat()..max.toFloat()
    )
  }
}
