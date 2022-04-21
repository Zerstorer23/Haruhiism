package com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting

import android.Manifest
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.core.graphics.drawable.toBitmap
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.interfaces.IFragmentActivity
import com.haruhi.bismark439.haruhiism.databinding.FragmentWallpaperSettingBinding
import com.haruhi.bismark439.haruhiism.system.*
import com.haruhi.bismark439.haruhiism.system.ui.Toaster
import java.io.FileOutputStream


class WallpaperSettingFragment :
    IFragmentActivity<FragmentWallpaperSettingBinding>(
        FragmentWallpaperSettingBinding::inflate
    ) {
    var folderPath = "folder path"
    var randomise = false
    var isEnabled = false
    var numFiles = 0
    var timeVal = 1
    var timeUnit = TimeUnit.Day
    var cropType = CropType.Fit

    var launcherInit = false
    val type = LauncherType.SelectPath
    //val act2 =
    private val fileExplorerLauncher =initActivityLauncher(this::onPathSelected)
    val isOnProgress = true
    override fun onRefreshUI() {
        val bivis = ScreenManager.getBinaryVisibility(isOnProgress)
        binding.mainBoard.visibility=bivis.showIfFalse
        binding.workOnProgress.visibility=bivis.showIfTrue

        binding.btnOpenFolder.setOnClickListener {
            onClickOpenFolder()
        }
       binding.etSelectedPath.setText(folderPath)
        binding.tvFilesFound.text = "$numFiles files found"
        binding.rgFillType.check(cropType.getRadioGroupId())
        binding.etTimeVal.setText(timeVal.toString())
        /*   setSpinner()*/
         binding.btnLeftSet.setOnClickListener { setWallpaper(R.drawable.wp_haruhi) }
          binding.btnRightSet.setOnClickListener { setWallpaper(R.drawable.wp_nagato) }
    }


    override fun onActivityStart() {
        loadData()
    }

    override fun onPause() {
        if(!launcherInit){

            launcherInit = true
        }
        super.onPause()
    }


    private fun loadData() {

    }

    private fun saveData() {

    }

    private fun setWallpaper(id: Int) {
        val bitmap: Bitmap =
            BitmapFactory.decodeResource(resources, id)
        val wallpaperManager = WallpaperManager.getInstance(requireContext())
        wallpaperManager.setBitmap(bitmap)
        Toaster.show(requireContext(), "Wallpaper set")
    }

    @SuppressLint("MissingPermission")
    fun getWallpaper() {
        val wmInstance = WallpaperManager.getInstance(requireContext());
        wmInstance.getDrawable().toBitmap()
            .compress(
                Bitmap.CompressFormat.PNG, 100,
                FileOutputStream("/storage/emulated/0/output.png")
            )
    }


    fun CropType.getRadioGroupId(): Int {
        return when (this) {
            CropType.Fit -> binding.rbFit.id
            CropType.Fill -> binding.rbFill.id
            CropType.Stretch -> binding.rbStretch.id
        }
    }

    var timeUnits = arrayOf("Day", "Hours", "Minutes")
    private fun setSpinner() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                timeUnits
            )
        binding.spTimeUnit.adapter = adapter

        binding.spTimeUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                timeUnit = TimeUnit.values()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                timeUnit = TimeUnit.Day
            }
        }

    }

    private fun onClickOpenFolder() {
        PermissionManager.checkPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        {
            launchFileExplorer()
        }
    }

    private fun launchFileExplorer() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        fileExplorerLauncher.launch(intent)
    }

    private fun onPathSelected(res: ActivityResult) {
        binding.etSelectedPath.setText(res.data.toString())
    }

}
