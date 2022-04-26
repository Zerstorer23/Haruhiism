package com.haruhi.bismark439.haruhiism.model.widgetDB

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WidgetDao {

    @Insert
    suspend fun insert(entity: WidgetData)

    @Update
    suspend fun update(entity: WidgetData)

    @Delete
    suspend fun delete(entity: WidgetData)

    @Query("SELECT * FROM `widget-table`")
    fun selectAll(): Flow<List<WidgetData>>
    @Query("SELECT * FROM `widget-table`")
    fun selectAllOnce(): List<WidgetData>


    @Query(
        "SELECT * " +
                "FROM `widget-table`" +
                "where appWidgetId=:code"
    )
    fun select(code: Int): Flow<WidgetData>

    companion object {
        lateinit var instance: WidgetDao
        fun initDao(context: Context) {
            instance = WidgetDatabase.getInstance(context).dao()
        }
    }

}