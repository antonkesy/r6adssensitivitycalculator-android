package com.poorskill.r6adssensitivitycalculator.services.google

import android.app.Activity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewManagerFactory
import com.poorskill.r6adssensitivitycalculator.settings.Settings

class GoogleServices(private val activity: Activity, private val settings: Settings) {

  fun checkInAppReview() {
    if (settings.usage < 10) return
    val manager = ReviewManagerFactory.create(activity)
    val request = manager.requestReviewFlow()
    request.addOnCompleteListener { task ->
      if (task.isSuccessful) manager.launchReviewFlow(activity, task.result)
    }
  }

  fun checkInAppUpdate() {
    // https://developer.android.com/guide/playcore/in-app-updates/kotlin-java
    // startUpdateFlow, not startUpdateFlowForResult: an IMMEDIATE update takes over the screen and
    // Play restarts the app itself, so there is no result worth plumbing a launcher back for.
    val appUpdateManager = AppUpdateManagerFactory.create(activity)
    val options = AppUpdateOptions.defaultOptions(AppUpdateType.IMMEDIATE)
    appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
      if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
              appUpdateInfo.isUpdateTypeAllowed(options)
      ) {
        appUpdateManager.startUpdateFlow(appUpdateInfo, activity, options)
      }
    }
  }
}
