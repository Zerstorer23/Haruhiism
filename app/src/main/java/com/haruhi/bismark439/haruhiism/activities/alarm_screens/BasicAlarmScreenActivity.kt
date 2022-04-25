@file:Suppress("DEPRECATION")

package com.haruhi.bismark439.haruhiism.activities.alarm_screens

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.KeyEvent
import androidx.viewbinding.ViewBinding
import com.haruhi.bismark439.haruhiism.DEBUG
import com.haruhi.bismark439.haruhiism.system.alarms.SoundPlayer
import com.haruhi.bismark439.haruhiism.activities.interfaces.ActivityViewBinder
import com.haruhi.bismark439.haruhiism.activities.interfaces.BaseActivity

abstract class BasicAlarmScreenActivity<T : ViewBinding>(inflater: ActivityViewBinder<T>) :
    BaseActivity<T>(inflater) {

    private var demolish = false
    protected lateinit var soundPlayer: SoundPlayer
    private lateinit var audioManager: AudioManager
    private lateinit var notificationManager: NotificationManager
    private lateinit var vibrationManager: Vibrator
    abstract fun initScreen()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        vibrationManager = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
        soundPlayer = SoundPlayer(this)
        initScreen()


    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DEBUG.appendLog("KEYCODE_BACK")
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            DEBUG.appendLog("KEYCODE_MENU")
            return true
        }
        return false
    }

    protected open fun destroy() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, SoundPlayer.currentVolume, 0)
        soundPlayer.stopPlaying()
        demolish = true
        notificationManager.cancel(1)
        finish()
    }


    @SuppressLint("MissingPermission")
    protected fun vibrate(duration:Long = 500){
        vibrationManager.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)) // New vibrate method for API Level 26 or higher
    }
}