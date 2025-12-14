package com.example.pet_walk.data.remote.api

import com.example.pet_walk.data.remote.model.ChartResponse
import com.example.pet_walk.data.remote.model.RecordRequest
import com.example.pet_walk.data.remote.model.WalkResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ChartApi {
    @GET("/chart/day")
    suspend fun getChart(
        @Header("Authorization") token: String,
        @Query("date")date: String
    ): Response<ChartResponse>
    @POST("/walk/record")
    suspend fun postWalk(
        @Header("Authorization") token: String,
        @Body recordRequest: RecordRequest
    ): Response<WalkResponse>
}