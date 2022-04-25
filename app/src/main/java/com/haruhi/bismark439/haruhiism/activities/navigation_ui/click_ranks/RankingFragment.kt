package com.haruhi.bismark439.haruhiism.activities.navigation_ui.click_ranks

import com.haruhi.bismark439.haruhiism.DEBUG
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.interfaces.IFragmentActivity
import com.haruhi.bismark439.haruhiism.databinding.FragmentRankingBinding
import com.haruhi.bismark439.haruhiism.system.firebase_manager.MyUser
import com.haruhi.bismark439.haruhiism.system.firebase_manager.ViewDatabaseHandler
import java.lang.Exception

class RankingFragment : IFragmentActivity<FragmentRankingBinding>(
    FragmentRankingBinding::inflate
) {
    companion object {
        var initDB: Boolean = false
    }

    override fun onActivityStart() {
        if (!initDB) {
            initUser()
        }
    }

    override fun onRefreshUI() {
        binding.btAdd.setOnClickListener {
            onClickIncrement()
        }
        onDataLoaded()

    }

    private fun onClickIncrement() {
        ViewDatabaseHandler.incrementView {
            onDataLoaded()
        }
    }

    private val listenChanges = true
    private fun initUser() {
        ViewDatabaseHandler.getSum {
            DEBUG.appendLog("Load sum? $it ${MyUser.totalViews}")
            if(it){
                initDB = true
                onDataLoaded()
            }
            if (it && listenChanges) {
                ViewDatabaseHandler.listenSumChanges(::onDataLoaded)
            }
        }
        ViewDatabaseHandler.getUser {
            DEBUG.appendLog("Load User? $it ${MyUser.user.views}")
            onDataLoaded()
            initDB = true
            if (it && listenChanges) {
                ViewDatabaseHandler.listenUserChanges(::onDataLoaded)
                return@getUser
            }
            ViewDatabaseHandler.createNewUser { it2 ->
                DEBUG.appendLog("New user created? $it2 ${MyUser.user.views}")
                initDB = it2
                onDataLoaded()
                if (it2 && listenChanges) {
                    ViewDatabaseHandler.listenUserChanges(::onDataLoaded)
                }
            }
        }
    }


    private fun onDataLoaded() {
        //${}
        if (context == null) return
        try {
            binding.tvHeader.text = getString(R.string.txt_my_clicks, MyUser.user.name)
            binding.tvMyCount.text = MyUser.user.views.toString()
            binding.tvSumCount.text = MyUser.totalViews.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}



