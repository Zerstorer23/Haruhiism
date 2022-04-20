package com.haruhi.bismark439.haruhiism.activities.navigation_ui.home

import android.Manifest
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.AddAlarmActivity
import com.haruhi.bismark439.haruhiism.activities.interfaces.IFragmentActivity
import com.haruhi.bismark439.haruhiism.databinding.FragmentHomeBinding
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmAdapter
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmDB
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmDao
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmData
import com.haruhi.bismark439.haruhiism.system.LauncherManager
import com.haruhi.bismark439.haruhiism.system.LauncherType
import com.haruhi.bismark439.haruhiism.system.StorageManager
import com.haruhi.bismark439.haruhiism.system.ui.Toaster
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class HomeFragment : IFragmentActivity<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate,
    HomeViewModel::class.java
) {

    private var easter = false
    private lateinit var adaptor: AlarmAdapter

    override fun onActivityStart() {
        onCheckPermission()
        setUpAddAlarmLauncher()
    }

    override fun onRefreshUI() {

        binding.easterEggButton.setOnClickListener { onEasterEgg() }
        binding.btnAddAlarm.setOnClickListener {
            onClickAddAlarm()
        }
        loadAlarms()
        binding.setWallPaperBtn.setOnClickListener { setWallpaper() }
    }



    fun setWallpaper() {
        val bitmap: Bitmap =
            BitmapFactory.decodeResource(resources, R.drawable.haruhi1)
        val wallpaperManager = WallpaperManager.getInstance(this.context)
        wallpaperManager.setBitmap(bitmap)

        Toaster.show(requireContext(), "Wallpaper set")
    }
/*
    fun getWallpaper() {
        val wmInstance = WallpaperManager.getInstance(this);
       wmInstance.getDrawable()
            .getBitmap()
            .compress(
                Bitmap.CompressFormat.PNG, 100,
                new FileOutputStream ("/storage/emulated/0/output.png")
            )
    }*/


    private fun onCheckPermission() {
        StorageManager.checkPermission(this.requireActivity(), Manifest.permission.RECEIVE_BOOT_COMPLETED)
    }

    private fun onClickAddAlarm() {
        val intent = Intent(requireContext(), AddAlarmActivity::class.java)
        LauncherManager.launch(LauncherType.CreateAlarm, intent)
    }

    private fun setUpAddAlarmLauncher() {
        LauncherManager.init(activity as AppCompatActivity, LauncherType.CreateAlarm) {
            val newAlarm = it.data!!.getParcelableExtra<AlarmData>("alarm")!!
            lifecycleScope.launch {
                println("Insert new alarm: " + newAlarm.reqCode)
                AlarmDao.instance.insert(newAlarm)
            }
        }
    }

    private fun loadAlarms() {
        AlarmDao.initDao(requireContext())
        lifecycleScope.launch {
            AlarmDao.instance.selectAll().collect {
                AlarmDB.alarmDB = ArrayList(it)
                println("alarm list updated : " + it.size)
                adaptor = AlarmAdapter(requireContext(), AlarmDB.alarmDB)
                binding.rvAlarms.layoutManager = LinearLayoutManager(requireContext())
                binding.rvAlarms.adapter = adaptor
                if (AlarmDB.alarmDB.size == 0) return@collect
                AlarmDB.safeRegisterAllAlarms(requireContext())
            }
        }
    }

    fun updateAlarm(alarmData: AlarmData, position: Int) {
        lifecycleScope.launch {
            AlarmDao.instance.update(alarmData)
            binding.rvAlarms.adapter!!.notifyItemChanged(position)
        }
    }

    fun removeAlarm(alarmData: AlarmData, position: Int) {
        AlarmDB.removeAlarm(requireContext(), alarmData.reqCode)
        lifecycleScope.launch {
            AlarmDao.instance.delete(alarmData)
            binding.rvAlarms.adapter?.notifyItemRemoved(position)
        }
    }


    override fun onResume() {
        super.onResume()
        println("notifyDataSetChanged called")
    }


    private fun onEasterEgg() {
        if (!easter) {
            binding.type.visibility = View.VISIBLE
            binding.type.animateText(getString(R.string.YUKI_6))
        } else {
            binding.type.visibility = View.GONE
        }
        easter = !easter
    }


}