package com.haruhi.bismark439.haruhiism.model.alarmDB

import com.haruhi.bismark439.haruhiism.DEBUG.DEBUG_IMMEDIATE_ALARM
import java.util.*

object AlarmFactory {


    fun create( hour:Int, minutes:Int, nearestNextTime: Long): AlarmData {
        if(DEBUG_IMMEDIATE_ALARM){
            val cal = Calendar.getInstance()
            cal.timeInMillis+=10*1001
            return AlarmData(reqCode = 0,
                alarmHours = cal[Calendar.HOUR_OF_DAY],
                alarmMinutes = cal[Calendar.MINUTE],
                startingTime = cal.timeInMillis)
        }
        return AlarmData(reqCode = 0,
            alarmHours = hour,
            alarmMinutes = minutes,
            startingTime = nearestNextTime)
    }



}