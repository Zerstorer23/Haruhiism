package com.haruhi.bismark439.haruhiism.system.firebase_manager

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.haruhi.bismark439.haruhiism.DEBUG
import com.haruhi.bismark439.haruhiism.system.firebase_manager.ViewDatabaseHandler.COLLECTION_SUM
import com.haruhi.bismark439.haruhiism.system.firebase_manager.ViewDatabaseHandler.FIELD_LASTTIME
import com.haruhi.bismark439.haruhiism.system.firebase_manager.ViewDatabaseHandler.FIELD_NAME
import com.haruhi.bismark439.haruhiism.system.firebase_manager.ViewDatabaseHandler.FIELD_VIEWS

@Keep
data class FBuser(
    var name :String = "ㅇㅇ",
    var views : Long = 0,
    var lastUpdate : Long = System.currentTimeMillis(),
) :Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readLong()
    )

    fun toHashMap():HashMap<String,Any>{
        val map = HashMap<String, Any>()
        map[FIELD_NAME] = name
        map[FIELD_VIEWS] =views
        map[FIELD_LASTTIME] =lastUpdate
        return map
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeLong(views)
        parcel.writeLong(lastUpdate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FBuser> {
        override fun createFromParcel(parcel: Parcel): FBuser {
            return FBuser(parcel)
        }

        override fun newArray(size: Int): Array<FBuser?> {
            return arrayOfNulls(size)
        }
    }
}
object MyUser{
    var user :FBuser = FBuser()
    var totalViews :Long= 0
    fun toSumMap():HashMap<String,Any>{
        val map = HashMap<String,Any>()
        map[COLLECTION_SUM]= totalViews
        return map
    }
    fun incrementViews(){
        user.views++
        totalViews++
        DEBUG.appendLog("New view ${user.views} / $totalViews")
        user.lastUpdate=System.currentTimeMillis()
    }
}