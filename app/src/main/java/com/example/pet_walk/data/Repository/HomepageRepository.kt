package com.example.pet_walk.data.Repository

import com.example.pet_walk.data.remote.api.HomepageApi
import com.example.pet_walk.data.remote.model.HomeResponse
import retrofit2.Response
import javax.inject.Inject

class HomepageRepository @Inject constructor(private val api: HomepageApi) {
    /* 홈 정보 */
    suspend fun getHome(token: String): Response<HomeResponse> {
        val response = api.getHome(token)
        return response
    }
}