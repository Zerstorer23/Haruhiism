package com.haruhi.bismark439.haruhiism.system.firebase_manager


import android.util.Log
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.HashMap

typealias BaseReturn<T> = (o: T) -> Unit
typealias SnapshotListener = (snapshot: DataSnapshot) -> Unit

object FirebaseHandler {
    val FIELD_COUNT = "counts"
    private val db : DatabaseReference by lazy {
        Firebase.database.reference
    }

/*
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
*/

/*
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
*/


/*    fun getWhere(boardName:String, where:String, id:String, limit:Long, onResult: BaseReturn<List<DocumentSnapshot>?>) {
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
    }*/

    /*  fun get(boardName:String, limit:Long, onResult: BaseReturn<List<DocumentSnapshot>?>) {
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
  */
    fun getCounts(onResult: BaseReturn<Long>) {
        db.child(FIELD_COUNT).get().addOnSuccessListener {
            onResult(it.value as Long)
        }.addOnFailureListener {
            onResult(0)
        }
    }

    fun incrementCounts() {
        val updates: MutableMap<String, Any> = hashMapOf(
            FIELD_COUNT to ServerValue.increment(1)
        )
        db.updateChildren(updates)
    }

}