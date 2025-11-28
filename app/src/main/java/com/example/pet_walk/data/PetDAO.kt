package com.withwalk.app.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface PetDAO {
    @Insert
    fun insertPet(pet: Pet)
}