package com.haruhi.bismark439.haruhiism.activities

import android.Manifest
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.haruhi.bismark439.haruhiism.DEBUG
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.interfaces.BaseActivity
import com.haruhi.bismark439.haruhiism.databinding.ActivityMainNavigatorBinding
import com.haruhi.bismark439.haruhiism.system.PermissionManager

class MainNavigatorActivity :
    BaseActivity<ActivityMainNavigatorBinding>(ActivityMainNavigatorBinding::inflate) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.navControllerView)
        navView.setupWithNavController(navController)

        if (DEBUG.debugMode) {
            PermissionManager.checkPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            {
                DEBUG.appendLog("Success permission")
            }
        }
    }
}