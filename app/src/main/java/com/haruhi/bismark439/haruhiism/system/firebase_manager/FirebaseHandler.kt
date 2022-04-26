package com.haruhi.bismark439.haruhiism.system.firebase_manager

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

typealias BaseReturn<T> = (o: T) -> Unit
typealias SnapshotListener = (snapshot : DocumentSnapshot) -> Unit

object FirebaseHandler {

    private val db by lazy{
        FirebaseFirestore.getInstance()
    }

    fun listenToChanges(collection:String, doc:String,onChange:SnapshotListener){
        val docRef = db.collection(collection).document(doc)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
               // Log.d(TAG, "Current data: ${snapshot.data}")
                onChange(snapshot)
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    fun insert(boardName:String, key:String, entry: Any, onResult: BaseReturn<Boolean>) {
        db.collection(boardName)
            .document(key)
            .set(entry, SetOptions.merge())
            .addOnSuccessListener {
                onResult(true)
            }.addOnFailureListener {
                it.printStackTrace()
                onResult(false)
            }.addOnCompleteListener {

            }
    }


    fun getWhere(boardName:String, where:String, id:String, limit:Long, onResult: BaseReturn<List<DocumentSnapshot>?>) {
        db.collection(boardName)
            .whereEqualTo(where, id)
            .limit(limit)
            .get()
            .addOnSuccessListener { document ->
                onResult(document.documents)
            }.addOnFailureListener {
                it.printStackTrace()
                onResult(null)
            }
    }

    fun get(boardName:String, limit:Long, onResult: BaseReturn<List<DocumentSnapshot>?>) {
        db.collection(boardName)
            .limit(limit)
            .get()
            .addOnSuccessListener { document ->
                onResult(document.documents)
            }.addOnFailureListener {
                it.printStackTrace()
                onResult(null)
            }
    }

    fun delete(boardName:String, key:String, onResult: BaseReturn<Boolean>) {
        db.collection(boardName)
            .document(key).delete()
            .addOnSuccessListener {
                Log.d(
                    TAG,
                    "DocumentSnapshot successfully deleted!"
                )
                onResult(true)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
                onResult(false)
            }
    }

    fun update(boardName: String, key : String, map:HashMap<String,Any>, onResult: BaseReturn<Boolean>) {
        db.collection(boardName)
            .document(key)
            .update(map)
            .addOnSuccessListener {
                onResult(true)
            }.addOnFailureListener {
                it.printStackTrace()
                onResult(false)
            }
    }

}