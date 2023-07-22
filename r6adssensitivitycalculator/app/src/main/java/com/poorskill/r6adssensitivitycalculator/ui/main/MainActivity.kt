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
import com.poorskill.r6adssensitivitycalculator.converter.R6Y5S3SensitivityConverter
import com.poorskill.r6adssensitivitycalculator.services.google.GoogleServices
import com.poorskill.r6adssensitivitycalculator.settings.Settings
import com.poorskill.r6adssensitivitycalculator.settings.UserPreferencesManager
import com.poorskill.r6adssensitivitycalculator.ui.AspectRatioAdapter
import com.poorskill.r6adssensitivitycalculator.ui.about.AboutActivity
import com.poorskill.r6adssensitivitycalculator.ui.base.BaseActivity
import com.poorskill.r6adssensitivitycalculator.ui.settings.SettingsActivity
import com.poorskill.r6adssensitivitycalculator.data.AspectRatios
import com.poorskill.r6adssensitivitycalculator.data.RangedValue


class MainActivity : BaseActivity(), AdapterView.OnItemSelectedListener {

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

    private fun updateFOV(value: Int) {
        adsCalculator.fov.value = value
        settings.putFOV(value)
    }

    private fun updateFOVAndEditText(value: Int) {
        updateFOV(value)
        findViewById<EditText>(R.id.fovEdit).setText(value.toString())
    }

    private fun updateFOVAndSeekBar(value: Int, fovSeekBar: SeekBar) {
        updateFOV(value)
        fovSeekBar.progress = value - adsCalculator.fov.min
    }

    private fun updateADS(value: Int) {
        adsCalculator.ads.value = value
        settings.putADS(value)
    }

    private fun updateADSAndEditText(value: Int) {
        updateADS(value)
        findViewById<EditText>(R.id.oldAdsEdit).setText(value.toString())
    }

    private fun updateADSAndSeekBar(value: Int, adsSeekBar: SeekBar) {
        updateADS(value)
        adsSeekBar.progress = value - adsCalculator.ads.min
    }

    private fun updateEdit(editText : EditText, range : RangedValue<Int>, seekbar : SeekBar, update : (Int, SeekBar) -> Unit) {
        //check while changing text
        editText.doAfterTextChanged {
            if (editText.text.isNotEmpty()) {
                try {
                    val newValue = Integer.parseInt(editText.text.toString())
                    if (newValue in range.min..range.max) {
                        update(newValue, seekbar)
                    } else {
                        Toast.makeText(
                            this,
                            if (newValue > range.max) getString(R.string.tooBig) else getString(R.string.tooSmall),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (ignore: Exception) {
                }
            }
        }
        //check after lost focus
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                //check if fovEdit value is legal (not empty & in range)
                var newValue = range.min
                try {
                    if (editText.text.isNotEmpty()) {
                        val readFovValue = Integer.parseInt(editText.text.toString())
                        newValue = if (readFovValue in range.min..range.max) {
                            readFovValue
                        } else {
                            //set to min/max
                            if (readFovValue > range.max) {
                                range.max
                            } else {
                                range.min
                            }
                        }
                    }
                } catch (ignore: Exception) {
                }
                editText.setText(newValue.toString())
                update(newValue, seekbar)
            }
        }
    }

    private fun initSeekBar(bar : SeekBar, range : RangedValue<Int>, onProgressChanged : (Boolean, Int) -> Unit){
        bar.max = range.max - range.min 
        bar.progress =  range.value - range.min 
        bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                onProgressChanged(fromUser, range.min + progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                clearFocusFromEditTexts()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //nothing
            }
        }
        )
    }

    private fun setupViews(motionLayout: MotionLayout) {
        //set defaults
        adsCalculator.ads.value = settings.getADS()
        adsCalculator.fov.value = settings.getFOV()
        adsCalculator.aspectRatio = AspectRatios.getAll()[settings.getAspectRatioPos()]
        //set val
        val adsSeekBar = findViewById<SeekBar>(R.id.adsSeekBar)
        val fovSeekBar = findViewById<SeekBar>(R.id.fovSeekBar)
        val fovEdit = findViewById<EditText>(R.id.fovEdit)
        val adsEdit = findViewById<EditText>(R.id.oldAdsEdit)
        
        setupSpinner(settings.getAspectRatioPos())
        updateADSAndEditText(settings.getADS())
        updateFOVAndEditText(settings.getFOV())

        //EditText Range
        //check while changing text
        updateEdit(fovEdit, adsCalculator.fov, fovSeekBar){ v, bar -> updateFOVAndSeekBar(v,bar)}
        updateEdit(adsEdit, adsCalculator.ads, adsSeekBar) { v, bar -> updateADSAndSeekBar(v,bar)}
        
        adsEdit.doAfterTextChanged {
            updateADS(if (adsEdit.text.isEmpty()) adsCalculator.ads.value else Integer.parseInt(adsEdit.text.toString()))
        }

        initSeekBar(fovSeekBar, adsCalculator.fov) { 
          fromUser,value -> 
              if (fromUser)
                    updateFOVAndEditText(value)
                else
                    updateFOV(value)
        }

        initSeekBar(adsSeekBar, adsCalculator.ads) { 
          fromUser,value -> 
              if (fromUser)
                    updateADSAndEditText(value)
                else
                    updateADS(value)
        }
        
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
            clearFocusFromEditTexts()
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
                adsCalculator.calculateNewAdsSensitivity().asArray()[adsValueIndex].toString(),
                name
            )
        }
    }

    private fun setupSpinner(startPos: Int) {
        val spinner: Spinner = findViewById(R.id.aspectRatioSpinner)
        spinner.onItemSelectedListener = this
        val customAspectAdapter = AspectRatioAdapter(this)
        spinner.adapter = customAspectAdapter
        spinner.setSelection(startPos)

        findViewById<TextView>(R.id.aspectTV).setOnClickListener {
            spinner.performClick()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        adsCalculator.aspectRatio = AspectRatios.getAll()[pos]
        settings.putAspectRatio(pos)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
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

    private fun clearFocusFromEditTexts() {
        val fovEdit = findViewById<EditText>(R.id.fovEdit)
        val adsEdit = findViewById<EditText>(R.id.oldAdsEdit)

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
