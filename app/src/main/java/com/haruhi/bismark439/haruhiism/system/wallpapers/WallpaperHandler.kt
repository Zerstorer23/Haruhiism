package com.haruhi.bismark439.haruhiism.system.wallpapers

import android.app.AlarmManager
import android.app.WallpaperManager
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.DisplayMetrics
import com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting.MyWallpaperOption
import com.haruhi.bismark439.haruhiism.system.physicalScreenRectDp
import java.util.*
import kotlin.math.max
import kotlin.math.min


object WallpaperHandler {

    fun setWallpaper(context: Context, uri: Uri, option: MyWallpaperOption) {
        var bitmap = loadBitmapFromUri(context, uri)
        if (option.addTexts) {
            bitmap = cropCenter(context, bitmap)
            bitmap = drawText(context, bitmap, option.customText)
        }
        option.setAndSaveLastSet(context)
        val wallpaperManager = WallpaperManager.getInstance(context)
        wallpaperManager.setBitmap(bitmap)
    }

    private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                uri
            )
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
    }

    private fun drawText(context: Context, bitmap: Bitmap, text: String): Bitmap {
        val mutableBitmap: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val metrics: DisplayMetrics = context.resources.displayMetrics
        drawDate(metrics, mutableBitmap, canvas)
        drawQuote(metrics, mutableBitmap, canvas, text)
        return mutableBitmap
    }

    private fun drawDate(metrics: DisplayMetrics, bitmap: Bitmap, canvas: Canvas) {
        val centerX = bitmap.width * 0.1f
        val upperY = bitmap.height * 0.15f
        val size = 24f * metrics.density
        val cal = Calendar.getInstance()
        val dateStr = (cal[Calendar.MONTH] + 1).toString() + "/" + cal[Calendar.DATE]

        Paint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            this.color = Color.BLUE// Color.parseColor("#B7ADED")
            this.textSize = size
            typeface = Typeface.DEFAULT
            setShadowLayer(5f, 0f, 0f, Color.WHITE)
            canvas.drawText(dateStr, centerX, upperY, this)
        }
    }

    private const val max_side_length = 17
    private fun drawQuote(metrics: DisplayMetrics, bitmap: Bitmap, canvas: Canvas, text: String) {
        val centerX = bitmap.width * 0.1f
        var upperY = bitmap.height * 0.225f
        val size = 8f * metrics.density

        for (i in 0..text.length step max_side_length) {
            val wrappedText = text.substring(i, min(i + max_side_length, text.length))
            Paint().apply {
                flags = Paint.ANTI_ALIAS_FLAG
                this.color = Color.parseColor("#A3DCE4")
                this.textSize = size
                typeface = Typeface.DEFAULT
                setShadowLayer(5f, 0f, 0f, Color.parseColor("#C8FFFFFF"))
                canvas.drawText(wrappedText, centerX, upperY, this)
            }
            upperY += 13f * metrics.density
        }


    }

    private fun cropCenter(context: Context, srcBmp: Bitmap): Bitmap {
        val height = context.physicalScreenRectDp.height().toInt()
        val width = context.physicalScreenRectDp.width().toInt()
        val dstBmp: Bitmap

        val startX = max(0, srcBmp.width / 2 - width / 2)
        val startY = max(0, srcBmp.height / 2 - height / 2)
        val endX = min(srcBmp.width, width)
        val endY = min(srcBmp.height, height)

        dstBmp = Bitmap.createBitmap(
            srcBmp,
            startX,
            startY,
            endX,
            endY
        )


        return dstBmp
    }

/*    @SuppressLint("MissingPermission")
    fun getWallpaper(context: Context) {
        val wmInstance = WallpaperManager.getInstance(context)
        wmInstance.drawable.toBitmap()
            .compress(
                Bitmap.CompressFormat.PNG, 100,
                FileOutputStream("/storage/emulated/0/output.png")
            )
    }*/
}