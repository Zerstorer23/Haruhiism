/*
package com.haruhi.bismark439.haruhiism.system.firebase_manager

import android.content.Context
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.system.NetworkManager
import com.haruhi.bismark439.haruhiism.system.VoidReturn

object ViewDatabaseHandler {
    fun getSum(onResult: BaseReturn<Boolean>) {
        FirebaseHandler.getCounts() {
            MyUser.totalViews = it.toLong();
            onResult(true)
        }
    }

    fun incrementView(context: Context, onResult: BaseReturn<Boolean>) {
        MyUser.incrementViews()
        if(!NetworkManager.hasWifi(context)) {
            return
        }
        FirebaseHandler.incrementCounts()
        onResult(true)
    }

    fun listenSumChanges(onUpdate: VoidReturn) {
        FirebaseHandler.listenToChanges(COLLECTION_SUM, COLLECTION_SUM) { s ->
            val sum = s.toObject(FBSum::class.java)!!
            if(sum.sums==0L)return@listenToChanges
            MyUser.totalViews = sum.sums
            Debugger.log("Sum changed ${sum.sums}")
            onUpdate()
        }
    }

}*/
