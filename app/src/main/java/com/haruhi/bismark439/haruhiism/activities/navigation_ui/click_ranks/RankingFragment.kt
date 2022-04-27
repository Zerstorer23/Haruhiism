package com.haruhi.bismark439.haruhiism.activities.navigation_ui.click_ranks

import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.activities.interfaces.IFragmentActivity
import com.haruhi.bismark439.haruhiism.databinding.FragmentRankingBinding
import com.haruhi.bismark439.haruhiism.system.StorageManager
import com.haruhi.bismark439.haruhiism.system.firebase_manager.MyUser
import com.haruhi.bismark439.haruhiism.system.firebase_manager.ViewDatabaseHandler
import java.lang.Exception

class RankingFragment : IFragmentActivity<FragmentRankingBinding>(
    FragmentRankingBinding::inflate
) {
    companion object {
        var initDB: Boolean = false
        const val CLICKS_MINE = "MyClicks"
    }

    private var myClicks = 0

    override fun onActivityStart() {
        if (!initDB) {
            initUser()
        }
    }

    override fun onRefreshUI() {
        loadMyClicks()
        onDataLoaded()
    }

    private fun loadMyClicks(){
        val reader = StorageManager.getPrefReader(requireContext())
        myClicks = reader.getInt(CLICKS_MINE,0)
        binding.tvMyCount.text = myClicks.toString()
    }

    override fun onResume() {
        super.onResume()
        loadMyClicks()
    }
    private fun initUser() {
        ViewDatabaseHandler.getSum {
            Debugger.log("Load sum? $it ${MyUser.totalViews}")
            if(it){
                initDB = true
                onDataLoaded()
                ViewDatabaseHandler.listenSumChanges(::onDataLoaded)
            }
        }

    }




    private fun onDataLoaded() {
        //${}
        if (context == null) return
        try {
            binding.tvMyCount.text = myClicks.toString()
            binding.tvSumCount.text = MyUser.totalViews.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}



