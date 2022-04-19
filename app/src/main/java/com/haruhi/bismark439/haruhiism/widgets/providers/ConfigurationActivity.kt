package com.haruhi.bismark439.haruhiism.widgets.providers

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RemoteViews
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.interfaces.BaseActivity
import com.haruhi.bismark439.haruhiism.databinding.ConfigActivityWidgetBinding
import com.haruhi.bismark439.haruhiism.model.widgetDB.WidgetDB
import com.haruhi.bismark439.haruhiism.model.widgetDB.WidgetData
import kotlinx.coroutines.DelicateCoroutinesApi
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
import java.util.*

/**
 * Created by Bismark439 on 28/02/2018.
 */
@DelicateCoroutinesApi
class ConfigurationActivity :
    BaseActivity<ConfigActivityWidgetBinding>(ConfigActivityWidgetBinding::inflate) {
    private var mAppWidgetId = 0
    var date = IntArray(3)
    var myImg = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WidgetDB.loadWidgets(this) {
            runOnUiThread {
                initialiseWidget()
            }
        }
    }
    private fun initialiseWidget(){
        setResult(RESULT_CANCELED)
        binding.btnCustomColor.setOnClickListener { onCustomColor() }
        binding.okButton.setOnClickListener { onCreateWidget() }
        setDatePicker()
        setSpinner()
        //INITIALISE
        updateExample()
        val extras = intent.extras
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
// If you receive an intent without the appropriate ID, then the system should kill this Activity//
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }
    }
    private fun onCreateWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        //But after 30 min it calls default update
        val wg = WidgetData(
            appWidgetId = mAppWidgetId,
            color = myColor,
            picture = drawables[myImg]
        )
        wg.setDate(date)
        WidgetDB.saveWidget(this@ConfigurationActivity, wg) {
            val ui = WidgetCreater.createUI(applicationContext, wg)
            println(wg)
            appWidgetManager.updateAppWidget(wg.appWidgetId, ui)
            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, wg.appWidgetId)
            setResult(RESULT_OK, resultValue)
            finish()
        }
    }


    private fun setSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dNames)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                myImg = position
                updateExample()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setDatePicker() {
        val today = Date()
        val cal = Calendar.getInstance()
        cal.time = today
        date[0] = cal[Calendar.YEAR]
        date[1] = cal[Calendar.MONTH] + 1
        date[2] = cal[Calendar.DATE]
        val list = arrayOf(binding.dateYear, binding.dateMonth, binding.dateDay)// yy, mm, dd)
        for (i in 0..2) {
            list[i].addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    try {
                        val text = s.toString()
                        date[i] = text.toInt()
                    } catch (e: Exception) {
                        date[i] = 6
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            })
        }


    }

    //Date
    fun onDateUp(v: View) {
        when (val index: Int = v.tag.toString().toInt()) {
            0 -> date[index]++
            1 -> {
                date[index]++
                if (date[index] > 12) {
                    date[index] = 1
                }
            }
            2 -> {
                val cal = Calendar.getInstance()
                cal[date[0], date[1] - 1] = 1 //YY MM DD
                val maxDay = cal.getActualMaximum(Calendar.DATE)
                date[index]++
                if (date[2] > maxDay) {
                    date[2] = 1
                }
            }
        }
        checkValidity()
        updateExample()
    }

    fun onDateDown(v: View) {
        when (val index: Int = v.tag.toString().toInt()) {
            0 -> date[index]--
            1 -> {
                date[index]--
                if (date[index] < 1) {
                    date[index] = 12
                }
            }
            2 -> {
                date[index]--
                val cal = Calendar.getInstance()
                cal[date[0], date[1] - 1] = 1 //YY MM DD
                val maxDay = cal.getActualMaximum(Calendar.DATE)
                if (date[index] < 1) {
                    date[index] = maxDay
                }
            }
        }
        checkValidity()
        updateExample()
    }

    private fun checkValidity() {
        val cal = Calendar.getInstance()
        cal[date[0], date[1] - 1] = 1 //YY MM DD
        println("Validity" + cal[Calendar.MONTH])
        val maxDay = cal.getActualMaximum(Calendar.DATE)
        if (date[2] > maxDay || date[2] < 1) {
            date[2] = maxDay
        }
        if (date[1] > 12 || date[1] < 1) {
            date[1] = 12
        }
    }

    //Color
    private fun onCustomColor() {
        val dialog = AmbilWarnaDialog(this, myColor, true, object : OnAmbilWarnaListener {
            override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                // color is the color selected by the user.
                myColor = color
                updateExample()
            }

            override fun onCancel(dialog: AmbilWarnaDialog) {
                // cancel was selected by the user
            }
        })
        dialog.show()
    }

    fun onPresetColor(v: View?) {
        if (v == null) return
        myColor = Color.parseColor(v.tag as String)
        updateExample()
    }

    fun updateExample() {
        binding.exName.setTextColor(myColor)
        binding.exName.text = binding.dateName.text.toString()
        binding.exDays.setTextColor(myColor)
        binding.exDays2.setTextColor(myColor)
        binding.exImage.setImageResource(drawables[myImg])
        binding.dateYear.setText(date[0].toString())
        binding.dateMonth.setText(date[1].toString())
        binding.dateDay.setText(date[2].toString())
    }


    companion object {
        var myColor = Color.parseColor("#c80000c8")
        var drawables = intArrayOf(
            R.drawable.haruhi_yuutsu,
            R.drawable.haruhi1,
            R.drawable.mikuruw,
            R.drawable.nagato_chan
        )
        var dNames = arrayOf("Haruhi", "Haruhi-Shoshitsu", "Mikuru", "Nagato")

    }
}