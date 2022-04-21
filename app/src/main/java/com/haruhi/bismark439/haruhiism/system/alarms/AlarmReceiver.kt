package com.haruhi.bismark439.haruhiism.system.alarms

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import androidx.core.content.ContextCompat
import com.haruhi.bismark439.haruhiism.model.alarmDB.*
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
        const val TIME_PRECISION_IN_SEC = 29
    }

    private var alarmManager: AlarmManager? = null

    override fun onReceive(context: Context, intent: Intent) {
        val reqCode = intent.getIntExtra("requestCode", 0)
        println("Received code:$reqCode")
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        loadAlarm(context, reqCode)
        println("Received Signal:" + intent.action)
    }


    private fun generateIntent(context: Context, alarm: AlarmData): Intent {
        val waker = modifyWaker(alarm.waker)
        val alarmIntent = Intent(context, AlarmDB.AlarmDictionary[waker])
        saveVolume(context, alarm.volume)
        println(alarm.reqCode.toString() + " Received Waker: " + waker)
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
        val today = AlarmFactory.convertCalendarDateToMyDate(now)
        if (!alarm.days.isTrueAt(today)) {
            println("It is not for today")
            return false
        }
        if (abs(System.currentTimeMillis() - alarm.lastTime) <= (TIME_PRECISION_IN_SEC * 1000)) {
            println("We already triggered this today")
            return false
        }
        val alarmInSeconds = (alarm.alarmHours * 60 + alarm.alarmMinutes) * 60
        val nowInSeconds = (now[Calendar.HOUR_OF_DAY] * 60 + now[Calendar.MINUTE]) * 60
        val diff = abs(alarmInSeconds - nowInSeconds)
        println("Time difference : ${diff.toLong().toReadableTime()}")
        if (diff <= TIME_PRECISION_IN_SEC) {
            return true
        }
        println("It is not tim yet")
        return false
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



    private fun onAlarmLoaded(context: Context, alarm: AlarmData) {
        if (!alarm.enabled) return
        if (!isTimeNow(alarm)) return
        println("$alarm is for now")
        val alarmIntent = generateIntent(context, alarm)
        ContextCompat.startActivity(context, alarmIntent, null)
        updateLastAlarmFired(alarm)
    }

    private fun loadAlarm(context: Context, reqCode: Int) {
        AlarmDao.initDao(context)
        GlobalScope.launch {
            val it = AlarmDao.instance.selectOnce(reqCode) ?: return@launch
            println("Collect called $reqCode ${it.lastTime.toReadableTime()}")
            onAlarmLoaded(context, it)
        }
    }

    private fun updateLastAlarmFired(alarm: AlarmData) {
        alarm.lastTime = System.currentTimeMillis()
        GlobalScope.launch {
            AlarmDao.instance.update(alarm)
        }
    }
}