package com.haruhi.bismark439.haruhiism.activities

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.interfaces.BaseActivity
import com.haruhi.bismark439.haruhiism.databinding.ActivityMainBinding
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmAdapter
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmDB
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmDao
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmData
import com.haruhi.bismark439.haruhiism.system.LauncherManager
import com.haruhi.bismark439.haruhiism.system.LauncherType
import com.haruhi.bismark439.haruhiism.system.StorageManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@DelicateCoroutinesApi
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private var easter = false

    // private var demolish = false
    private lateinit var adaptor: AlarmAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // demolish = false
        setUpAddAlarmLauncher()
        binding.easterEggButton.setOnClickListener { onEasterEgg() }
        binding.btnAddAlarm.setOnClickListener {
            onClickAddAlarm()
        }
        loadAlarms()
    }



    private fun onClickAddAlarm() {
        val intent = Intent(this, AddAlarmActivity::class.java)
        LauncherManager.launch(LauncherType.CreateAlarm, intent)
    }

    private fun setUpAddAlarmLauncher() {
        LauncherManager.init(this, LauncherType.CreateAlarm) {
            val newAlarm = it.data!!.getParcelableExtra<AlarmData>("alarm")!!
            lifecycleScope.launch {
                println("Insert new alarm: " + newAlarm.reqCode)
                AlarmDao.instance.insert(newAlarm)
            }
        }
    }

    private fun loadAlarms() {
        AlarmDao.initDao(this)
        lifecycleScope.launch {
            AlarmDao.instance.selectAll().collect {
                AlarmDB.alarmDB = ArrayList(it)
                println("alarm list updated : " + it.size)
                adaptor = AlarmAdapter(this@MainActivity, AlarmDB.alarmDB, ::removeAlarm,::updateAlarm)
                binding.rvAlarms.layoutManager = LinearLayoutManager(this@MainActivity)
                binding.rvAlarms.adapter = adaptor
                if (AlarmDB.alarmDB.size == 0) return@collect
                AlarmDB.safeRegisterAllAlarms(this@MainActivity)
            }
        }
    }

    private fun updateAlarm(alarmData: AlarmData, position: Int) {
        lifecycleScope.launch {
            AlarmDao.instance.update(alarmData)
            binding.rvAlarms.adapter!!.notifyItemChanged(position)
        }
    }

    private fun removeAlarm(alarmData: AlarmData, position: Int) {
        AlarmDB.removeAlarm(this, alarmData.reqCode)
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
