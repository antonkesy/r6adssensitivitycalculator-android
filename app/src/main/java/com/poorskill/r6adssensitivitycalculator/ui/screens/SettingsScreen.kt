package com.poorskill.r6adssensitivitycalculator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.converter.data.AdsScope
import com.poorskill.r6adssensitivitycalculator.settings.Settings
import com.poorskill.r6adssensitivitycalculator.ui.Theme
import com.poorskill.r6adssensitivitycalculator.ui.theme.appTheme

/**
 * Replaces the two-entry `PreferenceFragmentCompat`. Same preference keys and same stored values
 * for theme and language, so settings made by an older build are read back unchanged; the
 * "shown ADS values" picker is new.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settings: Settings, onBack: () -> Unit) {
  val themeEntries = stringArrayResource(R.array.theme_entries)
  val themeValues = stringArrayResource(R.array.theme_values)
  val languageEntries = stringArrayResource(R.array.language_entries)
  val languageValues = stringArrayResource(R.array.language_values)

  var language by remember { mutableStateOf(settings.language) }
  val theme by appTheme
  val scopes by visibleScopes

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
        modifier =
            Modifier.fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      SectionHeader(stringResource(R.string.application_header))

      SettingRow(
          title = stringResource(R.string.app_theme_title),
          entries = themeEntries,
          selectedIndex = themeValues.indexOf(theme.id.toString()).coerceAtLeast(0)
      ) { index ->
        val picked = Theme.entries.first { it.id.toString() == themeValues[index] }
        settings.theme = picked
        appTheme.value = picked
      }

      SettingRow(
          title = stringResource(R.string.app_language_title),
          entries = languageEntries,
          selectedIndex = languageValues.indexOf(language).coerceAtLeast(0)
      ) { index ->
        language = languageValues[index]
        // recreates the activity itself, via AppCompatDelegate.setApplicationLocales
        settings.language = language
      }

      var pickerOpen by remember { mutableStateOf(false) }
      SettingButtonRow(
          title = stringResource(R.string.visible_scopes_header),
          value = "${scopes.size} / ${AdsScope.entries.size}"
      ) {
        pickerOpen = true
      }
      if (pickerOpen) {
        ScopePickerDialog(scopes = scopes, onDismiss = { pickerOpen = false }) { next ->
          settings.visibleScopes = next
          visibleScopes.value = next
        }
      }
    }
  }
}

/** Title on the left, a button with the current value on the right — same look as [SettingRow]. */
@Composable
private fun SettingButtonRow(title: String, value: String, onClick: () -> Unit) {
  Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
    Text(
        title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.weight(1f)
    )
    TextButton(onClick = onClick) { Text(value, style = MaterialTheme.typography.titleMedium) }
  }
}

/**
 * One checkbox per scope. Every tick is applied immediately (like the dropdowns), so closing the
 * dialog is just closing it. The last ticked scope is disabled: an empty result list would only
 * look broken.
 */
@Composable
private fun ScopePickerDialog(
    scopes: Set<AdsScope>,
    onDismiss: () -> Unit,
    onChange: (Set<AdsScope>) -> Unit
) {
  AlertDialog(
      onDismissRequest = onDismiss,
      title = { Text(stringResource(R.string.visible_scopes_header)) },
      text = {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
          AdsScope.entries.forEach { scope ->
            val on = scope in scopes
            ScopeCheckRow(
                title = scope.displayName,
                checked = on,
                enabled = !(on && scopes.size == 1)
            ) { checked ->
              onChange(if (checked) scopes + scope else scopes - scope)
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = onDismiss) { Text(stringResource(android.R.string.ok)) }
      }
  )
}

/**
 * The whole row toggles (and carries the checkbox semantics), so the label is the tap target and
 * tests can find the checkbox by its text.
 */
@Composable
private fun ScopeCheckRow(
    title: String,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
  Row(
      modifier =
          Modifier.fillMaxWidth()
              .toggleable(
                  value = checked,
                  enabled = enabled,
                  role = Role.Checkbox,
                  onValueChange = onCheckedChange
              )
              .padding(vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically
  ) {
    Checkbox(checked = checked, onCheckedChange = null, enabled = enabled)
    Text(
        title,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.weight(1f).padding(start = 12.dp)
    )
  }
}

@Composable
private fun SectionHeader(text: String) {
  Text(
      text,
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.primary,
      modifier = Modifier.padding(vertical = 12.dp)
  )
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
