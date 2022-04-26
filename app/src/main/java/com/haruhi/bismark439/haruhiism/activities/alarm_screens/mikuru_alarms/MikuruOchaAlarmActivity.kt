package com.haruhi.bismark439.haruhiism.activities.alarm_screens.mikuru_alarms

import android.annotation.SuppressLint
import android.net.Uri
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.alarm_screens.BasicAlarmScreenActivity
import com.haruhi.bismark439.haruhiism.databinding.ActivityMikuruOchaScreenBinding
import com.haruhi.bismark439.haruhiism.system.Sleeper
import com.haruhi.bismark439.haruhiism.system.alarms.NotificationManager.sendNotification

class MikuruOchaAlarmActivity :
    BasicAlarmScreenActivity<ActivityMikuruOchaScreenBinding>(ActivityMikuruOchaScreenBinding::inflate) {
    var mAutoIncrement = false
    lateinit var progressBar: ProgressBar
    private lateinit var imgView: ImageView
    private lateinit var announce: TextView// = null
    private var weightFront = 0
    private var weightMid = 0
    private var weightEnd = 0
    private lateinit var pathIn :String
    private lateinit var pathLoop:String

    @SuppressLint("ClickableViewAccessibility")
    override fun initScreen() {
        imgView = binding.oiThumbnail
        progressBar = binding.teaProgress
        announce = binding.oiAnnounce
        pathIn = "android.resource://${packageName}/${R.raw.tea_in}"
        pathLoop = "android.resource://${packageName}/${R.raw.tea_loop}"
        setTime()
        setVideoView()
        setUpCountDown()
        soundPlayer.playSound("Alarm/bgm1.mp3")
        binding.PourButton.setOnTouchListener { _, m -> onTouchView(m) }
        sendNotification(this)
    }

    private lateinit var videoView: VideoView
    private fun setVideoView() {
        videoView = binding.videoView
        videoView.setVideoURI(Uri.parse(pathIn))
        videoView.setOnCompletionListener {
            videoView.setVideoURI(Uri.parse(pathLoop))
            videoView.start()
        }
        videoView.setOnErrorListener { _, what, extra ->
            Debugger.log("$what = code , $extra = extra")
            false
        }
    }

    private fun setTime() {
        val rand = (Math.random() * 49).toInt() + 24 //24 to 93;
        weightMid = 20
        weightFront = rand
        weightEnd = 100 - rand - weightMid
        val tv1 = findViewById<View>(R.id.oi_front) as TextView
        val tv2 = findViewById<View>(R.id.oi_mid) as TextView
        val tv3 = findViewById<View>(R.id.oi_end) as TextView
        var param = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
        param.weight = weightFront.toFloat()
        tv1.layoutParams = param
        param = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
        param.weight = weightMid.toFloat()
        tv2.layoutParams = param
        param = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
        param.weight = weightEnd.toFloat()
        tv3.layoutParams = param

    }

    private lateinit var countDown: Thread
    var stopThread = false
    private fun setUpCountDown() {
        stopThread = false
        countDown = object : Thread() {
            override fun run() {
                super.run()
                while (progressBar.progress < 100 && !stopThread) {
                    if (mAutoIncrement) {
                        progressBar.progress += 3
                    } else {
                        progressBar.progress = 0
                    }
                    sleep(100)
                }
                if (!stopThread) {
                    makeDecision()
                }
            }
        }
        countDown.start()
    }

    fun makeDecision() {
        val prgBar = binding.teaProgress
        val ans = prgBar.progress
        when {
            ans < weightFront -> {
                announce.setText(R.string.pourtoolittle)
                soundPlayer.playSoundOnTop("mikuru/voice_00079.wav") //watashinde iyi deska?
                vibrate()
                prgBar.progress = 0
            }
            ans > 100 - weightEnd -> {
                announce.setText(R.string.pourtomuch)
                soundPlayer.playSoundOnTop("mikuru/voice_00078.wav") //watashinde iyi deska?
                vibrate()
                prgBar.progress = 0
            }
            else -> { //Solved puzzle
                soundPlayer.playSoundOnTop("Etc/oisii.mp3")
                stopThread = true
                Sleeper.waitAndExecute(1875, ::destroy)
            }
        }
    }

    private fun onTouchView(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !mAutoIncrement) {
            mAutoIncrement = true
            videoView.setVideoURI(Uri.parse(pathIn))
            videoView.requestFocus()
            binding.videoView.start()
            binding.videoView.visibility = View.VISIBLE
            imgView.visibility = View.INVISIBLE
        }
        if ((event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) && mAutoIncrement) {
            mAutoIncrement = false
            //   binding.videoView.stopPlayback()
            makeDecision()
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        stopThread = true
    }
}