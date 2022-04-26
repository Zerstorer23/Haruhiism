package com.haruhi.bismark439.haruhiism.activities.alarm_screens.mikuru_alarms

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.system.alarms.NotificationManager.sendNotification
import com.haruhi.bismark439.haruhiism.system.alarms.SoundPlayer
import com.haruhi.bismark439.haruhiism.system.Constants.FILE_KYON_MIKURU_KINSOKU_MP3
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.alarm_screens.BasicAlarmScreenActivity
import com.haruhi.bismark439.haruhiism.databinding.ActivityMikuruPuzzleScreenBinding
import com.haruhi.bismark439.haruhiism.system.Sleeper

class MikuruPuzzleAlarmActivity :
    BasicAlarmScreenActivity<ActivityMikuruPuzzleScreenBinding>(ActivityMikuruPuzzleScreenBinding::inflate) {
    private var xDelta = 0
    private var yDelta = 0
    private var remaining = 6
    private val tvs =
        intArrayOf(R.id.line0, R.id.line1, R.id.line2, R.id.line3, R.id.line4, R.id.line5)
    private lateinit var wordSets: Array<WordPuzzle>// = Array(5){null}
    private lateinit var tvSets: Array<TextView>// = Array(6){null}

    private lateinit var secondaryPlayer: SoundPlayer
    private lateinit var textFront: Array<String>
    private lateinit var textEnd: Array<String>
    private lateinit var coords: ArrayList<Int>// = null

    @SuppressLint("ClickableViewAccessibility")
    override fun initScreen() {
        textFront = arrayOf(
            getString(R.string.linef1),
            getString(R.string.linef2),
            getString(R.string.linef3),
            getString(R.string.linef4),
            getString(R.string.linef5),
            getString(R.string.linef6)
        )
        textEnd = arrayOf(
            getString(R.string.line1),
            getString(R.string.line2),
            getString(R.string.line3),
            getString(R.string.line4),
            getString(R.string.line5),
            getString(R.string.line6)
        )
        tvSets = arrayOf(
            binding.text1,
            binding.text2,
            binding.text3,
            binding.text4,
            binding.text5,
            binding.text6
        )
        binding.unlockMikuru.setOnClickListener { onClickGiveUp() }
        wordSets =
            Array(WordType.values().size) { i: Int -> WordPuzzle(this, WordType.values()[i]) }
        secondaryPlayer = SoundPlayer(this)
        sendNotification(this)
        soundPlayer.playSound("kinsoku/kinsoku.mp3")
        secondaryPlayer.playSound("kinsoku/mikuru_cry.mp3")
        initialiseCoordinates()
        for (i in tvSets.indices) {
            val tv = tvSets[i]// in tvSets)
            tv.setOnTouchListener { v, event ->
                onTouch(v, event)
            }
            tv.text = wordSets[i].random.word
            tv.tag = "$i"
            tv.translationX = coords[i].toFloat()
        }
    }

    private fun onTouch(view: View, event: MotionEvent): Boolean {
        val x = event.rawX.toInt()
        val y = event.rawY.toInt()
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                val lParams = view.layoutParams as FrameLayout.LayoutParams
                xDelta = x - lParams.leftMargin
                yDelta = y - lParams.topMargin
            }
            MotionEvent.ACTION_UP -> {
                var i = 0
                while (i < tvs.size) {
                    val test = findViewById<View>(tvs[i]) as TextView
                    val overlap = isViewOverlapping(test, view)
                    if (overlap) {
                        doSomething(i, view)
                    }
                    i++
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val layoutParams = view
                    .layoutParams as FrameLayout.LayoutParams
                layoutParams.leftMargin = x - xDelta
                layoutParams.topMargin = y - yDelta
                layoutParams.rightMargin = 0
                layoutParams.bottomMargin = 0
                view.layoutParams = layoutParams
            }
        }
        binding.main.invalidate()
        return true
    }

    private fun isViewOverlapping(back: View, moving: View): Boolean {
        val firstPosition = IntArray(2)
        val secondPosition = IntArray(2)
        back.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        moving.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        back.getLocationOnScreen(firstPosition)
        moving.getLocationOnScreen(secondPosition)
        val fRight = back.measuredWidth + firstPosition[0]
        val fBot = firstPosition[1] + back.measuredHeight //Bot higher
        val xx = (secondPosition[0] + moving.width / 2).toFloat()
        val yy = (secondPosition[1] + moving.height / 2).toFloat()
        if (xx >= firstPosition[0] && xx <= fRight) {
            if (yy >= firstPosition[1] && yy <= fBot) {
                Debugger.log("X: " + firstPosition[0] + "~ " + fRight)
                Debugger.log("Y: " + firstPosition[1] + " ~ " + fBot)
                Debugger.log("vs X: $xx Y:$yy")
                return true
            }
        }
        return false
    }

    private fun doSomething(i: Int, view: View) {
        view.tag as String
        val input = (view as TextView).text.toString()
        val textView = findViewById<View>(tvs[i]) as TextView
        val t1 = textFront[i]
        val text = t1 + input + textEnd[i]
        val spannable: Spannable = SpannableString(text)
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#00c800")),
            t1.length,
            (t1 + input).length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.setText(spannable, TextView.BufferType.SPANNABLE)
        view.setVisibility(View.GONE)
        remaining--
        if (remaining == 0) {
            cleanUpSounds()
            Sleeper.waitAndExecute(1000, ::destroy)
        }
        vibrate(500)

    }

    private fun onClickGiveUp() {
        try {
            cleanUpSounds()
        } catch (e: Exception) {
            e.printStackTrace()
            destroy()
        }
    }

    override fun destroy() {
        secondaryPlayer.stopPlaying()
        super.destroy()
    }

    private fun cleanUpSounds() {
        soundPlayer.stopPlaying()
        secondaryPlayer.stopPlaying()
        soundPlayer.playSoundOnTop(FILE_KYON_MIKURU_KINSOKU_MP3)
    }

    private fun initialiseCoordinates() {
        coords = ArrayList()
        val displayMetrics = DisplayMetrics()
        val width = displayMetrics.widthPixels
        for (i in 0..5) {
            val a = (width.toFloat() / 7).toInt() * i
            coords.add(a)
        }
        coords.shuffle()
    }


}