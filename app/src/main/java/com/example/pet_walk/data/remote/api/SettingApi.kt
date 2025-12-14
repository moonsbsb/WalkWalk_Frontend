package com.example.pet_walk.data.remote.api

import com.example.pet_walk.data.remote.model.SettingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface SettingApi {
    @GET("today/dday")
    suspend fun getDday(
        @Header("Authorization") token: String
    ): Response<SettingResponse>


}