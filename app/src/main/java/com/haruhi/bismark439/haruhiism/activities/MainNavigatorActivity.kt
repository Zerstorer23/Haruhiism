package com.haruhi.bismark439.haruhiism.activities

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.haruhi.bismark439.haruhiism.Debugger
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
        PermissionManager.checkPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        {
            Debugger.log("Success permission")
        }
        val a = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Debugger.log("next alarm = "+a.nextAlarmClock.showIntent.toString())
        Debugger.log("next alarm = "+a.nextAlarmClock.triggerTime.toString())
    }
}