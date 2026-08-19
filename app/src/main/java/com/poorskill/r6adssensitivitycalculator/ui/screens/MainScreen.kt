package com.poorskill.r6adssensitivitycalculator.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.converter.PersistentSensitivityConverter
import com.poorskill.r6adssensitivitycalculator.converter.data.AspectRatios
import com.poorskill.r6adssensitivitycalculator.ui.components.ValueSlider
import com.poorskill.r6adssensitivitycalculator.ui.copyToClipboard
import com.poorskill.r6adssensitivitycalculator.ui.openAbout
import com.poorskill.r6adssensitivitycalculator.ui.openHelp
import com.poorskill.r6adssensitivitycalculator.ui.openSettings
import com.poorskill.r6adssensitivitycalculator.ui.shareString
import kotlinx.coroutines.launch

private val adsLabels =
    intArrayOf(
        R.string.ads_1,
        R.string.ads_2,
        R.string.ads_3,
        R.string.ads_4,
        R.string.ads_5,
        R.string.ads_6,
        R.string.ads_7,
        R.string.ads_8
    )

/**
 * Inputs and converted values on one screen — the results update as you drag, so there is no
 * calculate button and no second page (the old build slid between two MotionLayout states). The
 * eight results sit in a 2-column grid so the whole thing fits without scrolling on a normal phone.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(converter: PersistentSensitivityConverter, activity: Activity) {
  var ads by rememberSaveable { mutableIntStateOf(converter.ads.value) }
  var fov by rememberSaveable { mutableIntStateOf(converter.fov.value) }
  var aspect by rememberSaveable { mutableIntStateOf(converter.aspectRatio.currentIndex) }
  // writing through the converter is what persists the value (RangedValue.onChange -> Settings)
  val result = remember(ads, fov, aspect) { converter.calculate() }

  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val copiedPrefix = stringResource(R.string.copied)
  val copiedSuffix = stringResource(R.string.toClipboard)
  fun copy(value: String, name: String) {
    copyToClipboard(value, activity)
    scope.launch { snackbarHostState.showSnackbar(copiedPrefix + name + copiedSuffix) }
  }

  Scaffold(
      snackbarHost = { SnackbarHost(snackbarHostState) },
      topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.app_name)) },
            actions = { OverflowMenu(activity) }
        )
      }
  ) { innerPadding ->
    Column(
        modifier =
            Modifier.fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionHeader(stringResource(R.string.input_header))
        Card(
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                )
        ) {
          Column(
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
              verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            ValueSlider(
                label = stringResource(R.string.old_ads),
                value = ads,
                min = converter.ads.min,
                max = converter.ads.max
            ) {
              converter.ads.value = it
              ads = it
            }
            ValueSlider(
                label = stringResource(R.string.fov),
                value = fov,
                min = converter.fov.min,
                max = converter.fov.max
            ) {
              converter.fov.value = it
              fov = it
            }
            AspectRatioRow(selectedIndex = aspect) {
              converter.aspectRatio.currentIndex = it
              aspect = it
            }
          }
        }
      }

      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SectionHeader(stringResource(R.string.output_header))
        ResultList(values = result.asArray()) { index, value ->
          copy(value.toString(), activity.getString(adsLabels[index]))
        }
      }

      Row(
          modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        val everything = stringResource(R.string.everything)
        FilledTonalButton(
            onClick = { copy(result.toString(), everything) },
            modifier = Modifier.weight(1f)
        ) {
          Icon(painterResource(R.drawable.ic_baseline_content_copy_24), contentDescription = null)
          Spacer(Modifier.width(8.dp))
          Text(stringResource(R.string.copyValues))
        }
        FilledTonalButton(
            onClick = { shareString(result.toString(), activity) },
            modifier = Modifier.weight(1f)
        ) {
          Icon(painterResource(R.drawable.ic_baseline_share_24), contentDescription = null)
          Spacer(Modifier.width(8.dp))
          Text(stringResource(R.string.share))
        }
      }
    }
  }
}

/** Small caps label that splits the screen into "what you set" and "what you get". */
@Composable
private fun SectionHeader(text: String) {
  Text(
      text.uppercase(),
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.primary,
      letterSpacing = 1.5.sp,
      modifier = Modifier.padding(start = 4.dp)
  )
}

/**
 * One value per line inside a single card. Rows are hairline-separated rather than individually
 * boxed, so a row costs ~40dp instead of the ~56dp a card per value would: eight of them plus the
 * inputs still fit a normal phone without scrolling.
 */
@Composable
private fun ResultList(values: IntArray, onCopy: (index: Int, value: Int) -> Unit) {
  Card(
      shape = RoundedCornerShape(14.dp),
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
          )
  ) {
    values.forEachIndexed { index, value ->
      // zebra striping instead of dividers: easier to keep your eye on one line, same height
      val stripe =
          if (index % 2 == 0) MaterialTheme.colorScheme.surfaceContainerHighest
          else MaterialTheme.colorScheme.surfaceContainerLow
      Row(
          modifier =
              Modifier.fillMaxWidth()
                  .background(stripe)
                  .clickable { onCopy(index, value) }
                  .padding(horizontal = 14.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
            stringResource(adsLabels[index]),
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
    }
  }
}

@Composable
private fun AspectRatioRow(selectedIndex: Int, onSelect: (Int) -> Unit) {
  val ratios = AspectRatios.ALL
  var expanded by remember { mutableStateOf(false) }
  Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
    Text(
        stringResource(R.string.aspect_ratio),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.weight(1f)
    )
    Column(horizontalAlignment = Alignment.End) {
      TextButton(onClick = { expanded = true }) {
        Text(
            ratios[selectedIndex.coerceIn(ratios.indices)].name,
            style = MaterialTheme.typography.titleMedium
        )
      }
      DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        ratios.forEachIndexed { index, ratio ->
          DropdownMenuItem(
              text = { Text(ratio.name) },
              onClick = {
                expanded = false
                onSelect(index)
              }
          )
        }
      }
    }
  }
}

@Composable
private fun OverflowMenu(activity: Activity) {
  var expanded by remember { mutableStateOf(false) }
  IconButton(onClick = { expanded = true }) {
    Icon(
        painterResource(R.drawable.ic_baseline_more_vert_24),
        contentDescription = stringResource(R.string.action_settings)
    )
  }
  DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
    DropdownMenuItem(
        text = { Text(stringResource(R.string.action_help)) },
        onClick = {
          expanded = false
          openHelp(activity)
        }
    )
    DropdownMenuItem(
        text = { Text(stringResource(R.string.action_settings)) },
        onClick = {
          expanded = false
          openSettings(activity)
        }
    )
    DropdownMenuItem(
        text = { Text(stringResource(R.string.action_about)) },
        onClick = {
          expanded = false
          openAbout(activity)
        }
    )
  }
}
