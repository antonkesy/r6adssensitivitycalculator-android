package com.poorskill.r6adssensitivitycalculator.ui.components.aspectratio

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import com.poorskill.r6adssensitivitycalculator.converter.data.AspectRatios
import com.poorskill.r6adssensitivitycalculator.ui.AspectRatioAdapter

class AspectRatioSpinner(
    private val spinner: Spinner,
    private val textView: TextView,
    private val context: Context,
    private val aspectratio: AspectRatios
) : AdapterView.OnItemSelectedListener {

  init {
    setupSpinner()
  }

  private fun setupSpinner() {
    val customAspectAdapter = AspectRatioAdapter(context)
    spinner.adapter = customAspectAdapter
    spinner.setSelection(aspectratio.currentIndex)

    textView.setOnClickListener { spinner.performClick() }

    spinner.onItemSelectedListener = this
  }

  override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    aspectratio.currentIndex = position
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {}
}
