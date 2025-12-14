package com.example.pet_walk.data.remote.model


data class TodayRequest(
    val meal: Int,
    val water: Int,
    val poo: Int
)
data class TodayResponse(
    val message: String,
    val result: TodayInfo
)
data class TodayInfo(
    val name: String,
    val meal: Int,
    val water: Int,
    val poo: Int,
    val img: String
)
