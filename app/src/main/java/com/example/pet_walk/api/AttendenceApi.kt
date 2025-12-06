package com.example.pet_walk.api

import com.example.pet_walk.api.model.AttendenceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AttendenceApi {
    /* 출석 확인 */
    @GET("attendence/check")
    suspend fun getAttendence(
        @Header("Authorization") token: String,
        @Query("month") month: String
    ): Response<AttendenceResponse>
}