package com.poorskill.r6adssensitivitycalculator.ui

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import com.poorskill.r6adssensitivitycalculator.R

fun copyToClipboard(value: String, context: Context) {
  val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
  val clip = ClipData.newPlainText(context.getString(R.string.copyValues), value)
  clipboard.setPrimaryClip(clip)
}

fun shareString(value: String, activity: Activity) {
  val intent = Intent()
  intent.action = Intent.ACTION_SEND
  intent.putExtra(Intent.EXTRA_TEXT, value)
  intent.type = "text/plain"
  activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.shareTitle)))
}
