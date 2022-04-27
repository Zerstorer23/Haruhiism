package com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.lifecycle.lifecycleScope
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.interfaces.IFragmentActivity
import com.haruhi.bismark439.haruhiism.databinding.FragmentWallpaperSettingBinding
import com.haruhi.bismark439.haruhiism.system.*
import com.haruhi.bismark439.haruhiism.system.ui.Toaster
import com.haruhi.bismark439.haruhiism.system.wallpapers.WallpaperBroadcastManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


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
    }

    private fun loadOptions() {
        if (init) {
            postInit()
            return
        }
        lifecycleScope.launch(Dispatchers.Default) {
            option = MyWallpaperOption.loadData(requireContext())
            option.readFiles(requireContext())
            withContext(Dispatchers.Main) {
                postInit()
            }
            init = true
        }
    }

    private fun preInit() {
        binding.btnOpenFolder.setOnClickListener {
            onClickOpenFolder()
        }
    }

    private fun onClickSave() {
        if (option.imgList.isEmpty()) return
        lifecycleScope.launch(Dispatchers.Default) {
            saveSettings(true)
            withContext(Dispatchers.Main) {
                if (option.isEnabled) {
                    Toaster.show(requireContext(), getString(R.string.txt_wallpaper_set))
                } else {
                    Toaster.show(requireContext(), getString(R.string.txt_disabled_auto))
                }
            }
        }
    }

    override fun onRefreshUI() {
        binding.spLoading.visibility = View.VISIBLE
        binding.mainBoard.visibility = View.GONE
        preInit()
        loadOptions()
    }

    private fun postInit() {
        setSpinner()
        updateEnabled()
        updatePreviews()
        updateQuotations()
        binding.tvFilesFound.text =
            getString(R.string.txt_files_found, option.imgList.size.toString())
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
        setDebugWindow()
        binding.spLoading.visibility = View.GONE
        binding.mainBoard.visibility = View.VISIBLE
    }

    private fun setDebugWindow() {
        val lastCalStr = Helper.getTimeString(requireContext(), option.lastSet)
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val next = alarmManager.nextAlarmClock
        //    val nextStr = Helper.getTimeString(requireContext(),next.triggerTime)
        binding.tvDebugWindow.text = "last set: $lastCalStr"// \n next : $nextStr"
        //    Debugger.log("next: ${next.showIntent.describeContents()}")
        //  Debugger.log("next: ${next.showIntent}")
        //  Debugger.log("next: ${next.describeContents()}")
    }

    private fun updateQuotations() {
        val vis = ScreenManager.getBinaryVisibility(option.addTexts)
        binding.etCustomQuotes.visibility = vis.showIfTrue
        binding.etCustomQuotes.setText(option.customText)
        binding.cbAddTextOption.isChecked = option.addTexts
        binding.cbAddTextOption.setOnCheckedChangeListener { _, isChecked ->
            option.addTexts = isChecked
            binding.etCustomQuotes.visibility = ScreenManager.getVisibility(isChecked)
        }
    }

    private fun updatePreviews() {
        binding.tvFilesFound.text =
            getString(R.string.txt_files_found, option.imgList.size.toString())
        binding.etSelectedPath.setText(option.simplePath)
        val imgArr = arrayOf(binding.imgLeft, binding.imgCenter, binding.imgRight)
        ScreenManager.setVisibilities(View.INVISIBLE, *imgArr)
        val size = option.imgList.size
        if (size == 0) return
        for (i in imgArr) {
            i.visibility = View.VISIBLE
            val imguri = option.getRandomUri() ?: return
            i.setImageURI(imguri)
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
    private fun setSpinner() {
        SpinnerFactory.createSpinner(requireContext(), timeUnits, binding.spTimeUnit) {
            option.timeUnit = TimeUnit.values()[it]
        }
        binding.spTimeUnit.setSelection(option.timeUnit.ordinal)
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

    @SuppressLint("WrongConstant")
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
        requireActivity().contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        option.setPathUri(uri)
        lifecycleScope.launch {
            option.readFiles(requireContext())
            updatePreviews()
            if (!option.isEnabled) {
                withContext(Dispatchers.Main) {
                    binding.btnToggleWallpaper.performClick()
                }
            }
            saveSettings(false)
        }

    }

    private fun saveSettings(setImmediately: Boolean) {
        try {
            option.setTimeVal(binding.etTimeVal.text.toString())
            option.customText = binding.etCustomQuotes.text.toString()
        } catch (e: Exception) {

        }
        MyWallpaperOption.saveData(requireContext(), option)
        if (!setImmediately) return
        Debugger.log("Saving settings...")
        WallpaperBroadcastManager.updateWallpaper(requireContext(), option, option.isEnabled)
        setDebugWindow()
    }


}
