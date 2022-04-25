package com.haruhi.bismark439.haruhiism.system.firebase_manager

import com.haruhi.bismark439.haruhiism.DEBUG
import com.haruhi.bismark439.haruhiism.system.Helper
import com.haruhi.bismark439.haruhiism.system.Helper.toHash
import com.haruhi.bismark439.haruhiism.system.VoidReturn

object ViewDatabaseHandler {
    const val COLLECTION_VIEWS = "views"
    const val COLLECTION_SUM = "sums"
    const val FIELD_NAME = "name"
    const val FIELD_VIEWS = "views"
    const val FIELD_LASTTIME = "lastUpdate"
    const val FIELD_DOC_ID = "documentId"
    fun createNewUser(onResult: BaseReturn<Boolean>) {
        FirebaseHandler.insert(COLLECTION_VIEWS, MyUser.user.name.toHash(), MyUser.user) {
            onResult(it)
        }
    }

    fun getUser(onResult: BaseReturn<Boolean>) {
        FirebaseHandler.getWhere(COLLECTION_VIEWS, FIELD_NAME, MyUser.user.name, 1) {
            if (it == null || it.isEmpty()) {
                onResult(false)
                return@getWhere
            }
            MyUser.user = it.first().toObject(FBuser::class.java)!!
            //  MyUser.user.documentId = it.first().id// i.id
            onResult(true)
        }
    }

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

    fun incrementView(onResult: BaseReturn<Boolean>) {
        //  getSum {
        //   if (!it) return@getSum
        MyUser.incrementViews()
        FirebaseHandler.update(COLLECTION_SUM, COLLECTION_SUM, MyUser.toSumMap()) { it2 ->
            if (!it2) return@update
        }
        // }
        val map = MyUser.user.toHashMap()
        FirebaseHandler.update(COLLECTION_VIEWS, MyUser.user.name.toHash(), map, onResult)
    }

    fun listenSumChanges(onUpdate: VoidReturn) {
        FirebaseHandler.listenToChanges(COLLECTION_SUM, COLLECTION_SUM) { s ->
            val sum = s.toObject(FBSum::class.java)!!
            if(sum.sums==0L)return@listenToChanges
            MyUser.totalViews = sum.sums
            DEBUG.appendLog("Sum changed ${sum.sums}")
            onUpdate()
        }
    }

    fun listenUserChanges(onUpdate: VoidReturn) {
        FirebaseHandler.listenToChanges(COLLECTION_VIEWS, MyUser.user.name.toHash()) { s ->
            val user = s.toObject(FBuser::class.java)!!
            if (user.name != MyUser.user.name) return@listenToChanges
            if (user.views == 0L) return@listenToChanges
            MyUser.user = user
            DEBUG.appendLog("User changed ${MyUser.user}")
            onUpdate()

        }
    }
}