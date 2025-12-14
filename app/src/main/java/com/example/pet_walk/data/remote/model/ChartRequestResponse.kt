package com.example.pet_walk.data.remote.model

data class ChartResponse(
    val message: String,
    val result: ChartResult
)
data class ChartResult(
    val stepCount: Int? = 0,
    val slowStepTime: Int? = 0,
    val nomalStepTime: Int? = 0,
    val distance: Float? = 0f,
    val time: String? = "",
    val slowPercent: Int? = 0,
    val nomalPercent: Int? = 0,
    val kcal: Int? = 0,
    val stepPercent: Int? = 0
)

data class RecordRequest(
    val stepCount: Int,
    val distance: Float,
    val time: String,
    val slowStepTime: Int,
    val nomalStepTime: Int,
    val date: String
)
data class WalkResponse(
    val message: String,
)