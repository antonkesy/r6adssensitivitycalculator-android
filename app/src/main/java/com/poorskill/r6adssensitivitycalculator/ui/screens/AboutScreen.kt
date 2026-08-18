package com.poorskill.r6adssensitivitycalculator.ui.screens

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.ui.copyToClipboard
import com.poorskill.r6adssensitivitycalculator.ui.openMail
import com.poorskill.r6adssensitivitycalculator.ui.openURLInBrowser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(activity: Activity, onBack: () -> Unit) {
  val versionName =
      remember(activity) {
        activity.packageManager.getPackageInfo(activity.packageName, 0).versionName ?: ""
      }

  Scaffold(
      topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.aboutTitle)) },
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Image(
          painter = painterResource(R.drawable.pixel_panda),
          contentDescription = null,
          modifier = Modifier.height(120.dp).padding(top = 8.dp)
      )
      Text(
          stringResource(R.string.copyrightAntonKesy),
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Card(modifier = Modifier.padding(top = 8.dp)) {
        LinkRow(activity, R.string.website, R.string.poorskillWebsite)
        HorizontalDivider()
        LinkRow(activity, R.string.privacyPolicy, R.string.privacyPolicyURL)
        HorizontalDivider()
        LinkRow(activity, R.string.sourceCode, R.string.sourceCodeURL)
        HorizontalDivider()
        LinkRow(activity, R.string.reportBug, R.string.reportBugURL)
        HorizontalDivider()
        LinkRow(activity, R.string.rateApp, R.string.rateAppURL)
        HorizontalDivider()
        LinkRow(activity, R.string.contact, R.string.contactMail, isMail = true)
      }

      Text(
          "${stringResource(R.string.version)} $versionName",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(vertical = 16.dp)
      )
    }
  }
}

/** Tap opens the target, long press copies it — same as the old view-based About screen. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LinkRow(activity: Activity, labelRes: Int, targetRes: Int, isMail: Boolean = false) {
  val target = stringResource(targetRes)
  val appName = stringResource(R.string.app_name)
  Text(
      stringResource(labelRes),
      style = MaterialTheme.typography.titleMedium,
      modifier =
          Modifier.fillMaxWidth()
              .combinedClickable(
                  onClick = {
                    if (isMail) openMail(target, appName, activity)
                    else openURLInBrowser(target, activity)
                  },
                  onLongClick = { copyToClipboard(target, activity) }
              )
              .padding(horizontal = 20.dp, vertical = 14.dp)
  )
}
