package com.haruhi.bismark439.haruhiism.activities.alarm_screens

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.haruhi.bismark439.haruhiism.system.alarms.NotificationManager.sendNotification
import com.haruhi.bismark439.haruhiism.system.Constants.FILE_HARUHI_CHAN_MP3
import com.haruhi.bismark439.haruhiism.system.Constants.FILE_WAKATERU_MP3
import com.haruhi.bismark439.haruhiism.system.Sleeper
import com.haruhi.bismark439.haruhiism.databinding.ActivityAlarmScreenBinding

class KyonSisterAlarmActivity :
    BasicAlarmScreenActivity<ActivityAlarmScreenBinding>(ActivityAlarmScreenBinding::inflate) {
    companion object{
        val DENWA_FILES = arrayOf("denwa1.mp3", "denwa2.mp3", "denwa3.mp3", "denwa4.mp3", "denwa5.mp3", "denwa6.mp3", "denwa7.mp3")
    }
    private var myNumber = IntArray(11)
    private var idx = 0
    private var ansNumber = IntArray(11)
    private lateinit var myNum: TextView
    private lateinit var ansNum: TextView
    private lateinit var keypad: LinearLayout

    override fun initScreen() {
        myNum = binding.myNumber// findViewById(R.id.myNumber) as TextView?
        ansNum = binding.ansnum
        keypad = binding.keypad// findViewById(R.id.keypad) as LinearLayout?
        binding.cheatButton.setOnClickListener { onCheat() }
        generateNumber()
        soundPlayer.playSound(FILE_HARUHI_CHAN_MP3)
        soundPlayer.playLooper("denwa", DENWA_FILES)
        sendNotification(this)
    }


    fun onClickPad(v: View) {
        val tt = v.tag as String
        val t = tt.toInt()
        if (t == 10) {
            if (idx > 0) {
                idx--
            }
        } else {
            myNumber[idx] = t
            idx++
            if (idx >= myNumber.size) {
                if (checkAns()) { //correct
                    myNum.setTextColor(Color.parseColor("#00c800"))
                    soundPlayer.playSound(FILE_WAKATERU_MP3)
                    Sleeper.waitAndExecute(1000){
                        destroy()
                    }
                } else {
                    vibrate()
                    myNum.setTextColor(Color.parseColor("#ff0000"))
                    keypad.visibility = View.INVISIBLE
                    idx--
                    Sleeper.waitAndExecute(1000){
                        myNum.setTextColor(Color.parseColor("#000000"))
                        keypad.visibility = View.VISIBLE
                    }
                }
            }
        }
        updateScreen()
    }

    private fun generateNumber() {
        ansNumber[0] = 0
        ansNumber[1] = 9
        ansNumber[2] = 0
        for (i in 3 until ansNumber.size) {
            val rand = (Math.random() * 10).toInt()
            ansNumber[i] = rand
        }
        for (i in ansNumber.indices) {
            if (i == 3 || i == 7) {
                ansNum.append(" ")
            }
            ansNum.append(ansNumber[i].toString() + "")
        }
    }

    private fun checkAns(): Boolean {
        var ret = true
        var i = 0
        while (i < myNumber.size) {
            if (myNumber[i] != ansNumber[i]) {
                ret = false
                i = 999
            }
            i++
        }
        return ret
    }

    private fun updateScreen() {
        myNum.text = ""
        for (i in 0 until idx) {
            if (i == 3 || i == 7) {
                myNum.append(" ")
            }
            myNum.append(myNumber[i].toString() + "")
        }
        for (i in idx until myNumber.size) {
            if (i == 3 || i == 7) {
                myNum.append(" ")
            }
            myNum.append("_ ")
        }
    }
    private fun onCheat() {
        destroy()
    }


}