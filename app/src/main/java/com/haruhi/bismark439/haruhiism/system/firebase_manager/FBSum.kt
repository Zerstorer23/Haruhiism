package com.haruhi.bismark439.haruhiism.system.firebase_manager

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
data class FBSum(var sums: Long = 0) :Parcelable {
    constructor(parcel: Parcel) : this(parcel.readLong())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(sums)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FBSum> {
        override fun createFromParcel(parcel: Parcel): FBSum {
            return FBSum(parcel)
        }

        override fun newArray(size: Int): Array<FBSum?> {
            return arrayOfNulls(size)
        }
    }
}

