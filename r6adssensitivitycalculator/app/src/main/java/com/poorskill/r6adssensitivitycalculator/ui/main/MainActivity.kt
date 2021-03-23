package com.poorskill.r6adssensitivitycalculator.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.ui.about.AboutActivity
import com.poorskill.r6adssensitivitycalculator.ui.base.BaseActivity
import com.poorskill.r6adssensitivitycalculator.ui.settings.SettingsActivity
import com.poorskill.r6adssensitivitycalculator.utility.LastCalculationValues
import com.poorskill.r6adssensitivitycalculator.utility.SensitivityCalculator


class MainActivity : BaseActivity() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        private var oldSensValue = 0.0
        private var fov = 0.0
        private var aspectRatioWidth = 0.0
        private var aspectRatioHeight = 0.0
        private var adsValues = DoubleArray(8)

        fun getAdsValues(): DoubleArray {
            return adsValues
        }

        fun calculateNewAds() {
            adsValues = SensitivityCalculator.calculateNewAdsSensitivity(oldSensValue, fov, aspectRatioWidth / aspectRatioHeight)
        }

        fun setInputValues(oldSensValue: Int, inputFOV: Int, inputAspectRatioWidth: Int, inputAspectRatioHeight: Int) {
            try{
            Companion.oldSensValue = oldSensValue.toDouble()
            fov = inputFOV.toDouble()
            aspectRatioWidth = inputAspectRatioWidth.toDouble()
            aspectRatioHeight = inputAspectRatioHeight.toDouble()
            LastCalculationValues.saveValues(context, oldSensValue.toDouble(), inputFOV.toDouble(), inputAspectRatioWidth.toDouble(), inputAspectRatioHeight.toDouble())
            }catch (e: UninitializedPropertyAccessException){
                Toast.makeText(context, context.getString(R.string.errorPropertyAccess), Toast.LENGTH_SHORT).show()
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null)
            context = this
        supportActionBar?.title = getString(R.string.title_text)
        supportActionBar?.subtitle = getString(R.string.subtitle_text)
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