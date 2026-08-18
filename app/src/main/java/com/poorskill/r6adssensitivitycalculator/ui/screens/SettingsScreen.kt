package com.poorskill.r6adssensitivitycalculator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.settings.Settings
import com.poorskill.r6adssensitivitycalculator.ui.Theme
import com.poorskill.r6adssensitivitycalculator.ui.theme.appTheme

/**
 * Replaces the two-entry `PreferenceFragmentCompat`. Same preference keys and same stored values,
 * so settings made by an older build are read back unchanged.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settings: Settings, onBack: () -> Unit) {
  val themeEntries = stringArrayResource(R.array.theme_entries)
  val themeValues = stringArrayResource(R.array.theme_values)
  val languageEntries = stringArrayResource(R.array.language_entries)
  val languageValues = stringArrayResource(R.array.language_values)

  var language by remember { mutableStateOf(settings.getLanguage()) }
  val theme by appTheme

  Scaffold(
      topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.settingsTitle)) },
            navigationIcon = {
              IconButton(onClick = onBack) {
                Icon(
                    painterResource(R.drawable.ic_baseline_arrow_back_24),
                    contentDescription = null
                )
              }
            }
        )
      }
  ) { innerPadding ->
    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Text(
          stringResource(R.string.application_header),
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.padding(vertical = 12.dp)
      )

      SettingRow(
          title = stringResource(R.string.app_theme_title),
          entries = themeEntries,
          selectedIndex = themeValues.indexOf(theme.id.toString()).coerceAtLeast(0)
      ) { index ->
        val picked = Theme.entries.first { it.id.toString() == themeValues[index] }
        settings.putTheme(picked)
        appTheme.value = picked
      }

      SettingRow(
          title = stringResource(R.string.app_language_title),
          entries = languageEntries,
          selectedIndex = languageValues.indexOf(language).coerceAtLeast(0)
      ) { index ->
        language = languageValues[index]
        // recreates the activity itself, via AppCompatDelegate.setApplicationLocales
        settings.putLanguage(language)
      }
    }
  }
}

@Composable
private fun SettingRow(
    title: String,
    entries: Array<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
  var expanded by remember { mutableStateOf(false) }
  Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
    Text(
        title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.weight(1f)
    )
    Column(horizontalAlignment = Alignment.End) {
      TextButton(onClick = { expanded = true }) {
        Text(entries[selectedIndex], style = MaterialTheme.typography.titleMedium)
      }
      DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        entries.forEachIndexed { index, entry ->
          DropdownMenuItem(
              text = { Text(entry) },
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
