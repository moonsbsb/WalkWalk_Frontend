package com.example.pet_walk.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pet_walk.data.local.entity.WRecord

@Dao
interface RecordDAO {

    @Insert
    suspend fun insertWRecord(record: WRecord)

    @Delete
    suspend fun deleteWRecord(record: WRecord)

    @Update
    suspend fun updateWRecord(record: WRecord)

    @Query("SELECT * FROM RecordTable WHERE recordId = :id")
    suspend fun getWRecord(id: Int) : WRecord

    @Query("SELECT * FROM RecordTable")
    fun getAllRecords() :  LiveData<List<WRecord>>
}