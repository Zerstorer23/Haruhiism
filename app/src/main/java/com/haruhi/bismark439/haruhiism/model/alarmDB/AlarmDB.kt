package com.haruhi.bismark439.haruhiism.model.alarmDB

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.alarm_screens.HaruhiAlarmScreen
import com.haruhi.bismark439.haruhiism.activities.alarm_screens.KoizumiAlarmScreenActivity
import com.haruhi.bismark439.haruhiism.activities.alarm_screens.KyonSisterAlarmActivity
import com.haruhi.bismark439.haruhiism.activities.alarm_screens.mikuru_alarms.MikuruOchaAlarmActivity
import com.haruhi.bismark439.haruhiism.activities.alarm_screens.mikuru_alarms.MikuruPuzzleAlarmActivity
import com.haruhi.bismark439.haruhiism.system.alarms.AlarmReceiver
import kotlinx.coroutines.DelicateCoroutinesApi

enum class AlarmWakers {
    Random, KyonSister, MikuruPuzzle, Koizumi, MikuruOcha, Haruhi
}

class AlarmDB {
    @DelicateCoroutinesApi
    companion object {
        const val ALARM_INTENT_DATA_LABEL = "alarm"
        private const val ALARM_REQ_CODE_TITLE = "requestCode"
        val AlarmDictionary = hashMapOf(
            AlarmWakers.KyonSister to KyonSisterAlarmActivity::class.java,
            AlarmWakers.MikuruPuzzle to MikuruPuzzleAlarmActivity::class.java,
            AlarmWakers.Koizumi to KoizumiAlarmScreenActivity::class.java,
            AlarmWakers.MikuruOcha to MikuruOchaAlarmActivity::class.java,
            AlarmWakers.Haruhi to HaruhiAlarmScreen::class.java
        )

        fun getWakerStringName(wakers: AlarmWakers): Int {
            return when (wakers) {
                AlarmWakers.Random -> R.string.random
                AlarmWakers.KyonSister -> R.string.kyonsis
                AlarmWakers.MikuruPuzzle -> R.string.asahinaMikuru
                AlarmWakers.Koizumi -> R.string.koizumi_and_sos
                AlarmWakers.MikuruOcha -> R.string.mikurunotea
                AlarmWakers.Haruhi -> R.string.hrhNoPuzzle
            }
        }


        var alarmDB: ArrayList<AlarmData> = arrayListOf()

        fun safeRegisterAllAlarms(context: Context) {
            for (alarm in alarmDB) {
                registerAlarm(context, alarm)
                println("Register alarm ${alarm.reqCode}")
            }
        }

        fun printDB() {
//            DEBUG.printStack()
            Debugger.log("===Alarm size : ${alarmDB.size}")
            for (alarm in alarmDB) {
                Debugger.log(alarm.toString())
            }
        }

        @SuppressLint("ShortAlarm")
        fun registerAlarm(mContext: Context, alarm: AlarmData) {
            if (!alarm.enabled) return
            val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
 /*           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val hasPermission: Boolean = alarmManager.canScheduleExactAlarms()
                println("Has permission schedule : $hasPermission")
            }*/
            val intent = Intent(mContext, AlarmReceiver::class.java)
            val reqCode = alarm.reqCode
            val time: Long = alarm.startingTime
            intent.putExtra(ALARM_REQ_CODE_TITLE, reqCode)
            Debugger.log("Sent code[ADD]:$reqCode")
            Debugger.log("$alarm")
            println("Next alarm  = "+AlarmFactory.getNextAlarmString(mContext, alarm))
            val pi = PendingIntent.getBroadcast(
                mContext,
                reqCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            ) //getactivity

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                time,
                AlarmManager.INTERVAL_DAY,
                pi
            )
        }


        fun disableAlarm(mContext: Context, temp: AlarmData) {
            val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(mContext, AlarmReceiver::class.java)
            val reqCode = temp.reqCode
            intent.putExtra(ALARM_REQ_CODE_TITLE, reqCode)
            val pi =
                PendingIntent.getBroadcast(
                    mContext,
                    reqCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            pi.cancel()
            alarmManager.cancel(pi)
            Debugger.log("Disabled code:$reqCode")
        }


        fun removeAlarm(mContext: Context, reqCode: Int) {
            val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(mContext, AlarmReceiver::class.java)
            intent.putExtra(ALARM_REQ_CODE_TITLE, reqCode)
            val pi = PendingIntent.getBroadcast(
                mContext,
                reqCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            pi.cancel()
            alarmManager.cancel(pi)
        }

    }
}
