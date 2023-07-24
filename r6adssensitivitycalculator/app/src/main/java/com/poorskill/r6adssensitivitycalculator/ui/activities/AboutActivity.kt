package com.poorskill.r6adssensitivitycalculator.ui.activities

import android.os.Bundle
import android.view.MenuItem
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

    binding.websiteButton.setOnClickListener {
      openURLInBrowser(this.getString(R.string.poorskillWebsite), this)
    }
    binding.websiteButton.setOnLongClickListener {
      copyToClipboard(this.getString(R.string.poorskillWebsite), this)
      return@setOnLongClickListener true
    }
    binding.privacyPolicyButton.setOnClickListener {
      openURLInBrowser(this.getString(R.string.privacyPolicyURL), this)
    }
    binding.privacyPolicyButton.setOnLongClickListener {
      copyToClipboard(this.getString(R.string.privacyPolicyURL), this)
      return@setOnLongClickListener true
    }
    binding.contactButton.setOnClickListener {
      openMail(this.getString(R.string.contactMail), this.getString(R.string.app_name), this)
    }
    binding.contactButton.setOnLongClickListener {
      copyToClipboard(this.getString(R.string.contactMail), this)
      return@setOnLongClickListener true
    }
    binding.sourceCodeButton.setOnClickListener {
      openURLInBrowser(this.getString(R.string.sourceCodeURL), this)
    }
    binding.sourceCodeButton.setOnLongClickListener {
      copyToClipboard(this.getString(R.string.sourceCodeURL), this)
      return@setOnLongClickListener true
    }
    binding.reportBugButton.setOnClickListener {
      openURLInBrowser(this.getString(R.string.reportBugURL), this)
    }
    binding.reportBugButton.setOnLongClickListener {
      copyToClipboard(this.getString(R.string.reportBugURL), this)
      return@setOnLongClickListener true
    }
    binding.rateAppButton.setOnClickListener {
      openURLInBrowser(this.getString(R.string.rateAppURL), this)
    }
    binding.rateAppButton.setOnLongClickListener {
      copyToClipboard(this.getString(R.string.rateAppURL), this)
      return@setOnLongClickListener true
    }

    setTitle(R.string.aboutTitle)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
