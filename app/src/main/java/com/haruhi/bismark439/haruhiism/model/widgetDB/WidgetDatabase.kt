package com.haruhi.bismark439.haruhiism.model.widgetDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WidgetData::class], version = 3)
abstract class WidgetDatabase : RoomDatabase() {
    abstract fun dao(): WidgetDao

    companion object {
        private const val DB_NAME = "widget_database"
        @Volatile
        private var INSTANCE: WidgetDatabase? = null

        fun getInstance(context: Context): WidgetDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WidgetDatabase::class.java,
                        DB_NAME
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return INSTANCE as WidgetDatabase
            }
        }
    }

}