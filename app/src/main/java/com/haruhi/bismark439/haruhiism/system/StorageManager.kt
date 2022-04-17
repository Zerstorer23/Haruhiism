package com.haruhi.bismark439.haruhiism.system

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object StorageManager {

    private const val PREF_NAME = "HARUHI_PREF"
    fun getSharedPrefEditor(context: Context):SharedPreferences.Editor{
        val sharedPref = getSharedPref(context)
        return sharedPref.edit()
    }
    fun getSharedPref(context: Context):SharedPreferences{
        return context.getSharedPreferences(
            PREF_NAME,
            AppCompatActivity.MODE_PRIVATE
        )
    }

    private const val READ_CODE = 1
    fun checkPermission(activity: Activity, permission:String) : Boolean{
        if (ContextCompat.checkSelfPermission(
                activity.applicationContext,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(permission),
                READ_CODE
            )
        }
        return false
    }

    fun onReadPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        if (requestCode == READ_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }

    fun launchImageLoader(type: LauncherType) {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        LauncherManager.launch(type, intent)
    }

    fun getFileExtension(activity: Activity, uri: Uri): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(
                activity.contentResolver.getType(uri)
            )
    }


}