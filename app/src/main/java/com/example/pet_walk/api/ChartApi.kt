package com.withwalk.app.api

import com.withwalk.app.api.model.ChartResponse
import com.withwalk.app.api.model.RecordRequest
import com.withwalk.app.api.model.WalkResponse
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