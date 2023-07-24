package com.poorskill.r6adssensitivitycalculator.ui.components.aspectratio

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import com.poorskill.r6adssensitivitycalculator.converter.data.AspectRatios

class AspectRatioSpinner(
    private val spinner: Spinner,
    private val textView: TextView,
    private val context: Context,
    private val ratios: AspectRatios
) : AdapterView.OnItemSelectedListener {

  init {
    setupSpinner()
  }

  private fun setupSpinner() {
    val customAspectAdapter = AspectRatioAdapter(context)
    spinner.adapter = customAspectAdapter
    spinner.setSelection(ratios.currentIndex)

    textView.setOnClickListener { spinner.performClick() }

    spinner.onItemSelectedListener = this
  }

  override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    ratios.currentIndex = position
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {}
}
