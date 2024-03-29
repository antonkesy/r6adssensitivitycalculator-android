package com.poorskill.r6adssensitivitycalculator.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.converter.PersistentSensitivityConverter
import com.poorskill.r6adssensitivitycalculator.databinding.ActivityMainBinding
import com.poorskill.r6adssensitivitycalculator.services.google.GoogleServices
import com.poorskill.r6adssensitivitycalculator.settings.Settings
import com.poorskill.r6adssensitivitycalculator.settings.UserPreferencesManager
import com.poorskill.r6adssensitivitycalculator.ui.clearFocus
import com.poorskill.r6adssensitivitycalculator.ui.components.TextEditSeekbar
import com.poorskill.r6adssensitivitycalculator.ui.components.aspectratio.AspectRatioSpinner
import com.poorskill.r6adssensitivitycalculator.ui.copyToClipboard
import com.poorskill.r6adssensitivitycalculator.ui.openAbout
import com.poorskill.r6adssensitivitycalculator.ui.openHelp
import com.poorskill.r6adssensitivitycalculator.ui.openSettings
import com.poorskill.r6adssensitivitycalculator.ui.shareString

class MainActivity : BaseActivity() {
  private var isStartLayout = true

  private lateinit var settings: Settings
  private lateinit var adsCalculator: PersistentSensitivityConverter
  private lateinit var binding: ActivityMainBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    supportActionBar?.subtitle = getString(R.string.subtitle_text)

    settings = UserPreferencesManager(this)
    adsCalculator = PersistentSensitivityConverter(settings)

    with(GoogleServices(this, settings)) {
      checkInAppUpdate()
      checkInAppReview()
    }

    setupViews()
  }

  private fun setupViews() {
    TextEditSeekbar(
        adsCalculator.ads,
        binding.include.oldAdsTV,
        binding.include.oldAdsEdit,
        binding.include.adsSeekBar,
        this
    )

    TextEditSeekbar(
        adsCalculator.fov,
        binding.include.fovTV,
        binding.include.fovEdit,
        binding.include.fovSeekBar,
        this
    )

    AspectRatioSpinner(
        binding.include.aspectRatioSpinner,
        binding.include.aspectTV,
        this,
        adsCalculator.aspectRatio
    )

    binding.include2.ads0Row.setOnClickListener(adsViewClickListener(0, "ADS 1x"))

    binding.include2.ads1Row.setOnClickListener(adsViewClickListener(1, "ADS 1.5x"))
    binding.include2.ads2Row.setOnClickListener(adsViewClickListener(2, "ADS 2x"))
    binding.include2.ads3Row.setOnClickListener(adsViewClickListener(3, "ADS 2.5x"))
    binding.include2.ads4Row.setOnClickListener(adsViewClickListener(4, "ADS 3x"))
    binding.include2.ads5Row.setOnClickListener(adsViewClickListener(5, "ADS 4x"))
    binding.include2.ads6Row.setOnClickListener(adsViewClickListener(6, "ADS 5x"))
    binding.include2.ads7Row.setOnClickListener(adsViewClickListener(7, "ADS 12x"))

    binding.include2.btnCopyAll.setOnClickListener {
      copyToClipboard(adsCalculator.calculate().toString(), this)
    }

    binding.include2.btnBack.setOnClickListener {
      binding.motionLayoutMain.transitionToStart()
      isStartLayout = true
    }

    binding.include.btnCalculate.setOnClickListener {
      clearFocus(this)
      binding.motionLayoutMain.transitionToEnd()
      val adsValues = adsCalculator.calculate()
      binding.include2.outputAds0.text = adsValues.x1.toString()
      binding.include2.outputAds1.text = adsValues.x1_5.toString()
      binding.include2.outputAds2.text = adsValues.x2.toString()
      binding.include2.outputAds3.text = adsValues.x2_5.toString()
      binding.include2.outputAds4.text = adsValues.x3.toString()
      binding.include2.outputAds5.text = adsValues.x4.toString()
      binding.include2.outputAds6.text = adsValues.x5.toString()
      binding.include2.outputAds7.text = adsValues.x12.toString()
      isStartLayout = false
      settings.incrementUsage()
    }

    binding.include2.btnShare.setOnClickListener {
      shareString(adsCalculator.calculate().toString(), this)
    }
    binding.include2.btnHelp.setOnClickListener { openHelp(this) }
  }

  private fun adsViewClickListener(adsValueIndex: Int, name: String): View.OnClickListener {
    return View.OnClickListener {
      copyToClipboard(adsCalculator.calculate().asArray()[adsValueIndex].toString(), this)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.action_settings -> openSettings(this)
      R.id.action_about -> openAbout(this)
      R.id.action_help -> openHelp(this)
      else -> {
        return super.onOptionsItemSelected(item)
      }
    }
    return true
  }

  /** Overrides backPress to go back to start motion layout if in end */
  @Deprecated("Deprecated in Java")
  override fun onBackPressed() {
    if (!isStartLayout) {
      findViewById<MotionLayout>(R.id.motionLayoutMain).transitionToStart()
      isStartLayout = true
    } else {
      super.onBackPressed()
    }
  }
}
