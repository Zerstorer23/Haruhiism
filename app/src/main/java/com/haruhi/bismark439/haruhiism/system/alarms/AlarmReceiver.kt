package com.haruhi.bismark439.haruhiism.system.alarms

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmDB
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmDao
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmData
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmWakers
import com.haruhi.bismark439.haruhiism.system.alarms.SoundPlayer.Companion.currentVolume
import com.haruhi.bismark439.haruhiism.system.isTrueAt
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.abs

/**
 * Created by Bismark439 on 13/01/2018.
 */
@DelicateCoroutinesApi
class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val DUPLICATE_TIME_BOUND_IN_SEC = 30
    }

    private var alarmManager: AlarmManager? = null

    override fun onReceive(context: Context, intent: Intent) {
        val reqCode = intent.getIntExtra("requestCode", 0)
        println("Received code:$reqCode")
       // Toast.makeText(context, "알람이 울립니다...[${reqCode}]", Toast.LENGTH_LONG).show()
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
        println("$volume And raw volume~ $newVolume")
    }

    private fun isTimeNow(alarm: AlarmData): Boolean {
        val today = getTodayDate()
        if (!alarm.days.isTrueAt(today)) return false
        val todayDate = Date()
        val now = Calendar.getInstance() //TimeZone.getTimeZone("GMT"));
        if (abs(now.timeInMillis - alarm.lastTime) <= DUPLICATE_TIME_BOUND_IN_SEC) return false

        // Set as today
        now.time = todayDate
        println("Received time " + alarm.alarmHours + ":" + alarm.alarmMinutes + " vs " + now[Calendar.HOUR_OF_DAY] + ":" + now[Calendar.MINUTE])
        val alarmInSeconds = (alarm.alarmHours * 60 + alarm.alarmMinutes) * 60
        val nowInSeconds = (now[Calendar.HOUR_OF_DAY] * 60 + now[Calendar.MINUTE]) * 60

        val diff = abs(alarmInSeconds - nowInSeconds)
        if (diff <= DUPLICATE_TIME_BOUND_IN_SEC) {
            println("Time difference : $diff")
            return true
        }
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
    private fun getTodayDate(): Int {
        val calendar = Calendar.getInstance()
        val dayNum = calendar[Calendar.DAY_OF_WEEK] - 1 //Offset -1
        //Sun ~ Mon = 0 ~ 6
        return if (dayNum == 0) {
            6
        } else {
            dayNum
        }
    }


    private fun onAlarmLoaded(context: Context, alarm: AlarmData) {
        println("Found: $alarm")
        if(!alarm.enabled) return
        if (!isTimeNow(alarm)) return
        val alarmIntent = generateIntent(context, alarm)
        ContextCompat.startActivity(context, alarmIntent, null)
    }

    private fun loadAlarm(context: Context, reqCode: Int) {
        AlarmDao.initDao(context)
        GlobalScope.launch {
            AlarmDao.instance.select(reqCode).collect {
                if(it == null) return@collect
                onAlarmLoaded(context, it)
            }
        }
    }

}