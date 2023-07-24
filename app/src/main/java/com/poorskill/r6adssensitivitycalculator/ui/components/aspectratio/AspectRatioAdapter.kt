package com.poorskill.r6adssensitivitycalculator.ui.components.aspectratio

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.poorskill.r6adssensitivitycalculator.R
import com.poorskill.r6adssensitivitycalculator.converter.data.AspectRatio
import com.poorskill.r6adssensitivitycalculator.converter.data.AspectRatios

class AspectRatioAdapter(context: Context) : BaseAdapter() {

  private val dataList = AspectRatios.getAll()

  private val inflater: LayoutInflater =
      context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val view: View
    val vh: ItemHolder
    if (convertView == null) {
      view = inflater.inflate(R.layout.aspect_spinner_item, parent, false)
      vh = ItemHolder(view)
      view?.tag = vh
    } else {
      view = convertView
      vh = view.tag as ItemHolder
    }
    vh.text.text = dataList[position].name

    return view
  }

  override fun getItem(position: Int): AspectRatio {
    return dataList[position]
  }

  override fun getCount(): Int {
    return dataList.size
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  private class ItemHolder(row: View?) {
    val text: TextView = row?.findViewById(R.id.aspectSpinnerText) as TextView
  }
}
