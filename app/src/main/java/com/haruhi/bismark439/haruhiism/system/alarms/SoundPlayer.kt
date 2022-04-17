package com.haruhi.bismark439.haruhiism.system.alarms

import android.content.Context
import android.media.MediaPlayer
import java.io.IOException
import kotlin.math.ln

class SoundPlayer(
    private val context: Context
) {
    companion object {
        const val MAX_VOLUME: Double = 100.0
        const val DEFAULT_VOLUME: Double = 100.0
        var currentVolume = 0
    }

    private val mainPlayer = MediaPlayer()
    private val loopingPlayer = MediaPlayer()
    private var doLoop = false
    fun stopPlaying() {
        if (doLoop) {
            loopingPlayer.stop()
            loopingPlayer.reset()
            doLoop = false
        }
        mainPlayer.stop()
        mainPlayer.reset()
    }


    fun playLooper(folder: String, list: Array<String>) {
        doLoop = true
        try {
            val rand = (Math.random() * list.size).toInt()
            val afd = context.assets.openFd("${folder}/${list[rand]}")
            loopingPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            loopingPlayer.prepare()
            loopingPlayer.start()
            loopingPlayer.setOnCompletionListener { mediaPlayer ->
                mediaPlayer.reset()
                if (doLoop) {
                    playLooper(folder, list)
                }
            }
        } catch (e: IOException) {
            e.localizedMessage
            e.printStackTrace()
        }
    }

    fun playSoundOnTop(songName: String) {
        try {
            val afd = context.assets.openFd(songName)
            val player = MediaPlayer()
            player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            player.prepare()
            player.start()
            player.setOnCompletionListener { mediaPlayer ->
                mediaPlayer.release()
            }
        } catch (e: IOException) {
            e.localizedMessage
            e.printStackTrace()
        }
    }

    fun playSound(songName: String, doLoop: Boolean = true) {
        try {
            val afd = context.assets.openFd(songName)
            stopPlaying()
            mainPlayer.setScreenOnWhilePlaying(true)
            val log1 = (ln(MAX_VOLUME - DEFAULT_VOLUME) / ln(MAX_VOLUME)).toFloat()
            mainPlayer.setVolume(1 - log1, 1 - log1)
            mainPlayer.isLooping = doLoop
            mainPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            mainPlayer.prepare()
            mainPlayer.start()
       /*     mainPlayer.setOnCompletionListener { mediaPlayer ->
                mediaPlayer.
              //  mediaPlayer.release()
            }*/
        } catch (e: IOException) {
            e.localizedMessage
            e.printStackTrace()
        }
    }
}