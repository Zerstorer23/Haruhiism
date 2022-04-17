package com.haruhi.bismark439.haruhiism.wallPapers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import com.haruhi.bismark439.haruhiism.R
import java.util.*

/**
 * Created by Bismark439 on 02/03/2018.
 */

class YukiNWall : WallpaperService() {
    private var mVisible // visible flag
            = false
    var canvas // canvas reference
            : Canvas? = null
    var delayFast = 90
    var delaySlow = 278
    var delayNormal = 120
    var textPaint = Paint()
    var backPaint = Paint()
    var mcontext //reference to the current context
            : Context? = null
    val mHandler = Handler() // this is to handle the thread
    var red = Color.parseColor("#CD5C5C")
    override fun onCreateEngine(): Engine {
        mcontext = this //set the current context
        //return the Engine Class
        return LiveWall() // this calls contain the wallpaper code
    }

    internal inner class LiveWall : Engine() {
        private val mDrawFrame = Runnable { drawFrame() }
        private val mChangePic = Runnable { changePicture() }

        // ======== MATRIX LIVE WALLPAPER VARS
        var background_color = Color.parseColor("#FF000000")
        private var width = 1000000 //default initial width
        private var height = 100 //default initial height
        var changePicSpeed = 70000
        var columnSize = 50 //column size ; no of digit required to fill the screen
        var fontSize = 24 //font size of the text which will fall
        var imgList = intArrayOf(R.string.YUKI_6, R.string.YUKI_disappear)
        var mImgIndex = 0
        var text = "" // "涼宮ハルヒ";  // Text which need to be drawn
        var text_color = Color.parseColor("#FFFFFF")
        var rand = Random() //random generater

        //Main Strings
        private var mStrings: Array<String> = arrayOf()
        private var stringSoFar: String? = null
        private var arrayIndex = 0

        //Sub Strings
        private var subString: String? = null
        private var mIndex = 0
        private var mDelay = 150 //Default 150ms delay

        //Beeping
        private var beep = true

        //======================
        //Called when the surface is created
        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
        }

        // remove thread
        override fun onDestroy() {
            super.onDestroy()
            mHandler.removeCallbacks(mDrawFrame)
            mHandler.removeCallbacks(mChangePic)
        }

        //called when varaible changed
        override fun onVisibilityChanged(visible: Boolean) {
            mVisible = visible
            fontSize = width / columnSize
            if (visible) {
                //   changePicture();
                drawFrame()
            } else {
                //this is necessay to remove the call back
                mHandler.removeCallbacks(mDrawFrame)
                mHandler.removeCallbacks(mChangePic)
            }
        }

        //called when surface destroyed
        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            mVisible = false
            //this is necessary to remove the call back
            mHandler.removeCallbacks(mDrawFrame)
            mHandler.removeCallbacks(mChangePic)
        }

        //this function contain the the main draw call
        /// this function need to call every time the code is executed
        // the thread call this function with some delay "drawspeed"
        fun drawFrame() {
            //getting the surface holder
            val holder = surfaceHolder
            canvas = null // canvas
            try {
                canvas = holder.lockCanvas() //get the canvas
                if (canvas != null) {
                    canvasDraw()
                }
            } finally {
                if (canvas != null) holder.unlockCanvasAndPost(canvas)
            }
            mHandler.removeCallbacks(mDrawFrame)
            if (mVisible) {
                // set the execution delay
                //      System.out.println("Delay "+mDelay);
                mHandler.postDelayed(mDrawFrame, mDelay.toLong())
            }
        }

        fun beep() {
            //   System.out.println("Beep called");
            if (beep) {
                drawText(stringSoFar)
            } else {
                drawText(stringSoFar + "_")
            }
            beep = !beep
        }

        fun changePicture() {
            initialise()
            mImgIndex++
            if (mImgIndex >= imgList.size) mImgIndex = 0
            mHandler.postDelayed(mChangePic, changePicSpeed.toLong())
        }

        fun addCharacter() {
            var toDraw: String? = ""
            mDelay = delayNormal
            if (mStrings[arrayIndex] == "YUKI.N>") {
                stringSoFar = appendText()
                toDraw = stringSoFar + "YUKI.N>_"
                arrayIndex++
            } else if (mStrings[arrayIndex] == "\n") { //New line mode
                stringSoFar = appendText()
                toDraw = stringSoFar
                arrayIndex++
                mDelay = delaySlow
            } else { //SubString Mode
                if (subString == null) {
                    subString = mStrings[arrayIndex]
                    stringSoFar = appendText()
                    if (subString!!.length > 10) {
                        mDelay = delayFast
                    }
                } //Init substring
                toDraw = if (mIndex == 0) {
                    stringSoFar + subString!!.substring(0, mIndex)
                } else {
                    stringSoFar + subString!!.substring(0, mIndex) + "_"
                }
                mIndex++
                if (mIndex > subString!!.length) {
                    arrayIndex++
                    mIndex = 0
                    subString = null
                }
            }
            drawText(toDraw)
        }

        fun initialise() {
            mIndex = 0
            arrayIndex = 0
            text = getString(imgList[mImgIndex])
            mStrings = text.split(",").toTypedArray()
            textPaint.style = Paint.Style.FILL
            textPaint.color = text_color
            textPaint.textSize = fontSize.toFloat()
            backPaint.color = background_color
            backPaint.alpha = 255 //set the alpha
            backPaint.style = Paint.Style.FILL
        }

        fun appendText(): String {
            var text = ""
            for (i in 0 until arrayIndex) {
                text = text + mStrings[i]
            }
            return text
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            // some matrix variable
            // though not needed
            this.width = width + 20
            this.height = height
            fontSize = width / columnSize
            changePicture()
            drawFrame()
            canvas!!.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backPaint)
        }

        //old matrix effect code
        fun drawText(text: String?) {
//            System.out.println("Draw text : "+text);
            var x = 0
            var y = 7
            for (i in 0 until text!!.length) {
                if (text[i] == '\n') {
                    y++
                    y++
                    x = 0
                } else {
                    //   System.out.println("set:"+text.charAt(i)+":at x"+x+"/ y"+y);
                    canvas!!.drawText(
                        "" + text[i],
                        (x * fontSize).toFloat(),
                        (y * fontSize).toFloat(),
                        textPaint
                    )
                    x++
                }
                if (x * fontSize > width - 100) {
                    x = 0
                    y++
                }
            }
        }

        //old martix effect code
        fun canvasDraw() {
            //Log.d("canvas ","drawing");
            canvas!!.drawRect(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                backPaint
            ) //draw rect to clear the canvas
            if (arrayIndex < mStrings.size) {
                addCharacter() // draw the canvas
            } else { //Wrote Everything
                stringSoFar = appendText()
                mDelay = 250
                beep()
            }
        }
    }
}
