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
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.widget.doAfterTextChanged
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.calculator.R6Y5S3SensitivityConverter
import com.poorskill.r6adssensitivitycalculator.services.google.GoogleServices
import com.poorskill.r6adssensitivitycalculator.settings.Settings
import com.poorskill.r6adssensitivitycalculator.settings.UserPreferencesManager
import com.poorskill.r6adssensitivitycalculator.ui.AspectRatioAdapter
import com.poorskill.r6adssensitivitycalculator.ui.AspectRatioItem
import com.poorskill.r6adssensitivitycalculator.ui.about.AboutActivity
import com.poorskill.r6adssensitivitycalculator.ui.base.BaseActivity
import com.poorskill.r6adssensitivitycalculator.ui.settings.SettingsActivity


class MainActivity : BaseActivity(), AdapterView.OnItemSelectedListener {

    private val adsMin = 1
    private val adsMax = 100

    private val fovMin = 60
    private val fovMax = 90

    private var oldAdsValue = 50 //game default
    private var fov = 75 //game default
    private var aspectRatio = (16.0 / 9) //game default 16:9
    private var adsValues = IntArray(8)

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

    private fun updateFOV(value: Int) {
        fov = value
        settings.putFOV(fov)
    }

    private fun updateFOVAndEditText(value: Int) {
        updateFOV(value)
        findViewById<EditText>(R.id.fovEdit).setText(value.toString())
    }

    private fun updateFOVAndSeekBar(value: Int, fovSeekBar: SeekBar) {
        updateFOV(value)
        fovSeekBar.progress = value - fovMin
    }

    private fun updateADS(value: Int) {
        oldAdsValue = value
        settings.putADS(oldAdsValue)
    }

    private fun updateADSAndEditText(value: Int) {
        updateADS(value)
        findViewById<EditText>(R.id.oldAdsEdit).setText(value.toString())
    }

    private fun updateADSAndSeekBar(value: Int, adsSeekBar: SeekBar) {
        updateADS(value)
        adsSeekBar.progress = value - adsMin
    }

    private fun setupViews(motionLayout: MotionLayout) {
        //set defaults
        val adsDefault = settings.getADS()
        val fovDefault = settings.getFOV()
        val posAspectRatioDefault = settings.getAspectRatioPos()
        //set val
        val adsSeekBar = findViewById<SeekBar>(R.id.adsSeekBar)
        val fovSeekBar = findViewById<SeekBar>(R.id.fovSeekBar)
        val fovEdit = findViewById<EditText>(R.id.fovEdit)
        val adsEdit = findViewById<EditText>(R.id.oldAdsEdit)
        //update aspect ratio
        setupSpinner(posAspectRatioDefault)
        aspectRatio = getAspectRatioFromArray(posAspectRatioDefault)

        updateADSAndEditText(adsDefault)
        updateFOVAndEditText(fovDefault)

        //EditText Range
        //check while changing text
        fovEdit.doAfterTextChanged {
            if (fovEdit.text.isNotEmpty()) {
                try {
                    val fovValue = Integer.parseInt(fovEdit.text.toString())
                    if (fovValue in fovMin..fovMax) {
                        updateFOVAndSeekBar(fovValue, fovSeekBar)
                    } else {
                        Toast.makeText(
                            this,
                            if (fovValue > fovMax) getString(R.string.tooBig) else getString(R.string.tooSmall),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (ignore: Exception) {
                }
            }
        }
        //check after lost focus
        fovEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                //check if fovEdit value is legal (not empty & in range)
                var fovValue = fovMin
                try {
                    if (fovEdit.text.isNotEmpty()) {
                        val readFovValue = Integer.parseInt(fovEdit.text.toString())
                        fovValue = if (readFovValue in fovMin..fovMax) {
                            readFovValue
                        } else {
                            //set to min/max
                            if (readFovValue > fovMax) {
                                fovMax
                            } else {
                                fovMin
                            }
                        }
                    }
                } catch (ignore: Exception) {
                }
                fovEdit.setText(fovValue.toString())
                updateFOVAndSeekBar(fovValue, fovSeekBar)
            }
        }
        //check while changing text
        adsEdit.doAfterTextChanged {
            if (adsEdit.text.isNotEmpty()) {
                try {
                    val adsValue = Integer.parseInt(adsEdit.text.toString())
                    if (adsValue in adsMin..adsMax) {
                        updateADSAndSeekBar(adsValue, adsSeekBar)
                    } else {
                        Toast.makeText(
                            this,
                            if (adsValue > adsMax) getString(R.string.tooBig) else getString(R.string.tooSmall),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (ignore: Exception) {
                }
            }
        }
        //check after lost focus
        adsEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                //check if adsEdit value is legal (not empty & in range)
                var adsValue = adsMin
                try {
                    if (adsEdit.text.isNotEmpty()) {
                        val readAdsValue = Integer.parseInt(adsEdit.text.toString())
                        adsValue = if (readAdsValue in adsMin..adsMax) {
                            readAdsValue
                        } else {
                            //set to min/max
                            if (readAdsValue > adsMax) {
                                adsMax
                            } else {
                                adsMin
                            }
                        }
                    }
                } catch (ignore: Exception) {
                }
                adsEdit.setText(adsValue.toString())
                updateADSAndSeekBar(adsValue, adsSeekBar)
            }
        }


        adsEdit.doAfterTextChanged {
            updateADS(if (adsEdit.text.isEmpty()) adsDefault else Integer.parseInt(adsEdit.text.toString()))
        }

        //FOV Seekbar
        fovSeekBar.max = fovMax - fovMin
        fovSeekBar.progress = fovDefault - fovMin
        fovSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser)
                    updateFOVAndEditText(fovMin + progress)
                else
                    updateFOV(fovMin + progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                clearFocusFromEditTexts(adsEdit, fovEdit)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //nothing
            }
        }
        )

