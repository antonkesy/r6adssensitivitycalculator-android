package com.poorskill.r6adssensitivitycalculator.ui

import android.app.Activity
import android.content.Intent
import androidx.core.net.toUri
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.ui.activities.AboutActivity
import com.poorskill.r6adssensitivitycalculator.ui.activities.SettingsActivity

fun openHelp(current: Activity) =
    openURLInBrowser(current.getString(R.string.ubisoftHelpURL), current)

fun openAbout(current: Activity) = current.startActivity(Intent(current, AboutActivity::class.java))

fun openSettings(current: Activity) =
    current.startActivity(Intent(current, SettingsActivity::class.java))

fun openMail(address: String, subject: String, activity: Activity) =
    activity.startActivity(
        Intent(Intent.ACTION_SENDTO).apply {
          data = "mailto:".toUri() // only email apps should handle this
          putExtra(Intent.EXTRA_EMAIL, address)
          putExtra(Intent.EXTRA_SUBJECT, subject)
        }
    )

fun openURLInBrowser(url: String, activity: Activity) =
    activity.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
