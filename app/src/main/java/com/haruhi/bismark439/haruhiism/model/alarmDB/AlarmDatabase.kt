package com.haruhi.bismark439.haruhiism.model.alarmDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AlarmData::class], version = 3)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun dao(): AlarmDao

    companion object {
        private const val DB_NAME = "alert_database"

        @Volatile
        private var INSTANCE: AlarmDatabase? = null

        fun getInstance(context: Context): AlarmDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AlarmDatabase::class.java,
                        DB_NAME
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return INSTANCE as AlarmDatabase
            }
        }
    }

}