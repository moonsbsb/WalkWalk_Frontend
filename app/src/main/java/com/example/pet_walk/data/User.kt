package com.withwalk.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class User(
    @PrimaryKey(autoGenerate = true) var userId: Int = 0,
    var name: String? = null,
    var password: String? = null,
    var email: String? = null
)
