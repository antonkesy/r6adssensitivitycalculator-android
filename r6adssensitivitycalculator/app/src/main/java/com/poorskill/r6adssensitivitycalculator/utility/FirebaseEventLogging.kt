package com.poorskill.r6adssensitivitycalculator.utility

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

class FirebaseEventLogging {

    companion object {

        fun eventLogCalculateADS(
            firebaseAnalytics: FirebaseAnalytics,
            oldAdsValue: Int,
            fov: Int,
            aspectRatio: Double
        ) {
            firebaseAnalytics.logEvent("calculate_ads") {
                param("ads_old", oldAdsValue.toLong())
                param("fov", fov.toLong())
                param("aspect_ratio", aspectRatio)
            }
        }

        fun eventLogBackButton(firebaseAnalytics: FirebaseAnalytics) {
            firebaseAnalytics.logEvent("back_button") {}
        }

        fun eventLogCopyButton(firebaseAnalytics: FirebaseAnalytics) {
            firebaseAnalytics.logEvent("copy_all_button") {}
        }

        fun eventLogAdsViewClicked(firebaseAnalytics: FirebaseAnalytics, name: String) {
            firebaseAnalytics.logEvent("ads_view_clicked") {
                param("name", name)
            }
        }

        fun eventLogAdsHelpButtonClicked(firebaseAnalytics: FirebaseAnalytics) {
            firebaseAnalytics.logEvent("help_button") {}
        }

        fun eventLogAdsShareButtonClicked(firebaseAnalytics: FirebaseAnalytics) {
            firebaseAnalytics.logEvent("share_button") {}
        }

        fun eventLogUserUsageStart(firebaseAnalytics: FirebaseAnalytics, usage: Int) {
            firebaseAnalytics.logEvent("user_usage_start") {
                param("count", usage.toLong())
            }
        }

        fun eventLogOpenAbout(firebaseAnalytics: FirebaseAnalytics) {
            firebaseAnalytics.logEvent("open_about") { }
        }

    }
}
