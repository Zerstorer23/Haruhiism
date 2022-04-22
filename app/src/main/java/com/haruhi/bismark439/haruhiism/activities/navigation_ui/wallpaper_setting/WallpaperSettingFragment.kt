package com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.lifecycle.lifecycleScope
import com.haruhi.bismark439.haruhiism.DEBUG
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.interfaces.IFragmentActivity
import com.haruhi.bismark439.haruhiism.databinding.FragmentWallpaperSettingBinding
import com.haruhi.bismark439.haruhiism.system.*
import com.haruhi.bismark439.haruhiism.system.ui.Toaster
import com.haruhi.bismark439.haruhiism.system.wallpapers.WallpaperBroadcastManager
import com.haruhi.bismark439.haruhiism.system.wallpapers.WallpaperHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class WallpaperSettingFragment :
    IFragmentActivity<FragmentWallpaperSettingBinding>(
        FragmentWallpaperSettingBinding::inflate
    ) {
    companion object {
        private var init = false
        private var option: MyWallpaperOption = MyWallpaperOption()
    }

    //val act2 =
    private val fileExplorerLauncher = initActivityLauncher(this::onPathSelected)

    override fun onActivityStart() {
        timeUnits = arrayOf(
            getString(R.string.txt_day),
            getString(R.string.txt_hours),
            getString(R.string.minute)
        )
        DEBUG.start()
    }

    private fun loadOptions() {
        DEBUG.lap("start loading")
        if (init) {
            postInit()
            return
        }
        lifecycleScope.launch(Dispatchers.Default) {
            option = MyWallpaperOption.loadData(requireContext())
            DEBUG.lap("load option ")
            option.readFiles(requireContext())
            DEBUG.lap("Read files ")
            withContext(Dispatchers.Main) {
                postInit()
            }
            DEBUG.lap("update UI")
            init = true
        }
    }

    private fun preInit() {
        setSpinner()
        binding.btnOpenFolder.setOnClickListener {
            onClickOpenFolder()
        }
    }

    private fun onClickSave() {
        if (option.imgList.isEmpty()) return
        lifecycleScope.launch(Dispatchers.Default) {
            saveSettings(true)
            withContext(Dispatchers.Main) {
                Toaster.show(requireContext(), "Wallpaper set")
            }
        }
    }

    override fun onRefreshUI() {
        binding.spLoading.visibility = View.VISIBLE
        binding.mainBoard.visibility = View.GONE
        preInit()
        loadOptions()
        DEBUG.lap("Finish loading")
    }

    private fun postInit() {
        updateEnabled()
        updatePreviews()
        updateQuotations()
        binding.tvFilesFound.text = "${option.imgList.size} images found"
        binding.rgFillType.check(option.cropType.getRadioGroupId())
        binding.etTimeVal.setText(option.timeVal.toString())
        binding.cbRandom.isChecked = option.randomise
        binding.cbRandom.setOnCheckedChangeListener { _, isChecked ->
            option.randomise = isChecked
        }
        binding.btnToggleWallpaper.setOnClickListener {
            onToggleEnabled()
        }
        binding.btnRightSet.setOnClickListener {
            onClickSave()
        }

        binding.spLoading.visibility = View.GONE
        binding.mainBoard.visibility = View.VISIBLE
    }

    private fun updateQuotations() {
        val vis = ScreenManager.getBinaryVisibility(option.addTexts)
        binding.etCustomQuotes.visibility = vis.showIfTrue
        binding.cbAddTextOption.isChecked = option.addTexts
        binding.cbAddTextOption.setOnCheckedChangeListener { buttonView, isChecked ->
            option.addTexts = isChecked
            binding.etCustomQuotes.visibility = ScreenManager.getVisibility(isChecked)
        }
    }

    private fun updatePreviews() {
        binding.tvFilesFound.text =
            getString(R.string.txt_files_found, option.imgList.size.toString())
        binding.etSelectedPath.setText(option.simplePath)
        val size = option.imgList.size
        val imgArr = arrayOf(binding.imgLeft, binding.imgCenter, binding.imgRight)
        ScreenManager.setVisibilities(View.INVISIBLE, *imgArr)
        if (size == 0) return
        for (i in imgArr) {
            i.visibility = View.VISIBLE
            i.setImageURI(option.getRandomUri())
        }
    }

    private fun onToggleEnabled() {
        option.isEnabled = !option.isEnabled
        updateEnabled()
        lifecycleScope.launch(Dispatchers.Default) {
            saveSettings(true)
        }
    }

    private fun updateEnabled() {
        if (option.isEnabled) {
            binding.tvWallpaperStatus.text = getString(R.string.txt_wallpaper_is_on)
            binding.tvWallpaperStatus.setTextColor(Color.GREEN)
            binding.btnToggleWallpaper.text = getString(R.string.txt_disable_wallpaper)
        } else {
            binding.tvWallpaperStatus.text = getString(R.string.txt_wallpaper_is_off)
            binding.tvWallpaperStatus.setTextColor(Color.RED)
            binding.btnToggleWallpaper.text = getString(R.string.txt_enable_wallpaper)
        }

    }


    private fun CropType.getRadioGroupId(): Int {
        return when (this) {
            CropType.Fit -> binding.rbFit.id
            CropType.Fill -> binding.rbFill.id
            CropType.Stretch -> binding.rbStretch.id
        }
    }

    private lateinit var timeUnits: Array<String>
    private val spinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            option.timeUnit = TimeUnit.values()[position]
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            println("NOthing selected")
        }
    }

    private fun setSpinner() {
        binding.spTimeUnit.adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            timeUnits
        )
        binding.spTimeUnit.setSelection(option.timeUnit.ordinal)
        binding.spTimeUnit.onItemSelectedListener = spinnerListener
    }

    private fun onClickOpenFolder() {
        PermissionManager.checkPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        {
            launchFileExplorer()
        }
    }

    private fun launchFileExplorer() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.putExtra("android.content.extra.SHOW_ADVANCED", true)
        fileExplorerLauncher.launch(intent)
    }

    private fun onPathSelected(res: ActivityResult) {
        if (res.data == null) {
            binding.etSelectedPath.setText(getString(R.string.wp_invalid_paths))
            return
        }
        val uri = res.data!!.data!!
        option.setPathUri(uri)
        lifecycleScope.launch {
            option.readFiles(requireContext())
            updatePreviews()
            saveSettings(false)
        }

    }

    private fun saveSettings(setImmediately: Boolean) {
        try {
            option.timeVal = binding.etTimeVal.text.toString().toInt()
            option.customText = binding.etCustomQuotes.text.toString()
        } catch (e: Exception) {

        }
        MyWallpaperOption.saveData(requireContext(), option)
        if (!setImmediately) return
        println("Saving settings...")
        DEBUG.printStack()
        WallpaperBroadcastManager.updateWallpaper(requireContext(), option, option.isEnabled)
    }


}
