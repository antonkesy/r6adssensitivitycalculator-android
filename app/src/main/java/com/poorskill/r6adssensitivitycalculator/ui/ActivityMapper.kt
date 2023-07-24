package com.poorskill.r6adssensitivitycalculator.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.ui.activities.AboutActivity
import com.poorskill.r6adssensitivitycalculator.ui.activities.SettingsActivity

fun openHelp(current: Activity) {
  current.startActivity(
      Intent(Intent.ACTION_VIEW, Uri.parse(current.getString(R.string.ubisoftHelpURL)))
  )
}

fun openAbout(current: Activity) {
  val intent = Intent(current, AboutActivity::class.java)
  current.startActivity(intent)
}

fun openSettings(current: Activity) {
  val intent = Intent(current, SettingsActivity::class.java)
  current.startActivity(intent)
}

fun openMail(address: String, subject: String, activity: Activity) {
  val intent = Intent(Intent.ACTION_SENDTO)
  intent.data = Uri.parse("mailto:") // only email apps should handle this
  intent.putExtra(Intent.EXTRA_EMAIL, address)
  intent.putExtra(Intent.EXTRA_SUBJECT, subject)
  activity.startActivity(intent)
}

fun openURLInBrowser(url: String, activity: Activity) {
  activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}
