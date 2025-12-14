package com.example.pet_walk.data.remote.model

data class SettingResponse(
    val message: String,
    val result: SettingInfo
)
data class SettingInfo(
    val name: String,
    val dday: Int,
    val img: String
)