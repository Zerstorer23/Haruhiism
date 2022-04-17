@file:Suppress("DEPRECATION")

package com.haruhi.bismark439.haruhiism.system

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

object ScreenManager {
    fun hideStatusBar(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    fun setupActionBar(compat: AppCompatActivity, toolbar: Toolbar, title: String = "") {
        compat.setSupportActionBar(toolbar)
        if (compat.supportActionBar != null) {
            compat.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            compat.supportActionBar?.setHomeAsUpIndicator(com.google.android.material.R.drawable.ic_mtrl_chip_checked_circle)
            if (title.isNotEmpty()) {
                compat.supportActionBar?.title = title
            }
        }
        toolbar.setNavigationOnClickListener { compat.onBackPressed() }
    }

    fun getBinaryVisibility(b: Boolean): BinaryVisibility {
        return if (b) {
            BinaryVisibility(View.VISIBLE, View.GONE)
        } else {
            BinaryVisibility(View.GONE, View.VISIBLE)
        }
    }

    fun toggleVisibility(show: View, hide: View) {
        show.visibility = View.VISIBLE
        hide.visibility = View.GONE
    }

    fun getVisibility(b: Boolean): Int {
        return if (b) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun setVisibilities(visible: Int, vararg views: View) {
        for (v in views) {
            v.visibility = visible
        }
    }

}

data class BinaryVisibility(
    val showIfTrue: Int,
    val showIfFalse: Int,
)