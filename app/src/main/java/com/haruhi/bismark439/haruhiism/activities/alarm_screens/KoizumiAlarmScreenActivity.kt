package com.haruhi.bismark439.haruhiism.activities.alarm_screens

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.RelativeLayout
import android.widget.TextView
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.databinding.ActivityKoizumiScreenBinding
import com.haruhi.bismark439.haruhiism.system.Sleeper
import com.haruhi.bismark439.haruhiism.system.alarms.NotificationManager.sendNotification

class KoizumiAlarmScreenActivity :
    BasicAlarmScreenActivity<ActivityKoizumiScreenBinding>(ActivityKoizumiScreenBinding::inflate) {
    companion object{
        private val PROFILE_BITMAPS = intArrayOf(
            R.drawable.haruhi_prof,
            R.drawable.mikuru_prof,
            R.drawable.yuki_prof,
            R.drawable.kyon_prof,
            R.drawable.asakura_prof,
            R.drawable.tsuruya_prof
        )
        private val PLAYER_NAMES = arrayOf("Haruhi", "Mikuru", "Nagato", "Kyon", "Asakura", "Tsuruya")
    }

    var rel: RelativeLayout? = null
    var col = 10
    var row = 10
    private var numTrue = 0
    private var numFalse = 0
    var board = Array(row) {
        BooleanArray(
            col
        )
    }
    private var rPlayer = 0


    override fun initScreen() {

        rel = findViewById<View>(R.id.koi_board) as RelativeLayout
        rPlayer = (Math.random() * PLAYER_NAMES.size).toInt()
        val bitmap = BitmapFactory.decodeResource(applicationContext.resources, PROFILE_BITMAPS[rPlayer])
        binding.pl2.setImageBitmap(bitmap)
        binding.scoreboard.text = getString(R.string.koizumi_score_header)
        val rand = (Math.random() * 60 + 20).toInt()
        generateBoard(rand)
        binding.koiBoard.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.koiBoard.viewTreeObserver.removeOnGlobalLayoutListener(this)
                setBoard()
            }
        })
        soundPlayer.playSound("Alarm/toroden.mp3", false)
        sendNotification(this)// applicationContext, KoizumiScreen::class.java)
    }

    fun setBoard() {
        val width = rel!!.width.toFloat()
        val height = rel!!.height.toFloat()
        val w = width / col
        val h = height / row
        for (i in 0 until row) {
            for (j in 0 until col) {
                val y = i * h
                val x = j * w
                val tv = TextView(this)
                if (board[i][j]) {
                    tv.text = "●"
                } else {
                    tv.text = "○"
                }
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
                tv.gravity = View.TEXT_ALIGNMENT_CENTER
                tv.translationX = x
                tv.translationY = y
                tv.scaleX = 1.3.toFloat()
                tv.scaleY = 1.3.toFloat()
                rel!!.addView(tv)
            }
        }
    }

    fun generateBoard(C: Int) { // C 40?
        numTrue = 0
        numFalse = 0
        for (i in 0 until row) {
            for (j in 0 until col) {
                val a = (Math.random() * 100).toInt() + 1 // 1~100;
                if (a <= C) {
                    board[i][j] = true
                    numTrue++
                } else {
                    board[i][j] = false
                    numFalse++
                }
            }
        }
        println("$numTrue VS $numFalse")
    }

    fun updateBoard() {
        val skillArcade: Thread = object : Thread() {
            override fun run() {
                for (i in 0 until row) {
                    for (j in 0 until col) {
                        val tv = rel!!.getChildAt(i * col + j) as TextView
                        runOnUiThread {
                            if (board[i][j]) {
                                tv.text = "●"
                            } else {
                                tv.text = "○"
                            }
                        }
                    }
                }
            }
        }
        skillArcade.start()
    }

    @SuppressLint("SetTextI18n")
    fun onAnswer(v: View) {
        val tt = v.tag as String
        var correct = false
        if (tt == "1") {
            if (numTrue >= numFalse) {
                correct = true //Chose koizumi, corr
                soundPlayer.playSoundOnTop("Koizumi/voice_00129.wav")
            } else {
                //but wrong
                when (rPlayer) {
                    0 -> soundPlayer.playSoundOnTop("haruhi/voice_00016.wav")
                    1 -> soundPlayer.playSoundOnTop("mikuru/voice_00075.wav") //mikuruno shobu
                    2 -> soundPlayer.playSoundOnTop("Nagato/voice_00049.wav")
                    3 -> soundPlayer.playSoundOnTop("Kyon/voice_00100.wav")
                    4 -> soundPlayer.playSoundOnTop("Asakura/voice_00161.wav")
                    5 -> soundPlayer.playSoundOnTop("Tsuruya/voice_00182.wav")
                }
            }
        } else { //Chose Mikuru
            if (numTrue <= numFalse) {
                //Chose Mikuru, corr
                when (rPlayer) {
                    0 -> soundPlayer.playSoundOnTop("haruhi/voice_00026.wav")
                    1 -> soundPlayer.playSoundOnTop("mikuru/voice_00070.wav") //mikuru beam
                    2 -> soundPlayer.playSoundOnTop("Nagato/voice_00050.wav")
                    3 -> soundPlayer.playSoundOnTop("Kyon/voice_00095.wav")
                    4 -> soundPlayer.playSoundOnTop("Asakura/voice_00157.wav")
                    5 -> soundPlayer.playSoundOnTop("Tsuruya/voice_00185.wav")
                }
                correct = true
            } else {
                //but wrong
                soundPlayer.playSoundOnTop("Koizumi/voice_00125.wav") //mikuruno shobu
            }
        }
        binding.scoreboard.text = "$numTrue VS $numFalse"
        println("Answer $correct")
        showAnswer(correct)
    }

    private fun showAnswer(correct: Boolean) {
        board = Array(row) { BooleanArray(col) }
        for (i in 0 until numTrue) {
            board[i / col][i % col] = true
        }
        updateBoard()
        //    mMediaPlayer.pause();
        if (!correct) {
            vibrate()
        } else {
            binding.kzmButtoncont.visibility = View.INVISIBLE
        }
        Sleeper.waitAndExecute(1000) {
            if (correct) {
                destroy()
            } else {
                object : Thread() {
                    override fun run() {
                        //    mMediaPlayer.start();
                        runOnUiThread { binding.scoreboard.text = getString(R.string.txt_koizumi_board_title) }
                        val rand = (Math.random() * 40 + 30).toInt()
                        generateBoard(rand)
                        updateBoard()
                    }
                }.start()
            }
        }
    }

}