package com.withwalk.app.api.model

data class SettingResponse(
    val message: String,
    val result: SettingInfo
)
data class SettingInfo(
    val name: String,
    val dday: Int,
    val img: String
)