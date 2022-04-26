package com.haruhi.bismark439.haruhiism.system.alarms

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import androidx.core.content.ContextCompat
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.model.alarmDB.*
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmFactory
import com.haruhi.bismark439.haruhiism.system.alarms.SoundPlayer.Companion.currentVolume
import com.haruhi.bismark439.haruhiism.system.isTrueAt
import com.haruhi.bismark439.haruhiism.system.toReadableTime
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.abs

/**
 * Created by Bismark439 on 13/01/2018.
 */
@DelicateCoroutinesApi
class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val TIME_PRECISION_IN_MILLS = (90) * 1000L
    }

    private var alarmManager: AlarmManager? = null

    override fun onReceive(context: Context, intent: Intent) {
        val reqCode = intent.getIntExtra("requestCode", 0)
        Debugger.log("Received code:$reqCode")
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        loadAlarm(context, reqCode)
        Debugger.log("Received Signal:" + intent.action)
    }


    private fun generateIntent(context: Context, alarm: AlarmData): Intent {
        val waker = modifyWaker(alarm.waker)
        val alarmIntent = Intent(context, AlarmDB.AlarmDictionary[waker])
        saveVolume(context, alarm.volume)
        Debugger.log(alarm.reqCode.toString() + " Received Waker: " + waker)
        alarmIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return alarmIntent
    }

    private fun saveVolume(context: Context, newVolume: Int) {
        val audio = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
        val volume =
            (newVolume.toFloat() / 15 * audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                .toFloat()).toInt()
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
    }

    private fun isTimeNow(alarm: AlarmData): Boolean {
        val now = Calendar.getInstance() //Today
        val alarmCal = AlarmFactory.getMillsCalendar(alarm.alarmHours, alarm.alarmMinutes)

        val today = AlarmFactory.convertCalendarDateToMyDate(now)
        if (!alarm.days.isTrueAt(today)) {
            Debugger.log("It is not for today")
            return false
        }
        if (abs(System.currentTimeMillis() - alarm.lastTime) <= (TIME_PRECISION_IN_MILLS)) {
            Debugger.log("We already triggered this today")
            return false
        }

/*        val alarmInSeconds = (alarm.alarmHours * 60 + alarm.alarmMinutes) * 60
        val nowInSeconds = (now[Calendar.HOUR_OF_DAY] * 60 + now[Calendar.MINUTE]) * 60*/
        val diff = abs(alarmCal.timeInMillis - System.currentTimeMillis())
        Debugger.log(
            "Time: ${alarmCal.timeInMillis.toReadableTime()} vs ${
                System.currentTimeMillis().toReadableTime()
            }"
        )
        Debugger.log("Time difference : ${diff.toLong()} vs $TIME_PRECISION_IN_MILLS")
        return if (diff <= TIME_PRECISION_IN_MILLS) {
            true
        } else {
            Debugger.log("It is not tim yet")
            false
        }
    }

    private fun modifyWaker(waker: AlarmWakers): AlarmWakers {
        var newWaker = waker
        if (waker == AlarmWakers.Random) {
            val randomNumber = (Math.random() * AlarmWakers.values().size - 1).toInt() + 1 //1~End
            newWaker = AlarmWakers.values()[randomNumber]
            if (newWaker == AlarmWakers.MikuruPuzzle) {
                newWaker = AlarmWakers.MikuruOcha
            } //무작위에서 2 제외. 강제로 4
        }
        if (!AlarmDB.AlarmDictionary.containsKey(newWaker))
            newWaker = AlarmWakers.KyonSister
        return newWaker
    }

    //Sunday


    private fun onAlarmLoaded(context: Context, alarm: AlarmData): AlarmReceivedCode {
        if (!alarm.enabled) return AlarmReceivedCode.NotEnabled
        if (!isTimeNow(alarm)) return AlarmReceivedCode.NotTimeYet
        Debugger.log("$alarm is for now")
        val alarmIntent = generateIntent(context, alarm)
        ContextCompat.startActivity(context, alarmIntent, null)
        return AlarmReceivedCode.Success
    }

    private fun loadAlarm(context: Context, reqCode: Int) {
        AlarmDao.initDao(context)
        GlobalScope.launch {
            val it = AlarmDao.instance.selectOnce(reqCode) ?: return@launch
            Debugger.log("Collect called $reqCode ${it.lastTime.toReadableTime()}")
            when (onAlarmLoaded(context, it)) {
                AlarmReceivedCode.Success -> {
                    updateLastAlarmFired(it, true)
                    AlarmDB.registerAlarm(context, it)
                }
                AlarmReceivedCode.NotTimeYet -> {
                    updateLastAlarmFired(it, false)
                    AlarmDB.registerAlarm(context, it)
                }
                AlarmReceivedCode.NotEnabled -> {

                }
            }
        }
    }

    private fun updateLastAlarmFired(alarm: AlarmData, fired: Boolean) {
        if (fired) {
            alarm.lastTime = AlarmFactory.getMillsCalendar(alarm.alarmHours, alarm.alarmMinutes).timeInMillis
            alarm.startingTime = AlarmFactory.appendDayToTime(alarm.alarmHours, alarm.alarmMinutes)
        } else {
            alarm.startingTime =
                AlarmFactory.getNearestNextTimeInMills(alarm.alarmHours, alarm.alarmMinutes)
        }
        GlobalScope.launch {
            AlarmDao.instance.update(alarm)
        }
    }
}

enum class AlarmReceivedCode {
    Success, NotTimeYet, NotEnabled
}