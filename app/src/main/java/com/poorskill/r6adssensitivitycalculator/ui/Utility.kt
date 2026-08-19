package com.poorskill.r6adssensitivitycalculator.ui

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.core.content.getSystemService
import com.poorskill.r6adssensitivitycalculator.R

fun copyToClipboard(value: String, context: Context) {
  val clip = ClipData.newPlainText(context.getString(R.string.copyValues), value)
  context.getSystemService<ClipboardManager>()?.setPrimaryClip(clip)
}

fun shareString(value: String, activity: Activity) {
  val intent =
      Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, value)
        type = "text/plain"
      }
  activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.shareTitle)))
}
