package com.withwalk.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecordTable")
data class WRecord(
    @PrimaryKey(autoGenerate = true) var recordId: Int = 0,
    var location: String? = null,
    var date: String? = null,
    var km: Int? = null,
    var hour: Int? = null,
    var mapImg: Int? = null
)
