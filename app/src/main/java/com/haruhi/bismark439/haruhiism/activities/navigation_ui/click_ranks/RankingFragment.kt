package com.haruhi.bismark439.haruhiism.activities.navigation_ui.click_ranks

import com.haruhi.bismark439.haruhiism.activities.interfaces.IFragmentActivity
import com.haruhi.bismark439.haruhiism.databinding.FragmentRankingBinding
import com.haruhi.bismark439.haruhiism.system.StorageManager
import com.haruhi.bismark439.haruhiism.system.firebase_manager.FirebaseHandler

class RankingFragment : IFragmentActivity<FragmentRankingBinding>(
    FragmentRankingBinding::inflate
) {
    companion object {
        const val CLICKS_MINE = "MyClicks"
    }

    private var myClicks = 0
    private var totalClicks = 0L

    override fun onActivityStart() {
        loadClicks()
    }

    override fun onRefreshUI() {
        loadClicks()
    }

    private fun loadMyClicks() {
        val reader = StorageManager.getPrefReader(requireContext())
        myClicks = reader.getInt(CLICKS_MINE, 0)
    }

    override fun onResume() {
        super.onResume()
        loadClicks()
    }

    private fun loadClicks() {
        FirebaseHandler.getCounts {
            totalClicks = it
            onDataLoaded()
        }
    }


    private fun onDataLoaded() {
        //${}
        if (context == null) return
        try {
            loadMyClicks()
            binding.tvMyCount.text = myClicks.toString()
            binding.tvSumCount.text = totalClicks.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}



