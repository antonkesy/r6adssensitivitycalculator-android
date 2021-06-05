package com.poorskill.r6adssensitivitycalculator.ui.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.ui.about.AboutActivity
import com.poorskill.r6adssensitivitycalculator.ui.base.BaseActivity
import com.poorskill.r6adssensitivitycalculator.ui.settings.SettingsActivity
import com.poorskill.r6adssensitivitycalculator.utility.LastCalculationValues
import com.poorskill.r6adssensitivitycalculator.utility.SensitivityCalculator
import java.text.DecimalFormat


class MainActivity : BaseActivity() {
    //new values


    private lateinit var context: Context

    private var oldSensValue = 0.0
    private var fov = 0.0
    private var aspectRatioWidth = 0.0
    private var aspectRatioHeight = 0.0
    private var adsValues = DoubleArray(8)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null)
            context = this
        supportActionBar?.title = getString(R.string.title_text)
        supportActionBar?.subtitle = getString(R.string.subtitle_text)

        setupNewValuesView()
    }

    private fun setupNewValuesView() {
        //get ads textViews
        val ads0 = findViewById<TextView>(R.id.output_ads_0)
        val ads1 = findViewById<TextView>(R.id.output_ads_1)
        val ads2 = findViewById<TextView>(R.id.output_ads_2)
        val ads3 = findViewById<TextView>(R.id.output_ads_3)
        val ads4 = findViewById<TextView>(R.id.output_ads_4)
        val ads5 = findViewById<TextView>(R.id.output_ads_5)
        val ads6 = findViewById<TextView>(R.id.output_ads_6)
        val ads7 = findViewById<TextView>(R.id.output_ads_7)

        findViewById<View>(R.id.ads0_row).setOnClickListener {
            copyValueToClipboard(
                ads0.text.toString(), "ADS 1x"
            )
        }
        findViewById<View>(R.id.ads1_row).setOnClickListener {
            copyValueToClipboard(
                ads1.text.toString(), "ADS 1.5x"
            )
        }
        findViewById<View>(R.id.ads2_row).setOnClickListener {
            copyValueToClipboard(
                ads2.text.toString(), "ADS 2x"
            )
        }
        findViewById<View>(R.id.ads3_row).setOnClickListener {
            copyValueToClipboard(
                ads3.text.toString(), "ADS 2.5x"
            )
        }
        findViewById<View>(R.id.ads4_row).setOnClickListener {
            copyValueToClipboard(
                ads4.text.toString(), "ADS 3x"
            )
        }
        findViewById<View>(R.id.ads5_row).setOnClickListener {
            copyValueToClipboard(
                ads5.text.toString(), "ADS 4x"
            )
        }
        findViewById<View>(R.id.ads6_row).setOnClickListener {
            copyValueToClipboard(
                ads6.text.toString(), "ADS 5x"
            )
        }
        findViewById<View>(R.id.ads7_row).setOnClickListener {
            copyValueToClipboard(
                ads7.text.toString(), "ADS 12x"
            )
        }
        findViewById<View>(R.id.btn_share).setOnClickListener {
            copyValueToClipboard(
                convertAllValuesToString(),
                getString(R.string.everything)
            )
        }

        findViewById<Button>(R.id.btnCalculate).setOnClickListener {
            //todo motion layout start
            val df = DecimalFormat("#")
            ads0.text = df.format(adsValues[0])
            ads1.text = df.format(adsValues[1])
            ads2.text = df.format(adsValues[2])
            ads3.text = df.format(adsValues[3])
            ads4.text = df.format(adsValues[4])
            ads5.text = df.format(adsValues[5])
            ads6.text = df.format(adsValues[6])
            ads7.text = df.format(adsValues[7])
        }
    }

    private fun copyValueToClipboard(value: String, name: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(R.string.copyValues), value)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(
            this,
            getString(R.string.copied) + name + getString(R.string.toClipboard),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun convertAllValuesToString(): String {
        /* TODO
        return """
             ${resources.getString(R.string.copy_start)}
             ADS 1x = ${ads0.getText().toString()}
             ADS 1.5x = ${ads_1.getText().toString()}
             ADS 2x = ${ads_2.getText().toString()}
             ADS 2.5x = ${ads_3.getText().toString()}
             ADS 3x = ${ads_4.getText().toString()}
             ADS 4x = ${ads_5.getText().toString()}
             ADS 5x = ${ads_6.getText().toString()}
             ADS 12x = ${ads_7.getText().toString()}
             """.trimIndent()
             */
        return ""
    }

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

    fun getAdsValues(): DoubleArray {
        return adsValues
    }

    fun calculateNewAds() {
        adsValues = SensitivityCalculator.calculateNewAdsSensitivity(
            oldSensValue,
            fov,
            aspectRatioWidth / aspectRatioHeight
        )
    }

    fun setInputValues(
        oldSensValue: Double,
        inputFOV: Double,
        inputAspectRatioWidth: Double,
        inputAspectRatioHeight: Double
    ) {
        try {
            this.oldSensValue = oldSensValue
            fov = inputFOV
            aspectRatioWidth = inputAspectRatioWidth
            aspectRatioHeight = inputAspectRatioHeight
            LastCalculationValues.saveValues(
                context,
                oldSensValue,
                inputFOV,
                inputAspectRatioWidth,
                inputAspectRatioHeight
            )
        } catch (e: UninitializedPropertyAccessException) {
            Toast.makeText(
                context,
                context.getString(R.string.errorPropertyAccess),
                Toast.LENGTH_SHORT
            ).show()
        }
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


}