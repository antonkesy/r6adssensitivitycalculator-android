package com.poorskill.r6adssensitivitycalculator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * About Page
 */
class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Update Version Code
        findViewById<TextView>(R.id.versionCodeAbout).text =
                (this.packageManager.getPackageInfo(packageName, 0).versionName).toString()
        //Define Buttons
        val websiteButton = findViewById<Button>(R.id.websiteButton)
        val privacyPolicyButton = findViewById<Button>(R.id.privacyPolicyButton)
        val contactButton = findViewById<Button>(R.id.contactButton)
        val sourceCodeButton = findViewById<Button>(R.id.sourceCodeButton)
        val reportBugButton = findViewById<Button>(R.id.reportBugButton)
        val rateAppButton = findViewById<Button>(R.id.rateAppButton)

        //Button Listener
        websiteButton.setOnClickListener { openURLInBrowser(this.getString(R.string.poorskillWebsite)) }
        websiteButton.setOnLongClickListener { copyURLToClipboard(this.getString(R.string.poorskillWebsite)) }
        privacyPolicyButton.setOnClickListener { openURLInBrowser(this.getString(R.string.privacyPolicyURL)) }
        privacyPolicyButton.setOnLongClickListener { copyURLToClipboard(this.getString(R.string.privacyPolicyURL)) }
        contactButton.setOnClickListener {
            openMail(
                    this.getString(R.string.contactMail),
                    this.getString(R.string.app_name)
            )
        }
        contactButton.setOnLongClickListener { copyURLToClipboard(this.getString(R.string.contactMail)) }
        sourceCodeButton.setOnClickListener { openURLInBrowser(this.getString(R.string.sourceCodeURL)) }
        sourceCodeButton.setOnLongClickListener { copyURLToClipboard(this.getString(R.string.sourceCodeURL)) }
        reportBugButton.setOnClickListener { openURLInBrowser(this.getString(R.string.reportBugURL)) }
        reportBugButton.setOnLongClickListener { copyURLToClipboard(this.getString(R.string.reportBugURL)) }
        rateAppButton.setOnClickListener { openURLInBrowser(this.getString(R.string.rateAppURL)) }
        rateAppButton.setOnLongClickListener { copyURLToClipboard(this.getString(R.string.rateAppURL)) }
    }

    /**
     * ActionMenu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.about_action_menu, menu)
        return true
    }

    /**
     * ActionMenu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.aboutBackArrow -> this.finish()
        }
        return true
    }

    /**
     * MailTo Intent
     */
    private fun openMail(address: String, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, address)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(intent)
    }

    /**
     * Browser Intent
     */
    private fun openURLInBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    /**
     * Get SystemService and copies to clipboard
     */
    private fun copyURLToClipboard(url: String): Boolean {
        val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(android.R.attr.label.toString(), url)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show()
        return true
    }
}