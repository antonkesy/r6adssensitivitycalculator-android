package com.poorskill.r6adssensitivitycalculator.converter.data

import kotlin.properties.Delegates

class RangedValue(
    val min: Int,
    val max: Int,
    value: Int,
    private val onChange: ((Int) -> Unit)? = null
) {

  var value: Int by Delegates.observable(value) { _, _, new -> onChange?.invoke(new) }
}
