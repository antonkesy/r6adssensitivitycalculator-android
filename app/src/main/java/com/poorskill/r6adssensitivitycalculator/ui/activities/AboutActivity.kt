package com.poorskill.r6adssensitivitycalculator.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.databinding.ActivityAboutBinding
import com.poorskill.r6adssensitivitycalculator.ui.copyToClipboard
import com.poorskill.r6adssensitivitycalculator.ui.openMail
import com.poorskill.r6adssensitivitycalculator.ui.openURLInBrowser

class AboutActivity : BaseActivity() {

  private lateinit var binding: ActivityAboutBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAboutBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.versionCodeAbout.text =
        (this.packageManager.getPackageInfo(packageName, 0).versionName).toString()

    bindButton(binding.websiteButton, R.string.poorskillWebsite) {
      openURLInBrowser(this.getString(R.string.poorskillWebsite), this)
    }

    bindButton(binding.privacyPolicyButton, R.string.privacyPolicyURL) {
      openURLInBrowser(this.getString(R.string.privacyPolicyURL), this)
    }

    bindButton(binding.contactButton, R.string.contactMail) {
      openMail(this.getString(R.string.contactMail), this.getString(R.string.app_name), this)
    }

    bindButton(binding.sourceCodeButton, R.string.sourceCodeURL) {
      openURLInBrowser(this.getString(R.string.sourceCodeURL), this)
    }

    bindButton(binding.reportBugButton, R.string.reportBugURL) {
      openURLInBrowser(this.getString(R.string.reportBugURL), this)
    }

    bindButton(binding.rateAppButton, R.string.rateAppURL) {
      openURLInBrowser(this.getString(R.string.rateAppURL), this)
    }

    setTitle(R.string.aboutTitle)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  private fun bindButton(
      btn: View,
      longPressCopyStringRes: Int,
      onClick: () -> Unit,
  ) {
    btn.setOnClickListener { onClick() }
    btn.setOnLongClickListener {
      copyToClipboard(this.getString(longPressCopyStringRes), this)
      return@setOnLongClickListener true
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      android.R.id.home -> {
        onBackPressed()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }
}
