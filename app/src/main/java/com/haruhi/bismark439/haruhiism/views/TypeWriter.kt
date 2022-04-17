package com.haruhi.bismark439.haruhiism.views

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.haruhi.bismark439.haruhiism.R

/**
 * Created by Bismark439 on 02/03/2018.
 */
class TypeWriterView : AppCompatTextView {
    //Main Strings
    private var mStrings: Array<String> = arrayOf()
    private var stringSoFar: String? = null
    private var arrayIndex = 0

    //Sub Strings
    private var subString: String? = null
    private var mIndex = 0
    private var mDelay: Long = 150 //Default 150ms delay

    //Beeping
    private var beep = true
    private var repeat = 50

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    private val mHandler = Handler(Looper.myLooper()!!)
    private val characterAdder: Runnable = object : Runnable {
        override fun run() {
            if (mStrings[arrayIndex] == "YUKI.N>") {
                stringSoFar = appendText()
                text = stringSoFar + context.getString(R.string.typewriter_yuki_n)
                arrayIndex++
                if (arrayIndex < mStrings.size) {
                    mHandler.postDelayed(this, mDelay)
                }
            } else if (mStrings[arrayIndex] == "\n") { //New line mode
                stringSoFar = appendText()
                text = stringSoFar
                arrayIndex++
                mDelay = 780
                if (arrayIndex < mStrings.size) {
                    mHandler.postDelayed(this, mDelay)
                }
            } else { //SubString Mode
                if (subString == null) {
                    subString = mStrings[arrayIndex]
                    stringSoFar = appendText()
                    mDelay = if (subString!!.length > 10) {
                        90
                    } else {
                        150
                    }
                }
                text = if (mIndex == 0) {
                    stringSoFar + subString!!.substring(0, mIndex)
                } else {
                    stringSoFar + subString!!.substring(0, mIndex) + "_"
                }
                mIndex++
                if (mIndex <= subString!!.length) {
                    mHandler.postDelayed(this, mDelay)
                } else {
                    arrayIndex++
                    mIndex = 0
                    subString = null
                    if (arrayIndex < mStrings.size) {
                        mHandler.postDelayed(this, mDelay)
                    } else { //Wrote Everything
                        println("Send beeper")
                        stringSoFar = appendText()
                        mDelay = 300
                        repeat = 500
                        mHandler.postDelayed(beeper, mDelay)
                    }
                }
            }
        }
    }
    private val beeper: Runnable = object : Runnable {
        override fun run() {
            if (repeat > 0) {
                text = if (beep) {
                    stringSoFar
                } else {
                    stringSoFar + "_"
                }
                beep = !beep
                repeat--
                mHandler.postDelayed(this, mDelay)
            }
            // System.out.println(repeat);
        }
    }

    fun animateText(text: CharSequence) {
        mIndex = 0
        arrayIndex = 0
        repeat = 0
        mStrings = text.toString().split(",").toTypedArray()
        setText("")
        mHandler.removeCallbacks(characterAdder)
        mHandler.postDelayed(characterAdder, mDelay)
    }

    fun appendText(): String {
        var text = ""
        for (i in 0 until arrayIndex) {
            text += mStrings[i]
        }
        return text
    }

    fun setCharacterDelay(millis: Long) {
        mDelay = millis
    }
}