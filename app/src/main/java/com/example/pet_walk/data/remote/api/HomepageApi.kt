package com.example.pet_walk.data.remote.api

import com.example.pet_walk.data.remote.model.HomeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface HomepageApi {
    /* 홈페이지 */
    @GET("home/info")
    suspend fun getHome(
        @Header("Authorization")token: String
    ): Response<HomeResponse>
}