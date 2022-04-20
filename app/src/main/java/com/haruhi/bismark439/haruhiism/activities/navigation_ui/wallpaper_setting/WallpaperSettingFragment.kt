package com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.interfaces.IFragmentActivity
import com.haruhi.bismark439.haruhiism.activities.navigation_ui.home.HomeViewModel
import com.haruhi.bismark439.haruhiism.databinding.FragmentHomeBinding
import com.haruhi.bismark439.haruhiism.databinding.FragmentWallpaperSettingBinding

class WallpaperSettingFragment :
    IFragmentActivity<FragmentWallpaperSettingBinding, WallpaperSettingViewModel>(
        FragmentWallpaperSettingBinding::inflate,
        WallpaperSettingViewModel::class.java
    ) {


    override fun onRefreshUI() {
        binding.tvBase.text = "준비중입니다... \n Service in preparation..."
    }

    override fun onActivityStart() {

    }

}