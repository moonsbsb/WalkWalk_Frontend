package com.withwalk.app.data.Repository

import com.example.pet_walk.data.remote.api.SettingApi
import com.example.pet_walk.data.remote.model.SettingResponse
import retrofit2.Response
import javax.inject.Inject

class SettingRepository @Inject constructor (private val api: SettingApi)  {
    /* 설정 탄생 디데이 조회 */
    suspend fun getDday(token: String): Response<SettingResponse> {
        val response = api.getDday(token)
        return response
    }
}