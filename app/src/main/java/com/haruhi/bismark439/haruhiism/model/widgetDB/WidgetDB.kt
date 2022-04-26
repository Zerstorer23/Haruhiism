package com.haruhi.bismark439.haruhiism.model.widgetDB

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.system.Constants.FOLDER_HARUHI
import com.haruhi.bismark439.haruhiism.system.Constants.FOLDER_HARUHI_SHOSITSU
import com.haruhi.bismark439.haruhiism.system.Constants.FOLDER_MIKURU
import com.haruhi.bismark439.haruhiism.system.Constants.FOLDER_MIKURU_BIG
import com.haruhi.bismark439.haruhiism.system.Constants.FOLDER_NAGATO
import com.haruhi.bismark439.haruhiism.system.Constants.FOLDER_NAGATO_SHOSITSU
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
                val it = WidgetDao.instance.selectAllOnce()
                Debugger.log("Widgets loaded: " + widgetDB.size)
                for (i in widgetDB) {
                    Debugger.log(i.toString())
                    Debugger.log("==")
                }
                buildHashMap(it)
                onResult()
            }
        }


        fun deleteWidget(context: Context, widget: WidgetData) {
            WidgetDao.initDao(context)
            GlobalScope.launch {
                WidgetDao.instance.delete(widget)
            }
        }
        fun deleteWidget(context: Context, code:Int) {
            WidgetDao.initDao(context)
            GlobalScope.launch {
                WidgetDao.instance.deleteById(code)
            }
        }

        fun saveWidget(context: Context, temp: WidgetData, onResult: VoidReturn) {
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
    var appWidgetId: Int = 0,
    var name: String = " ",
    var yy: Int = 0,
    var mmMod: Int = 0,
    var dd: Int = 0,
    var color: Int = 0,
    var picture: Int = 0,
    var widgetCharacter: WidgetCharacter = WidgetCharacter.Haruhi
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        WidgetCharacter.values()[parcel.readInt()]
    )

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("=====PRINTING WIDGET DATA: =====\n")
        sb.append("ID: $appWidgetId Name: $name  \n")
        sb.append("Y: $yy M: $mmMod D: $dd \n")
        sb.append("Color : #$color Char: ${widgetCharacter}\n")
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
        parcel.writeInt(widgetCharacter.ordinal)
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

enum class WidgetCharacter {
    Haruhi, HaruhiShositsu, Nagato, NagatoShositsu, Mikuru, MikuruBig
}

fun WidgetCharacter.toCharacterFolder(): String {
    return when (this) {
        WidgetCharacter.Haruhi -> FOLDER_HARUHI
        WidgetCharacter.HaruhiShositsu -> FOLDER_HARUHI_SHOSITSU
        WidgetCharacter.Nagato -> FOLDER_NAGATO
        WidgetCharacter.NagatoShositsu -> FOLDER_NAGATO_SHOSITSU
        WidgetCharacter.Mikuru -> FOLDER_MIKURU
        WidgetCharacter.MikuruBig -> FOLDER_MIKURU_BIG
    }
}

fun WidgetCharacter.toCharacterImg(): Int {
    return when (this) {
        WidgetCharacter.Haruhi -> R.drawable.haruhi_normal
        WidgetCharacter.HaruhiShositsu -> R.drawable.haruhi1
        WidgetCharacter.Nagato -> R.drawable.hat_nagato
        WidgetCharacter.NagatoShositsu -> R.drawable.nagato_chan
        WidgetCharacter.Mikuru -> R.drawable.mikuruw
        WidgetCharacter.MikuruBig -> R.drawable.big_mikuru
    }
}

fun WidgetCharacter.toNameRes(): Int {
    return when (this) {
        WidgetCharacter.Haruhi -> (R.string.txt_suzumiya)
        WidgetCharacter.HaruhiShositsu -> (R.string.txt_suzumiya_shositsu)
        WidgetCharacter.Nagato -> (R.string.txt_nagato)
        WidgetCharacter.NagatoShositsu -> (R.string.txt_nagato_shositsu)
        WidgetCharacter.Mikuru -> (R.string.txt_mikuru)
        WidgetCharacter.MikuruBig -> (R.string.txt_mikuru_big)
    }
}