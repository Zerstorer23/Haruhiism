package com.haruhi.bismark439.haruhiism.system

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity

object StorageManager {

    private const val PREF_NAME = "HARUHI_PREF"
    fun getPrefWriter(context: Context): SharedPreferences.Editor {
        val sharedPref = getPrefReader(context)
        return sharedPref.edit()
    }

    fun getPrefReader(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            PREF_NAME,
            AppCompatActivity.MODE_PRIVATE
        )
    }

    fun readFilesFromAsset(context: Context, folderName: String): Array<String> {
        val assetManager = context.assets
        return assetManager.list(folderName) as Array<String>
    }


    fun launchImageLoader(type: LauncherType) {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        LauncherManager.launch(type, intent)
    }

    private fun getFileExtension(context: Context, uri: Uri): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(
                context.contentResolver.getType(uri)
            )
    }

    private val imageExts = hashSetOf("gif", "jpg", "png", "jpeg")
    fun isAnImage(context: Context, uri: Uri): Boolean {
        val ext = getFileExtension(context, uri)
        return imageExts.contains(ext)
        //https://stackoverflow.com/questions/5568874/how-to-extract-the-file-name-from-uri-returned-from-intent-action-get-content
    }

}