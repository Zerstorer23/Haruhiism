package com.haruhi.bismark439.haruhiism.activities.alarm_screens

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.databinding.ActivityHaruhiBasicBinding
import com.haruhi.bismark439.haruhiism.system.alarms.NotificationManager.sendNotification
import com.ncorti.slidetoact.SlideToActView
import pl.droidsonroids.gif.GifImageView
import java.io.IOException


class HaruhiAlarmScreen :
    BasicAlarmScreenActivity<ActivityHaruhiBasicBinding>(ActivityHaruhiBasicBinding::inflate) {
    companion object {
        var aniList = intArrayOf(
            R.drawable.ani1,
            R.drawable.ani2,
            R.drawable.ani3, R.drawable.ani4
        )
    }

    private lateinit var haruhiVoices: Array<String>
    override fun initScreen() {
        val rotate = RotateAnimation(
            0F,
            359F,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 3141
        rotate.interpolator = LinearInterpolator()
        rotate.repeatCount = Animation.INFINITE
        binding.hrhSos.startAnimation(rotate)
        val assetManager = assets
        //Haruhi Image
        val hrhImg: GifImageView = findViewById(R.id.hrh_img)
        val nrand = (Math.random() * aniList.size).toInt()
        hrhImg.setBackgroundResource(aniList[nrand])
        try {
            haruhiVoices = assetManager.list("haruhi") as Array<String>
        } catch (e: IOException) {
            e.printStackTrace()
        }
        binding.swipeButton.onSlideCompleteListener = object :
            SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                destroy()
            }
        }
        sendNotification(this)
        soundPlayer.playSound("Alarm/bgm1.mp3")
        soundPlayer.playLooper("haruhi", haruhiVoices)
    }


}