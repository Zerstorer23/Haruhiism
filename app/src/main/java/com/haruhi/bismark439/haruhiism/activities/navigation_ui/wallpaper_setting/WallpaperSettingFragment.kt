package com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting

import com.haruhi.bismark439.haruhiism.activities.interfaces.IFragmentActivity
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