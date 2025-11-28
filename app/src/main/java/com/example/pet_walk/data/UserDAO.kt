package com.withwalk.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDAO {

    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM UserTable")
    fun getUsers() : List<User>

    @Query("SELECT * FROM UserTable WHERE email=:email and password=:password")
    suspend fun getUser(email: String, password: String) : User?
}