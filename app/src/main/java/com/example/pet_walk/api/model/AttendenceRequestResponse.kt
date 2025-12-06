package com.example.pet_walk.api.model

data class AttendenceResponse(
    val message: String,
    val code: Int,
    val result: Attendence
)
data class Attendence(
    val count: Int,
    val distanceSum: Float,
    val minSum: Int
)