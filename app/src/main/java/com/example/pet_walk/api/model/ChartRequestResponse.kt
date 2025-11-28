package com.withwalk.app.api.model

data class ChartResponse(
    val message: String,
    val result: ChartResult
)
data class ChartResult(
    var meal: Int = 0,
    var water: Int = 0,
    var poo: Int = 0,
    val stepCount: Int? = 0,
    val distance: Float? = 0f,
    val time: String? = "",
    val slowPercent: Int? = 0,
    val nomalPercent: Int? = 0,
    val kcal: Int? = 0,
    val stepPercent: Int? = 0,
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