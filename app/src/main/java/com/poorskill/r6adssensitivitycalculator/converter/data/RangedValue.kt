package com.poorskill.r6adssensitivitycalculator.converter.data

import kotlin.properties.Delegates

class RangedValue<T>(
    val min: T,
    val max: T,
    value: T,
    private val onChange: ((T) -> Unit)? = null
) {

  var value: T by Delegates.observable(value) { _, _, new -> onChange?.invoke(new) }
}
