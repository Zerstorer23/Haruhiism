package com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.system.CropType
import com.haruhi.bismark439.haruhiism.system.StorageManager
import com.haruhi.bismark439.haruhiism.system.TimeUnit

class MyWallpaperOption {
    companion object {
        private const val NOT_FOUND = "WP_NOT_FOUND"
        private const val FOLDER_PATH = "WP_folderPath"
        private const val RANDOMISE = "WP_randomise"
        private const val ENABLED = "WP_enabled"
        private const val ADD_TEXTS = "WP_addTexts"
        private const val CUSTOM_TEXT = "WP_customTexts"
        private const val TIME_VAL = "WP_timeVal"
        private const val TIME_UNIT = "WP_timeUnit"
        private const val CROP_TYPE = "WP_cropType"
        private const val ITERATOR = "WP_iterator"
        fun loadData(context: Context): MyWallpaperOption {
            val option = MyWallpaperOption()
            val sharedPref = StorageManager.getPrefReader(context)
            val pathString = sharedPref.getString(FOLDER_PATH, NOT_FOUND)
            if (pathString != NOT_FOUND) {
                option.folderPathUri = Uri.parse(pathString)!!
                option.setPathUri(option.folderPathUri)
            }
            option.customText = sharedPref.getString(CUSTOM_TEXT, "")!!
            option.randomise = sharedPref.getBoolean(RANDOMISE, true)
            option.isEnabled = sharedPref.getBoolean(ENABLED, true)
            option.addTexts = sharedPref.getBoolean(ADD_TEXTS, false)
            option.timeVal = sharedPref.getInt(TIME_VAL, 1)
            option.iterator = sharedPref.getInt(ITERATOR, 0)
            option.timeUnit = TimeUnit.values()[sharedPref.getInt(TIME_UNIT, 0)]
            option.cropType = CropType.values()[sharedPref.getInt(CROP_TYPE, 0)]

            return option
        }

        fun saveData(context: Context, option: MyWallpaperOption) {
            val editor = StorageManager.getPrefWriter(context)
            editor.putString(FOLDER_PATH, option.folderPathUri.toString())
            editor.putString(CUSTOM_TEXT, option.customText)
            editor.putBoolean(RANDOMISE, option.randomise)
            editor.putBoolean(ENABLED, option.isEnabled)
            editor.putBoolean(ADD_TEXTS, option.addTexts)
            editor.putInt(TIME_VAL, option.timeVal)
            editor.putInt(TIME_UNIT, option.timeUnit.ordinal)
            editor.putInt(CROP_TYPE, option.cropType.ordinal)
            editor.putInt(ITERATOR, option.iterator)
            editor.apply()
        }

        fun savePartial(context: Context, option: MyWallpaperOption, key: String) {
            val editor = StorageManager.getPrefWriter(context)
            when (key) {
                ITERATOR -> editor.putInt(ITERATOR, option.iterator)
                else -> {}
            }
            editor.apply()
        }
    }

    var folderPathUri: Uri? = null// = Uri.parse(NOT_FOUND)
    var randomise = true
    var isEnabled = true
    var addTexts = false
    var customText = ""
    var timeVal = 1
    var timeUnit = TimeUnit.Day
    var cropType = CropType.Fill
    var iterator = 0

    var imgList: ArrayList<Uri> = arrayListOf()
    var simplePath: String = ""
    private var pathIsValid = false

    fun setPathUri(uri: Uri?) {
        folderPathUri = uri
        pathIsValid = folderPathUri != null
        if (!pathIsValid) return

        try {
            val docUri = DocumentsContract.buildDocumentUriUsingTree(
                uri,
                DocumentsContract.getTreeDocumentId(uri)
            )
            simplePath = docUri.lastPathSegment!!
        } catch (e: java.lang.Exception) {
            pathIsValid = false
            e.printStackTrace()
        }

    }

    fun readFiles(context: Context) {
        imgList.clear()
        if (!pathIsValid) return
        var cursor: Cursor? = null
        try {
            Debugger.log("Read " + folderPathUri.toString())
            // the uri returned by Intent.ACTION_OPEN_DOCUMENT_TREE
            // the uri from which we query the files
            val uriFolder = DocumentsContract.buildChildDocumentsUriUsingTree(
                folderPathUri,
                DocumentsContract.getTreeDocumentId(folderPathUri)
            )

            // let's query the files
            cursor = context.contentResolver.query(
                uriFolder, arrayOf(DocumentsContract.Document.COLUMN_DOCUMENT_ID),
                null, null, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // build the uri for the file
                    val uriFile =
                        DocumentsContract.buildDocumentUriUsingTree(
                            folderPathUri,
                            cursor.getString(0)
                        )
//val fileName = File(uri.path).name
//// or
//val fileName = uri.pathSegments.last()
                    //add to the list
                    if (StorageManager.isAnImage(context, uriFile)) {
                        imgList.add(uriFile)
                    }
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        pathIsValid = true
        if (pathIsValid) {
            Debugger.log("Found ${imgList.size} files")
        }
    }

    fun getTimeUnitInMills(): Long {
        return when (timeUnit) {
            TimeUnit.Day -> 24 * 60 * 60 * 1000//Get tonight
            TimeUnit.Hour -> 60 * 60 * 1000
            TimeUnit.Minutes -> 60 * 1000
        }
    }

    fun getRandomUri(): Uri? {
        if (!canPollImages()) return null
        return imgList[(Math.random() * imgList.size).toInt()]
    }

    fun getNextUri(context: Context): Uri? {
        if (!canPollImages()) return null
        return if (randomise) {
            getRandomUri()
        } else {
            val curr = iterator
            iterator++
            iterator %= imgList.size
            savePartial(context, this, ITERATOR)
            imgList[curr]
        }
    }

    private fun canPollImages(): Boolean {
        return pathIsValid && imgList.size > 0
    }

    fun setTimeVal(text: String) {
        try {
            val number = text.toInt()
            if (timeVal <= 0) return
            timeVal = number
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }
}