        //ADS Seekbar
        adsSeekBar.max = adsMax - adsMin
        adsSeekBar.progress = adsDefault - adsMin
        adsSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser)
                    updateADSAndEditText(adsMin + progress)
                else
                    updateADS(adsMin + progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                clearFocusFromEditTexts(adsEdit, fovEdit)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //nothing
            }
        }
        )
        //get ads textViews
        val ads0 = findViewById<TextView>(R.id.output_ads_0)
        val ads1 = findViewById<TextView>(R.id.output_ads_1)
        val ads2 = findViewById<TextView>(R.id.output_ads_2)
        val ads3 = findViewById<TextView>(R.id.output_ads_3)
        val ads4 = findViewById<TextView>(R.id.output_ads_4)
        val ads5 = findViewById<TextView>(R.id.output_ads_5)
        val ads6 = findViewById<TextView>(R.id.output_ads_6)
        val ads7 = findViewById<TextView>(R.id.output_ads_7)

        findViewById<View>(R.id.ads0_row).setOnClickListener(
            adsViewClickListener(0, "ADS 1x")
        )

        findViewById<View>(R.id.ads1_row).setOnClickListener(
            adsViewClickListener(1, "ADS 1.5x")
        )
        findViewById<View>(R.id.ads2_row).setOnClickListener(
            adsViewClickListener(2, "ADS 2x")
        )
        findViewById<View>(R.id.ads3_row).setOnClickListener(
            adsViewClickListener(3, "ADS 2.5x")
        )
        findViewById<View>(R.id.ads4_row).setOnClickListener(
            adsViewClickListener(4, "ADS 3x")
        )
        findViewById<View>(R.id.ads5_row).setOnClickListener(
            adsViewClickListener(5, "ADS 4x")
        )
        findViewById<View>(R.id.ads6_row).setOnClickListener(
            adsViewClickListener(6, "ADS 5x")
        )
        findViewById<View>(R.id.ads7_row).setOnClickListener(
            adsViewClickListener(7, "ADS 12x")
        )

        findViewById<View>(R.id.btnCopyAll).setOnClickListener {
            copyValueToClipboard(
                convertAllValuesToString(),
                getString(R.string.everything)
            )
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            motionLayout.transitionToStart()
            isStartLayout = true
        }

        findViewById<Button>(R.id.btnCalculate).setOnClickListener {
            clearFocusFromEditTexts(adsEdit, fovEdit)
            motionLayout.transitionToEnd()
            calculateNewAdsValues()
            ads0.text = adsValues[0].toString()
            ads1.text = adsValues[1].toString()
            ads2.text = adsValues[2].toString()
            ads3.text = adsValues[3].toString()
            ads4.text = adsValues[4].toString()
            ads5.text = adsValues[5].toString()
            ads6.text = adsValues[6].toString()
            ads7.text = adsValues[7].toString()
            isStartLayout = false
            settings.incrementUsage()
        }

        findViewById<TextView>(R.id.oldAdsTV).setOnClickListener {
            adsEdit.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(fovEdit, InputMethodManager.SHOW_IMPLICIT)
            adsEdit.setSelection(adsEdit.text.length)
        }

        findViewById<TextView>(R.id.fovTV).setOnClickListener {
            fovEdit.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(fovEdit, InputMethodManager.SHOW_IMPLICIT)
            fovEdit.setSelection(fovEdit.text.length)
        }

        findViewById<Button>(R.id.btnShare).setOnClickListener { shareADSValues() }
        findViewById<Button>(R.id.btnHelp).setOnClickListener { helpButtonClick() }
    }


    private fun adsViewClickListener(adsValueIndex: Int, name: String): View.OnClickListener {
        return View.OnClickListener {
            copyValueToClipboard(
                adsValues[adsValueIndex].toString(),
                name
            )
        }
    }

    private fun setupSpinner(startPos: Int) {
        val aspectRatios = listOf(
            AspectRatioItem("16:9 (Auto)", 16.0 / 9),
            AspectRatioItem("16:10", 16.0 / 10),
            AspectRatioItem("4:3", 4.0 / 3),
            AspectRatioItem("3:2", 3.0 / 2),
            AspectRatioItem("5:3", 5.0 / 3),
            AspectRatioItem("5:4", 5.0 / 4),
            AspectRatioItem("19:10", 19.0 / 10),
            AspectRatioItem("21:10", 21.0 / 9)
        )
        val spinner: Spinner = findViewById(R.id.aspectRatioSpinner)
        spinner.onItemSelectedListener = this
        val customAspectAdapter = AspectRatioAdapter(this, aspectRatios)
        spinner.adapter = customAspectAdapter
        spinner.setSelection(startPos)

        findViewById<TextView>(R.id.aspectTV).setOnClickListener {
            spinner.performClick()
        }
    }

    /**
     * get aspect ratio by pos -> @arrays.aspect_ratio_array
     */
    private fun getAspectRatioFromArray(pos: Int): Double {
        return when (pos) {
            0 -> (19.0 / 9)
            1 -> (19.0 / 10)
            2 -> (4.0 / 3)
            3 -> (3.0 / 2)
            4 -> (5.0 / 3)
            5 -> (5.0 / 4)
            6 -> (19.0 / 10)
            7 -> (21.0 / 9)
            else -> 1.0
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        setAspectRatio(getAspectRatioFromArray(pos))
        settings.putAspectRatio(pos)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    private fun setAspectRatio(aspectRatio: Double) {
        this.aspectRatio = aspectRatio
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
        return """
             ${resources.getString(R.string.copy_start)}
             ADS 1x = ${adsValues[0]}
             ADS 1.5x = ${adsValues[1]}
             ADS 2x = ${adsValues[2]}
             ADS 2.5x = ${adsValues[3]}
             ADS 3x = ${adsValues[4]}
             ADS 4x = ${adsValues[5]}
             ADS 5x = ${adsValues[6]}
             ADS 12x = ${adsValues[7]}
             """.trimIndent()
    }


    private fun calculateNewAdsValues() {
        adsValues = R6Y5S3SensitivityConverter().calculateNewAdsSensitivity(
            oldAdsValue,
            fov,
            aspectRatio
        )
    }

    //---- Options Menu ----------------------------------------------------

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

    //---------------------------------------------------------------------------------

    private fun clearFocusFromEditTexts(adsEdit: EditText, fovEdit: EditText) {
        adsEdit.clearFocus()
        fovEdit.clearFocus()
        findViewById<LinearLayout>(R.id.dummy).requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        //https://stackoverflow.com/questions/1109022/how-do-you-close-hide-the-android-soft-keyboard-programmatically
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * Overrides backPress to go back to start motion layout if in end
     */
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
        startActivity(
            Intent.createChooser(
                intent,
                getString(R.string.shareTitle)
            )
        )
    }

    private fun helpButtonClick() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(this.getString(R.string.ubisoftHelpURL))
            )
        )
    }
}