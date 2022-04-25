package com.haruhi.bismark439.haruhiism.system

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner


object SpinnerFactory {
    fun <T> createSpinner(context: Context,
                                  list: Array<T>, spinnerView: Spinner, onSelected: (position: Int) -> Unit
    ) {
        val adapter = ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item, list)
        //val adapter = ArrayAdapter(context,android.R.layout.view_spinner_row, list)
        spinnerView.adapter = adapter
        spinnerView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                onSelected( position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

}