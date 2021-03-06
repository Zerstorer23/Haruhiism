@file:OptIn(DelicateCoroutinesApi::class)

package com.haruhi.bismark439.haruhiism.activities

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TimePicker
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.interfaces.BaseActivity
import com.haruhi.bismark439.haruhiism.databinding.ActivityAddAlarmBinding
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmDB
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmFactory
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmWakers
import com.haruhi.bismark439.haruhiism.system.PermissionManager
import com.haruhi.bismark439.haruhiism.system.SpinnerFactory
import com.haruhi.bismark439.haruhiism.system.alarms.SoundPlayer.Companion.DEFAULT_VOLUME
import com.haruhi.bismark439.haruhiism.system.isTrueAt
import com.haruhi.bismark439.haruhiism.system.replaceAt
import kotlinx.coroutines.DelicateCoroutinesApi


class AddAlarmActivity : BaseActivity<ActivityAddAlarmBinding>(ActivityAddAlarmBinding::inflate) {
    private lateinit var alarmTimePicker: TimePicker
    private lateinit var minSpin: Spinner
    private lateinit var timeSpin: Spinner
    var snzMinutes = arrayOf("3", "5", "10", "15", "30")
    var snzTimes = arrayOf("3", "2", "1", "5", "10")
    var days = "1111100"
    private var repeat = false
    private var vibration = true
    private var songname = "alarm1.mp3"
    private var alarmname = "Alarm"
    var sMin = 0
    var sTimes = 0


    private fun linkUI() {
        alarmTimePicker = binding.timePicker //findViewById(R.id.timePicker) as TimePicker?
        //Repeat Box
        binding.aRepeatCheck.setOnCheckedChangeListener { _, isChecked -> repeat = isChecked }
        setUpSpinner()

        //Vib Check
        //Repeat Box
        binding.aVibrate.setOnCheckedChangeListener { _, isChecked -> vibration = isChecked }
        //Volume
        binding.aVolseek.progress = DEFAULT_VOLUME.toInt()


        //
        binding.btnSelectSong.setOnClickListener { songname = "alarm1.mp3" }
        binding.btnCancel.setOnClickListener { finish() }
        binding.btnAddAlarm.setOnClickListener { onDone() }
    }

    private fun setUpSpinner() {
        //Minutes Interval
        SpinnerFactory.createSpinner<String>(applicationContext, snzMinutes, binding.aSnzMinutes) {
            sMin = snzMinutes[it].toInt()
        }
        SpinnerFactory.createSpinner<String>(applicationContext, snzTimes, binding.aSnzTimes) {
            sTimes = snzTimes[it].toInt()
        }
        val nameArray = (wakerNames.map { res -> resources.getString(res) }).toTypedArray()
        SpinnerFactory.createSpinner<String>(applicationContext, nameArray, binding.wakerSpinner) {
        }

        //
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        linkUI()
        alarmname = binding.aNameEdit.text.toString()
        alarmTimePicker.setIs24HourView(false)
    }

    fun onClickDay(v: View) {
        val tt = v.tag as String
        val t = tt.toInt()
        if (days.isTrueAt(t)) {
            days = days.replaceAt(t, '0')
            v.setBackgroundResource(R.drawable.my_border)
        } else {
            days = days.replaceAt(t, '1')
            v.setBackgroundResource(R.drawable.my_border_green)
        }
    }

    private fun getSelectedWaker(): AlarmWakers {
        return when (binding.wakerSpinner.selectedItemPosition) {
            0 -> AlarmWakers.Haruhi
            1 -> AlarmWakers.Random
            2 -> AlarmWakers.KyonSister
            3 -> AlarmWakers.MikuruOcha
            4 -> AlarmWakers.Koizumi
            5 -> AlarmWakers.MikuruPuzzle
            else -> AlarmWakers.Haruhi
        }
    }

    @DelicateCoroutinesApi
    private fun onDone() {
            pushInfo()
    }

    private fun pushInfo() {
        val hour = alarmTimePicker.hour
        val minutes = alarmTimePicker.minute
        val nearestTimeInMills = AlarmFactory.getNearestNextTimeInMills(hour, minutes)
        val newAlarm = AlarmFactory.create(hour, minutes, nearestTimeInMills)
        newAlarm.days = days
        newAlarm.waker = getSelectedWaker()
        newAlarm.volume = binding.aVolseek.progress
        newAlarm.enabled = true
        AlarmFactory.showNextAlarmTime(applicationContext, newAlarm)
        val data = Intent()
        data.putExtra(AlarmDB.ALARM_INTENT_DATA_LABEL, newAlarm)
        setResult(RESULT_OK, data)
        finish()
    }


    companion object {
        const val dayInMills: Long = 1000 * 60 * 60 * 24
        val wakerNames = arrayOf(
            R.string.haruhi_no_puzzle,
            R.string.random,
            R.string.kyon_s_sister, R.string.mikurunotea,
            R.string.koizumi_and_sos,
            R.string.mikuruJinsoku
        )

    }


}

