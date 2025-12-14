package com.example.pet_walk.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pet_walk.data.local.dao.RecordDAO
import com.example.pet_walk.data.local.dao.UserDAO
import com.example.pet_walk.data.local.entity.User
import com.example.pet_walk.data.local.entity.WRecord

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