package com.haruhi.bismark439.haruhiism.model.widgetDB

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haruhi.bismark439.haruhiism.system.VoidReturn
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Bismark439 on 14/01/2018.
 */
class WidgetDB {
    @DelicateCoroutinesApi
    companion object {
        //var widgetDB: ArrayList<WidgetData> = arrayListOf()
        private var widgetDB: HashMap<Int, WidgetData> = hashMapOf()// arrayListOf()

        private fun buildHashMap(it: List<WidgetData>) {
            widgetDB.clear()
            for (wg in it) {
                widgetDB[wg.appWidgetId] = wg
            }
        }

        fun getSize(): Int {
            return widgetDB.size
        }


        fun get(appId: Int): WidgetData? {
            return widgetDB[appId]
        }

        fun loadWidgets(context: Context, onResult: VoidReturn) {
            WidgetDao.initDao(context)
            GlobalScope.launch {
                WidgetDao.instance.selectAll().collect {
                    println("Widgets loaded: "+widgetDB.size)
                    for( i in widgetDB){
                        println(i)
                        print("==")
                    }
                    buildHashMap(it)
                    onResult()
                }
            }
        }

        fun deleteWidget(context: Context, widget :WidgetData) {
            WidgetDao.initDao(context)
            GlobalScope.launch {
                WidgetDao.instance.delete(widget)
            }
        }

        fun saveWidget(context: Context, temp: WidgetData, onResult:VoidReturn) {
            WidgetDao.initDao(context)
            GlobalScope.launch {
                WidgetDao.instance.insert(temp)
                onResult()
            }
        }
    }
}


@Entity(tableName = "widget-table")
data class WidgetData
    (
    @PrimaryKey(autoGenerate = false)
    val appWidgetId: Int = 0,
    val name: String = " ",
    var yy: Int = 0,
    var mmMod: Int = 0,
    var dd: Int = 0,
    var color: Int = 0,
    var picture: Int = 0,
    var filePath: String = ""
):Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("=====PRINTING WIDGET DATA: =====\n")
        sb.append("ID: $appWidgetId Name: $name  \n")
        sb.append("Y: $yy M: $mmMod D: $dd \n")
        sb.append("Color : #$color \n")
        sb.append("============================ \n")
        return sb.toString()
    }

    fun setDate(date: IntArray) {
        yy = date[0]
        mmMod = date[1] - 1
        dd = date[2]
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(appWidgetId)
        parcel.writeString(name)
        parcel.writeInt(yy)
        parcel.writeInt(mmMod)
        parcel.writeInt(dd)
        parcel.writeInt(color)
        parcel.writeInt(picture)
        parcel.writeString(filePath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WidgetData> {
        override fun createFromParcel(parcel: Parcel): WidgetData {
            return WidgetData(parcel)
        }

        override fun newArray(size: Int): Array<WidgetData?> {
            return arrayOfNulls(size)
        }
    }
}