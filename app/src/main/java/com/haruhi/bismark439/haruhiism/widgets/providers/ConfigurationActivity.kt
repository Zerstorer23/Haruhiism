package com.haruhi.bismark439.haruhiism.widgets.providers

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.haruhi.bismark439.haruhiism.DEBUG
import com.haruhi.bismark439.haruhiism.activities.interfaces.BaseActivity
import com.haruhi.bismark439.haruhiism.databinding.ConfigActivityWidgetBinding
import com.haruhi.bismark439.haruhiism.model.widgetDB.*
import com.haruhi.bismark439.haruhiism.system.SpinnerFactory
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
    val widgetData = WidgetData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkValid()
        widgetData.color = Color.parseColor("#c80000c8")
        widgetData.widgetCharacter = WidgetCharacter.Haruhi
        widgetData.appWidgetId = mAppWidgetId
        initialiseWidget()
    }


    private fun initialiseWidget() {
        binding.btnCustomColor.setOnClickListener { onCustomColor() }
        binding.okButton.setOnClickListener { onCreateWidget() }
        setDatePicker()
        val nameList = WidgetCharacter.values().map{ch->getString(ch.toNameRes())}.toTypedArray()

        SpinnerFactory.createSpinner(
            applicationContext,
            nameList,
            binding.spinner
        ) {
            widgetData.widgetCharacter = WidgetCharacter.values()[it]
            updateExample()
        }
        //INITIALISE
        updateExample()
    }

    private fun onCreateWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        //But after 30 min it calls default update
        widgetData.name = binding.eventTitle.text.toString()
        widgetData.picture = widgetData.widgetCharacter.toCharacterImg()
        widgetData.setDate(date)
        WidgetDB.saveWidget(this@ConfigurationActivity, widgetData) {
            val ui = WidgetCreater.createUI(applicationContext, widgetData)
            DEBUG.appendLog(widgetData.toString())
            appWidgetManager.updateAppWidget(widgetData.appWidgetId, ui)
            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetData.appWidgetId)
            setResult(RESULT_OK, resultValue)
            finish()
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
        DEBUG.appendLog("Validity" + cal[Calendar.MONTH])
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
        val dialog = AmbilWarnaDialog(this, widgetData.color, true, object : OnAmbilWarnaListener {
            override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                // color is the color selected by the user.
                widgetData.color = color
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
        try {
            widgetData.color = Color.parseColor(v.tag as String)
            updateExample()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateExample() {
        binding.exName.setTextColor(widgetData.color)
        binding.exName.text = binding.eventTitle.text
        binding.exDays.setTextColor(widgetData.color)
        binding.exDays2.setTextColor(widgetData.color)
        binding.exImage.setImageResource(widgetData.widgetCharacter.toCharacterImg())
        binding.dateYear.setText(date[0].toString())
        binding.dateMonth.setText(date[1].toString())
        binding.dateDay.setText(date[2].toString())
    }

    private fun checkValid() {
        setResult(RESULT_CANCELED)
        val extras = intent.extras
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }
    }

}