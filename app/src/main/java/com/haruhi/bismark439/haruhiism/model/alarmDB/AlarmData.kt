package com.haruhi.bismark439.haruhiism.model.alarmDB

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haruhi.bismark439.haruhiism.system.toReadableTime

/**
 * Created by Bismark439 on 14/01/2018.
 */

@Entity(tableName = "alarm-table")
data class AlarmData(
    @PrimaryKey(autoGenerate = true)
    var reqCode: Int = 0,
    var days: String = "0000000",
    var repeat: Boolean = false,
    var vibration: Boolean = true,
    var songname: String = "alarm1.mp3",
    var alarmname: String = "Alarm",
    var sMin: Int = 0,
    var sTimes: Int = 0,
    var volume: Int = 12,
    var startingTime: Long = 0,
    var lastTime: Long = 0,
    var alarmHours: Int = 0,
    var alarmMinutes: Int = 0,
    var enabled: Boolean = false,
    var waker: AlarmWakers = AlarmWakers.KyonSister,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        AlarmWakers.values()[parcel.readInt()]
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(reqCode)
        parcel.writeString(days)
        parcel.writeByte(if (repeat) 1 else 0)
        parcel.writeByte(if (vibration) 1 else 0)
        parcel.writeString(songname)
        parcel.writeString(alarmname)
        parcel.writeInt(sMin)
        parcel.writeInt(sTimes)
        parcel.writeInt(volume)
        parcel.writeLong(startingTime)
        parcel.writeLong(lastTime)
        parcel.writeInt(alarmHours)
        parcel.writeInt(alarmMinutes)
        parcel.writeByte(if (enabled) 1 else 0)
        parcel.writeInt(waker.ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("=====PRINTING ALARM DATA: =====\n")
        sb.append("${alarmHours} :  $alarmMinutes  Code: $reqCode  Enabled: $enabled \n")
        sb.append("Last: ${lastTime.toReadableTime()} :  $alarmMinutes  Days: $days \n")
        sb.append("======================\n")
        return sb.toString()
    }

    companion object CREATOR : Parcelable.Creator<AlarmData> {
        override fun createFromParcel(parcel: Parcel): AlarmData {
            return AlarmData(parcel)
        }

        override fun newArray(size: Int): Array<AlarmData?> {
            return arrayOfNulls(size)
        }
    }
}