package com.haruhi.bismark439.haruhiism.wallPapers

import android.content.Context
import android.view.View
import com.haruhi.bismark439.haruhiism.activities.interfaces.BaseActivity
import com.haruhi.bismark439.haruhiism.databinding.ActivityYukiSettingBinding
import com.haruhi.bismark439.haruhiism.system.StorageManager

class ActivityYukiLWPSettings :
    BaseActivity<ActivityYukiSettingBinding>(ActivityYukiSettingBinding::inflate) {
    fun onClick(view: View) {
        val tag = view.tag as String
        saveLWP(applicationContext, tag)

/*        intent = Intent (
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(
            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
            new ComponentName (this, yukiWall.class)
        );
        startActivity(intent);*/
        finish()
    }

    companion object {
        fun loadLWP(context: Context): String {
            val pref = StorageManager.getPrefReader(context)
            return pref.getString("haruhiismLWP", "SUZUMIYA HARUHI")!!
        }

        fun saveLWP(context: Context, name: String?) {
            val editor = StorageManager.getPrefWriter(context)
            editor.putString("haruhiismLWP", name)
            editor.commit()
        }
    }
}
