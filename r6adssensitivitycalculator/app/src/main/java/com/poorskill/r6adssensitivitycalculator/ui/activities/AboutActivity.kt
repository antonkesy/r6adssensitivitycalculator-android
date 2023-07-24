package com.poorskill.r6adssensitivitycalculator.ui.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.databinding.ActivityAboutBinding

class AboutActivity : BaseActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.versionCodeAbout.text = (this.packageManager.getPackageInfo(packageName, 0).versionName).toString()
        
        binding.websiteButton.setOnClickListener { openURLInBrowser(this.getString(R.string.poorskillWebsite)) }
        binding.websiteButton.setOnLongClickListener { copyURLToClipboard(this.getString(R.string.poorskillWebsite)) }
        binding.privacyPolicyButton.setOnClickListener { openURLInBrowser(this.getString(R.string.privacyPolicyURL)) }
        binding.privacyPolicyButton.setOnLongClickListener { copyURLToClipboard(this.getString(R.string.privacyPolicyURL)) }
        binding.contactButton.setOnClickListener {
            openMail(
                this.getString(R.string.contactMail),
                this.getString(R.string.app_name)
            )
        }
       binding.contactButton.setOnLongClickListener { copyURLToClipboard(this.getString(R.string.contactMail)) }
       binding.sourceCodeButton.setOnClickListener { openURLInBrowser(this.getString(R.string.sourceCodeURL)) }
       binding.sourceCodeButton.setOnLongClickListener { copyURLToClipboard(this.getString(R.string.sourceCodeURL)) }
       binding.reportBugButton.setOnClickListener { openURLInBrowser(this.getString(R.string.reportBugURL)) }
       binding.reportBugButton.setOnLongClickListener { copyURLToClipboard(this.getString(R.string.reportBugURL)) }
       binding.rateAppButton.setOnClickListener { openURLInBrowser(this.getString(R.string.rateAppURL)) }
       binding.rateAppButton.setOnLongClickListener { copyURLToClipboard(this.getString(R.string.rateAppURL)) }

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

    private fun openMail(address: String, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, address)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(intent)
    }

    private fun openURLInBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun copyURLToClipboard(url: String): Boolean {
        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(android.R.attr.label.toString(), url)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show()
        return true
    }
}
