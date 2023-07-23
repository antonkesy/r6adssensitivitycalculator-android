package com.poorskill.r6adssensitivitycalculator.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.constraintlayout.motion.widget.MotionLayout
import clearFocus
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.converter.R6Y5S3SensitivityConverter
import com.poorskill.r6adssensitivitycalculator.services.google.GoogleServices
import com.poorskill.r6adssensitivitycalculator.settings.Settings
import com.poorskill.r6adssensitivitycalculator.settings.UserPreferencesManager
import com.poorskill.r6adssensitivitycalculator.ui.about.AboutActivity
import com.poorskill.r6adssensitivitycalculator.ui.base.BaseActivity
import com.poorskill.r6adssensitivitycalculator.ui.components.TextEditSeekbar
import com.poorskill.r6adssensitivitycalculator.ui.components.aspectratio.AspectRatioSpinner
import com.poorskill.r6adssensitivitycalculator.ui.settings.SettingsActivity
import copyValueToClipboard

class MainActivity : BaseActivity() {

  private val adsCalculator = R6Y5S3SensitivityConverter()

  private var isStartLayout = true

  private lateinit var settings: Settings

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    supportActionBar?.title = getString(R.string.title_text)
    supportActionBar?.subtitle = getString(R.string.subtitle_text)

    settings = UserPreferencesManager(this)

    with(GoogleServices(this, settings)) {
      checkInAppUpdate()
      checkInAppReview()
    }

    val motionLayout = findViewById<MotionLayout>(R.id.motionLayoutMain)
    setupViews(motionLayout)
  }

  private fun setupViews(motionLayout: MotionLayout) {
    // TODO: settings.putADS(value) -> create decorator for Conveter to save/get from
    adsCalculator.ads.value = settings.getADS()
    adsCalculator.fov.value = settings.getFOV()
    adsCalculator.aspectRatio.currentIndex = settings.getAspectRatioPos()

    TextEditSeekbar(
        adsCalculator.ads,
        findViewById(R.id.oldAdsTV),
        findViewById(R.id.oldAdsEdit),
        findViewById(R.id.adsSeekBar),
        this
    )

    TextEditSeekbar(
        adsCalculator.fov,
        findViewById(R.id.fovTV),
        findViewById(R.id.fovEdit),
        findViewById(R.id.fovSeekBar),
        this
    )

    setupSpinner()

    // get ads textViews
    val ads0 = findViewById<TextView>(R.id.output_ads_0)
    val ads1 = findViewById<TextView>(R.id.output_ads_1)
    val ads2 = findViewById<TextView>(R.id.output_ads_2)
    val ads3 = findViewById<TextView>(R.id.output_ads_3)
    val ads4 = findViewById<TextView>(R.id.output_ads_4)
    val ads5 = findViewById<TextView>(R.id.output_ads_5)
    val ads6 = findViewById<TextView>(R.id.output_ads_6)
    val ads7 = findViewById<TextView>(R.id.output_ads_7)

    findViewById<View>(R.id.ads0_row).setOnClickListener(adsViewClickListener(0, "ADS 1x"))

    findViewById<View>(R.id.ads1_row).setOnClickListener(adsViewClickListener(1, "ADS 1.5x"))
    findViewById<View>(R.id.ads2_row).setOnClickListener(adsViewClickListener(2, "ADS 2x"))
    findViewById<View>(R.id.ads3_row).setOnClickListener(adsViewClickListener(3, "ADS 2.5x"))
    findViewById<View>(R.id.ads4_row).setOnClickListener(adsViewClickListener(4, "ADS 3x"))
    findViewById<View>(R.id.ads5_row).setOnClickListener(adsViewClickListener(5, "ADS 4x"))
    findViewById<View>(R.id.ads6_row).setOnClickListener(adsViewClickListener(6, "ADS 5x"))
    findViewById<View>(R.id.ads7_row).setOnClickListener(adsViewClickListener(7, "ADS 12x"))

    findViewById<View>(R.id.btnCopyAll).setOnClickListener {
      copyValueToClipboard(convertAllValuesToString(), getString(R.string.everything), this)
    }

    findViewById<Button>(R.id.btnBack).setOnClickListener {
      motionLayout.transitionToStart()
      isStartLayout = true
    }

    findViewById<Button>(R.id.btnCalculate).setOnClickListener {
      clearFocus(this)
      motionLayout.transitionToEnd()
      val adsValues = adsCalculator.calculateNewAdsSensitivity()
      ads0.text = adsValues.x1.toString()
      ads1.text = adsValues.x1_5.toString()
      ads2.text = adsValues.x2.toString()
      ads3.text = adsValues.x2_5.toString()
      ads4.text = adsValues.x3.toString()
      ads5.text = adsValues.x4.toString()
      ads6.text = adsValues.x5.toString()
      ads7.text = adsValues.x12.toString()
      isStartLayout = false
      settings.incrementUsage()
    }

    findViewById<Button>(R.id.btnShare).setOnClickListener { shareADSValues() }
    findViewById<Button>(R.id.btnHelp).setOnClickListener { helpButtonClick() }
  }

  private fun adsViewClickListener(adsValueIndex: Int, name: String): View.OnClickListener {
    return View.OnClickListener {
      copyValueToClipboard(
          adsCalculator.calculateNewAdsSensitivity().asArray()[adsValueIndex].toString(),
          name,
          this
      )
    }
  }

  private fun setupSpinner() {
    val spinner: Spinner = findViewById(R.id.aspectRatioSpinner)
    AspectRatioSpinner(spinner, findViewById(R.id.aspectTV), this, adsCalculator.aspectRatio)
  }

  private fun convertAllValuesToString(): String {
    val adsValues = adsCalculator.calculateNewAdsSensitivity()
    return """
             ${resources.getString(R.string.copy_start)}
             ADS 1x = ${adsValues.x1}
             ADS 1.5x = ${adsValues.x1_5}
             ADS 2x = ${adsValues.x2}
             ADS 2.5x = ${adsValues.x2_5}
             ADS 3x = ${adsValues.x3}
             ADS 4x = ${adsValues.x4}
             ADS 5x = ${adsValues.x5}
             ADS 12x = ${adsValues.x12}
             """.trimIndent()
  }

  // ---- Options Menu ----------------------------------------------------

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.action_settings -> openSettingsActivity()
      R.id.action_about -> openAboutActivity()
      R.id.action_help -> openHelpActivity()
      else -> {
        return super.onOptionsItemSelected(item)
      }
    }
    return true
  }

  private fun openHelpActivity() {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.ubisoftHelpURL))))
  }

  private fun openSettingsActivity() {
    val intent = Intent(this, SettingsActivity::class.java)
    startActivity(intent)
  }

  private fun openAboutActivity() {
    val intent = Intent(this, AboutActivity::class.java)
    startActivity(intent)
  }

  /** Overrides backPress to go back to start motion layout if in end */
  override fun onBackPressed() {
    if (!isStartLayout) {
      findViewById<MotionLayout>(R.id.motionLayoutMain).transitionToStart()
      isStartLayout = true
    } else {
      super.onBackPressed()
    }
  }

  private fun shareADSValues() {
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.putExtra(Intent.EXTRA_TEXT, convertAllValuesToString())
    intent.type = "text/plain"
    startActivity(Intent.createChooser(intent, getString(R.string.shareTitle)))
  }

  private fun helpButtonClick() {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(this.getString(R.string.ubisoftHelpURL))))
  }
}
