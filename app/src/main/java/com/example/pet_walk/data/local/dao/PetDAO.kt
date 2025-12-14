package com.example.pet_walk.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.pet_walk.data.local.entity.Pet

@Dao
interface PetDAO {
    @Insert
    fun insertPet(pet: Pet)
}