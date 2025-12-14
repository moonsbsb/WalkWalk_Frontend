package com.example.pet_walk.data.remote.api

import com.example.pet_walk.data.remote.model.TodayRequest
import com.example.pet_walk.data.remote.model.TodayResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TodayApi {

    @POST("today/postInfo")
    suspend fun postToday(
        @Header("Authorization") token: String,
        @Body todayRequest: TodayRequest
    ): Response<Unit>

    @GET("today/getInfo")
    suspend fun getToday(
        @Header("Authorization") token: String
    ): Response<TodayResponse>
}