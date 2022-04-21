package com.haruhi.bismark439.haruhiism.model.alarmDB

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.widget.ToggleButton
import com.haruhi.bismark439.haruhiism.activities.interfaces.ItemsAdaptor
import com.haruhi.bismark439.haruhiism.databinding.ItemAlarmViewBinding
import com.haruhi.bismark439.haruhiism.system.Helper
import com.haruhi.bismark439.haruhiism.system.Helper.convertDec
import com.haruhi.bismark439.haruhiism.system.isTrueAt
import kotlinx.coroutines.DelicateCoroutinesApi


typealias updateFunction = (alarmData: AlarmData, position: Int) -> Unit
@DelicateCoroutinesApi
class AlarmAdapter(
    context: Context,
    items: ArrayList<AlarmData>,
    private val removeAlarm: updateFunction,
    private val updateAlarm: updateFunction
) : ItemsAdaptor<AlarmData, ItemAlarmViewBinding>(context, items, ItemAlarmViewBinding::inflate) {
    override fun onBindView(binding: ItemAlarmViewBinding, position: Int, model: AlarmData) {
        setUpToggle(binding, model, position)
        setData(binding, model)
        binding.root.setOnLongClickListener {
            Helper.vibrate(context, 200)
            removeAlarm(model, position)
            true
        }
    }

    private fun setUpToggle(
        binding: ItemAlarmViewBinding,
        model: AlarmData,
        position: Int
    ) {
        binding.crToggle.setOnClickListener {
            if (it !is ToggleButton) return@setOnClickListener
            if (it.tag != null && it.tag.equals("init")) return@setOnClickListener
            model.enabled = it.isChecked
            if (it.isChecked) {
                AlarmDB.registerAlarm(context, model)
            } else {
                AlarmDB.disableAlarm(context, model)
            }
            updateAlarm(model, position)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setData(
        binding: ItemAlarmViewBinding,
        alarmData: AlarmData
    ): Boolean {
        val hh = convertDec(alarmData.alarmHours)
        val mm = convertDec(alarmData.alarmMinutes)
        binding.crTime.text = "$hh:$mm"
        val wakerName = context.getString(AlarmDB.getWakerStringName(alarmData.waker))
        binding.crRepeat.text = wakerName
        if (alarmData.enabled != binding.crToggle.isChecked) {
            binding.crToggle.tag = "init"
            binding.crToggle.toggle()
            binding.crToggle.tag = ""
        }
        val days = arrayOf(
            binding.dayMon,
            binding.dayTue,
            binding.dayWed,
            binding.dayThu,
            binding.dayFri,
            binding.daySat,
            binding.daySun
        )
        for (i in 0..days.lastIndex) {
            val day = days[i]
            if (alarmData.days.isTrueAt(i)) {
                day.setTextColor(Color.parseColor("#00c800"))
            } else {
                day.setTextColor(Color.parseColor("#000000"))
            }
        }
        return true
    }



}