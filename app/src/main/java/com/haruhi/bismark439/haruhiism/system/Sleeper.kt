package com.haruhi.bismark439.haruhiism.system

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper


object Sleeper {
    fun waitAndExecute( delay: Long,func: VoidReturn) {
        Handler(Looper.getMainLooper()).postDelayed(func, delay)
    }
    fun waitAndTickAndExecute( delay: Long, interval:Long, onTick: (l:Long)->Unit, onFinish: VoidReturn) {
        object : CountDownTimer(delay, interval) {
            override fun onTick(millisUntilFinished: Long) {
                onTick(millisUntilFinished)
            }
            override fun onFinish() {
               onFinish()
            }
        }.start()
    }
}