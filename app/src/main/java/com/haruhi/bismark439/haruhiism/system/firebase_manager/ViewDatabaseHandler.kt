package com.haruhi.bismark439.haruhiism.system.firebase_manager

import android.content.Context
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.system.NetworkManager
import com.haruhi.bismark439.haruhiism.system.VoidReturn

object ViewDatabaseHandler {
    const val COLLECTION_SUM = "sums"


    fun getSum(onResult: BaseReturn<Boolean>) {
        FirebaseHandler.get(COLLECTION_SUM, 1) {
            if (it == null) {
                onResult(false)
                return@get
            }
            val sum = it.first().toObject(FBSum::class.java)!!
            MyUser.totalViews = sum.sums
            onResult(true)
        }
    }

    fun incrementView(context: Context, onResult: BaseReturn<Boolean>) {
        MyUser.incrementViews()
        if(!NetworkManager.hasWifi(context)) {
            return
        }
        FirebaseHandler.update(COLLECTION_SUM, COLLECTION_SUM, MyUser.toSumMap()) { it2 ->
            onResult(it2)
        }
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

}