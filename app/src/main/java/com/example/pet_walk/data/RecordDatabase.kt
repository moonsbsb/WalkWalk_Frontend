package com.withwalk.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WRecord::class, User::class], version = 1, exportSchema = false)
abstract class RecordDatabase: RoomDatabase() {

    abstract fun recordDao() : RecordDAO
    abstract fun userDao() : UserDAO

    companion object{
        @Volatile
        private var INSTANCE: RecordDatabase? = null

        fun getInstance(context: Context) : RecordDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext, RecordDatabase::class.java, "record-database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}