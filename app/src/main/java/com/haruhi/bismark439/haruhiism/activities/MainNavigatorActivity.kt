package com.haruhi.bismark439.haruhiism.activities

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.interfaces.BaseActivity
import com.haruhi.bismark439.haruhiism.databinding.ActivityMainNavigatorBinding
import com.haruhi.bismark439.haruhiism.system.PermissionManager
import com.haruhi.bismark439.haruhiism.system.alarms.BootReceiver

class MainNavigatorActivity :
    BaseActivity<ActivityMainNavigatorBinding>(ActivityMainNavigatorBinding::inflate) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        registerBootReceiver()
        val navView: BottomNavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navControllerView) as NavHostFragment
        val navController = navHostFragment.navController
       // val navController = findNavController(R.id.navControllerView)
        navView.setupWithNavController(navController)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PermissionManager.checkPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.SCHEDULE_EXACT_ALARM,
            )
            {
                Debugger.log("Success permission")
            }
        }else{
            PermissionManager.checkPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
            {
                Debugger.log("Success permission")
            }
        }
    }

    private fun registerBootReceiver() {
        val bootReceiver = BootReceiver()
        val filter = IntentFilter(Intent.ACTION_BOOT_COMPLETED)
        registerReceiver(bootReceiver, filter)
        Debugger.log("Registered boot receiver")
//        applicationContext.startForegroundService(Intent(applicationContext, BootingService::class.java))
    }
}