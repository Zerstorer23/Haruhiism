package com.haruhi.bismark439.haruhiism.wallPapers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import com.haruhi.bismark439.haruhiism.DEBUG
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.wallPapers.ActivityYukiLWPSettings.Companion.loadLWP
import java.util.*

/**
 * Created by Bismark439 on 02/03/2018.
 */

class YukiWallpaper : WallpaperService() {
    private var mVisible // visible flag
            = false
    var canvas // canvas reference
            : Canvas? = null
    var drawSpeed = 33 // thread call delay time 10 default
    var changePicSpeed = 47000
    private var mcontext //reference to the current context
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
        private var backgroundColor = Color.parseColor("#FF000000")
        private var width = 1000000 //default initial width
        private var height = 100 //default initial height
        private var columnSize = 100 //column size ; no of digit required to fill the screen
        private var fontSize = 0 //24; //font size of the text which will fall
        private var textPaint: Paint? = null
        private var backPaint: Paint? = null
        var text: String = "SUZUMIYAHARUHI" // "涼宮ハルヒ";  // Text which need to be drawn
        private var textChar = text.toCharArray() // split the character of the text
        private var textLength = textChar.size //length of the length text

        // contain the position which will help to draw the text
        private lateinit var textPosition: IntArray
        private var textColor = Color.parseColor("#FF8BFF4A")
        private var rand = Random() //random generater
        private var imgList = intArrayOf(
            R.string.haruhi_ascii3,
            R.string.ascii_mikuru,
            R.string.ascii_hrhLN,
            R.string.ascii_haruhi3,
            R.string.ascii_sos
        )
        private var mImgIndex = 0
        private lateinit var foregroundImage: Array<CharArray>
        private var asciiString: CharSequence? = null
        private var imageHeight = 0
        private var imageMultiply: Int = 2

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
            text = loadLWP(applicationContext)
            textChar = text.toCharArray() // split the character of the text
            textLength = textChar.size
            fontSize = width / columnSize
            if (visible) {
                drawFrame()
                changePicture()
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
            //this is necessay to remove the call back
            mHandler.removeCallbacks(mDrawFrame)
            mHandler.removeCallbacks(mChangePic)
        }

        //this function contain the the main draw call
        /// this function need to call every time the code is executed
        // the thread call this function with some delay "drawspeed"
        private fun drawFrame() {
            //getting the surface holder
            val holder = surfaceHolder
            canvas = null // canvas
            try {
                canvas = holder.lockCanvas() //get the canvas
                if (canvas != null) {
                    // draw something
                    // canvas matrix draw code
                    canvasDraw()
                    //^^^^
                }
            } finally {
                if (canvas != null) holder.unlockCanvasAndPost(canvas)
            }

            // Reschedule the next redraw
            // this is the replacement for the invialidate funtion
            // every time call the drawFrame to draw the matrix
            mHandler.removeCallbacks(mDrawFrame)
            if (mVisible) {
                // set the execution delay
                mHandler.postDelayed(mDrawFrame, drawSpeed.toLong())
            }
        }

        private fun changePicture() {
            constructImage()
            mImgIndex = rand.nextInt(imgList.size)
            mHandler.postDelayed(mChangePic, changePicSpeed.toLong())
        }

        private fun initialise() {
            textPaint = Paint()
            backPaint = Paint()
            textPaint!!.style = Paint.Style.FILL
            textPaint!!.color = textColor
            textPaint!!.textSize = fontSize.toFloat()
            backPaint!!.color = backgroundColor
            backPaint!!.alpha = 5 //set the alpha
            backPaint!!.style = Paint.Style.FILL
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            // some matrix variable
            // though not needed
            val paint = Paint()
            paint.color = backgroundColor
            paint.alpha = 255 //set the alpha
            paint.style = Paint.Style.FILL
            this.width = width + 20
            this.height = height
            fontSize = width / columnSize
            constructImage()
            initialise()
            //initalise the textposiotn to zero
            textPosition = IntArray(columnSize + 1) //add one more drop
            for (x in 0 until columnSize) {
                textPosition[x] = 1
            }
            drawFrame()
            changePicture()
            canvas!!.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        }

        //old matrix effect code
        private fun drawText() {
            //loop and paint
            for (i in textPosition.indices) {
                // draw the text at the random position
                val y = textPosition[i]
                if (i >= 99 || y >= imageHeight * imageMultiply || foregroundImage[y][i] == 'ㅡ') {
                    textPaint!!.color = Color.parseColor("#00c800")
                    canvas!!.drawText(
                        "" + textChar[rand.nextInt(textLength)],
                        (i * fontSize).toFloat(), (y * fontSize).toFloat(),
                        textPaint!!
                    )
                    //
                } else {
                    textPaint!!.color = red
                    //canvas.drawText(".", x * fontSize, y * fontSize, paint);
                    canvas!!.drawText(
                        "" + foregroundImage[y][i],
                        (i * fontSize).toFloat(), (y * fontSize).toFloat(),
                        textPaint!!
                    )
                }

                // check if text has reached bottom or not
                //0.925 good
                if (textPosition[i] * fontSize > height && Math.random() > 0.900) //0.975
                    textPosition[i] =
                        0 // change text position to zero when 0 when text is at the bottom
                textPosition[i]++ //increment the position array
            }
        }

        private fun constructImage() {
            asciiString = getString(imgList[mImgIndex])
            imageHeight = (asciiString as String).length / 100
            foregroundImage = Array(imageHeight * imageMultiply) {
                CharArray(
                    100
                )
            }
            var modY = 0
            for (y in 0 until imageHeight) {
                for (x in 0..99) {
                    foregroundImage[modY][x] = (asciiString as String)[x + y * 100]
                    foregroundImage[modY + 1][x] = foregroundImage[modY][x]
                    //    foregroundImage[modY+2][x] =  foregroundImage[modY][x];
                }
                modY += imageMultiply
            }
        }

        //old martix effect code
        private fun canvasDraw() {
            //Log.d("canvas ","drawing");
            //set the paint for the canvas
            canvas!!.drawRect(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                backPaint!!
            ) //draw rect to clear the canvas
            drawText() // draw the canvas
        }
    }
}
