package com.haruhi.bismark439.haruhiism.model.alarmDB

import android.content.Context
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.Debugger.DEBUG_IMMEDIATE_ALARM
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.AddAlarmActivity
import com.haruhi.bismark439.haruhiism.system.isTrueAt
import com.haruhi.bismark439.haruhiism.system.ui.Toaster
import java.util.*

object AlarmFactory {


    fun create(hour: Int, minutes: Int, nearestNextTime: Long): AlarmData {
        if (DEBUG_IMMEDIATE_ALARM) {
            val cal = Calendar.getInstance()
            cal.timeInMillis += 10 * 1001
            return AlarmData(
                reqCode = 0,
                alarmHours = cal[Calendar.HOUR_OF_DAY],
                alarmMinutes = cal[Calendar.MINUTE],
                startingTime = cal.timeInMillis
            )
        }
        return AlarmData(
            reqCode = 0,
            alarmHours = hour,
            alarmMinutes = minutes,
            startingTime = nearestNextTime
        )
    }

    fun getNearestNextTimeInMills(hour: Int, minutes: Int): Long {
        var nextTimeInMills: Long = getMillsCalendar(hour, minutes).timeInMillis
        if (System.currentTimeMillis() > nextTimeInMills) {
            nextTimeInMills += (AddAlarmActivity.dayInMills)
        }
        return nextTimeInMills
    }
    fun appendDayToTime(hour: Int, minutes: Int): Long {
        val calendar = getMillsCalendar(hour, minutes)
        calendar.timeInMillis+=AddAlarmActivity.dayInMills
        return calendar.timeInMillis
    }
    fun getMillsCalendar(hour: Int, minutes: Int): Calendar {
        val calendar = Calendar.getInstance() //Alarm time
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minutes
        calendar[Calendar.SECOND] = 0
        return calendar
    }
    fun getNextAlarmString( context : Context, alarm: AlarmData):String{
        val cal = Calendar.getInstance()
        cal.timeInMillis = alarm.startingTime
        val nextDate = convertCalendarDateToMyDate(cal)
        if (!alarm.days.isTrueAt(nextDate)) {
            return  context.getString(R.string.txt_no_alarm_tomorrow)
        }
        var timeDiffInSec = (alarm.startingTime - System.currentTimeMillis()) / 1000
        val hours = timeDiffInSec / (3600)
        timeDiffInSec -= hours * 3600
        val minutes = timeDiffInSec / 60
        timeDiffInSec -= minutes * 60
        var msg = ""
        if (hours > 0) {
            msg += "${hours}${context.resources.getString((R.string.hours))} "
        }
        if (minutes > 0) {
            msg += "${minutes}${context.resources.getString((R.string.minute))} "
        }
        if (timeDiffInSec <= 0) {
            timeDiffInSec = 0
        }
        msg += "${timeDiffInSec}${context.resources.getString(R.string.seconds)}"
        return context.resources.getString(R.string.toast_next_alarm, msg)
    }
    fun showNextAlarmTime(context: Context, alarm: AlarmData) {
        val msg = getNextAlarmString(context, alarm)
        Toaster.show(context, msg)
    }

    fun convertCalendarDateToMyDate(calendarDate: Calendar): Int {
        return when (calendarDate[Calendar.DAY_OF_WEEK]) {
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            Calendar.SUNDAY -> 6
            else -> {
                Debugger.log("Date Error")
                -1
            }
        }
    }

}