package com.haruhi.bismark439.haruhiism.system.wallpapers

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.graphics.drawable.toBitmap
import java.io.FileOutputStream

object WallpaperHandler {

    fun setWallpaper(context: Context, id: Int) {
        val bitmap: Bitmap =
            BitmapFactory.decodeResource(context.resources, id)
        setWallpaper(context, bitmap)
    }
    fun setWallpaper(context: Context, uri: Uri) {
        val bitmap = loadBitmapFromUri(context, uri)
        setWallpaper(context, bitmap)
    }

    fun setWallpaper(context: Context, bitmap:Bitmap) {
        val wallpaperManager = WallpaperManager.getInstance(context)
        wallpaperManager.setBitmap(bitmap)
    }
    fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap {
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


    @SuppressLint("MissingPermission")
    fun getWallpaper(context: Context) {
        val wmInstance = WallpaperManager.getInstance(context);
        wmInstance.getDrawable().toBitmap()
            .compress(
                Bitmap.CompressFormat.PNG, 100,
                FileOutputStream("/storage/emulated/0/output.png")
            )
    }
}