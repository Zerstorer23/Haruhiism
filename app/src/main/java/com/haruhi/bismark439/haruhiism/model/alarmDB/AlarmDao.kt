package com.haruhi.bismark439.haruhiism.model.alarmDB

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Insert
    suspend fun insert(entity: AlarmData)

    @Update
    suspend fun update(entity: AlarmData)

    @Delete
    suspend fun delete(entity: AlarmData)

    @Query("SELECT * FROM `alarm-table`")
    fun selectAll(): Flow<List<AlarmData>>

    @Query("SELECT * FROM `alarm-table`")
    fun selectAllOnce(): List<AlarmData>
    @Query(
        "SELECT * " +
                "FROM `alarm-table`" +
                "where reqCode=:code "+
                "LIMIT 1"
    )
    fun select(code: Int): Flow<AlarmData?>
    @Query(
        "SELECT * " +
                "FROM `alarm-table`" +
                "where reqCode=:code "+
                "LIMIT 1"
    )
    fun selectOnce(code: Int): AlarmData?

    companion object {
        lateinit var instance: AlarmDao
        /*      fun initDao(application: Application) {
                  instance = (application as MyApp).db.dao()
              }*/

        fun initDao(context: Context) {
            instance = AlarmDatabase.getInstance(context).dao()
        }
    }

}