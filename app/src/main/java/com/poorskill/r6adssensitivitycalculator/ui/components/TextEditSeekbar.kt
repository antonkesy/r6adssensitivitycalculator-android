package com.poorskill.r6adssensitivitycalculator.ui.components

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.converter.data.RangedValue

class TextEditSeekbar(
    private val range: RangedValue<Int>,
    private val textView: TextView,
    private val editText: EditText,
    private val seekBar: SeekBar, // TODO convert to inner class with specific init
    private val activity: Activity
) {

  init {
    initTextView()
    initSeekBar()
    updateEdit()
  }

  private fun initTextView() {
    textView.setOnClickListener {
      editText.requestFocus()
      val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
      imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
      editText.setSelection(editText.text.length)
    }
  }

  private fun initSeekBar() {
    seekBar.max = range.max - range.min
    seekBar.progress = range.value - range.min
    seekBar.setOnSeekBarChangeListener(
        object : SeekBar.OnSeekBarChangeListener {
          override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            val value = range.min + progress
            if (fromUser) editText.setText(value.toString())
            range.value = value
          }

          override fun onStartTrackingTouch(seekBar: SeekBar?) {
            editText.clearFocus()
            // TODO: clear all focus from main activity
          }

          override fun onStopTrackingTouch(seekBar: SeekBar?) {
            // nothing
          }
        }
    )
  }

  private fun updateEdit() {
    editText.setText(range.value.toString())

    // check while changing text
    editText.doAfterTextChanged {
      if (editText.text.isNotEmpty()) {
        try {
          val newValue = Integer.parseInt(editText.text.toString())
          if (newValue in range.min..range.max) {
            seekBar.progress = newValue - range.min
          } else {
            Toast.makeText(
                    activity,
                    if (newValue > range.max) activity.getString(R.string.tooBig)
                    else activity.getString(R.string.tooSmall),
                    Toast.LENGTH_SHORT
                )
                .show()
          }
        } catch (ignore: Exception) {}
      }
    }
    // check after lost focus
    editText.setOnFocusChangeListener { _, hasFocus ->
      if (!hasFocus) {
        var newValue = range.min
        try {
          if (editText.text.isNotEmpty()) {
            val readFovValue = Integer.parseInt(editText.text.toString())
            newValue =
                if (readFovValue in range.min..range.max) {
                  readFovValue
                } else {
                  // set to min/max
                  if (readFovValue > range.max) {
                    range.max
                  } else {
                    range.min
                  }
                }
          }
        } catch (ignore: Exception) {}
        editText.setText(newValue.toString())
        seekBar.progress = range.value - range.min
      }
    }
  }
}
