package com.withwalk.app.data.Repository

import com.withwalk.app.api.NetworkModule
import com.withwalk.app.api.SettingApi
import com.withwalk.app.api.model.SettingResponse
import retrofit2.Response
import javax.inject.Inject

class SettingRepository @Inject constructor (private val api: SettingApi)  {
    /* 설정 탄생 디데이 조회 */
    suspend fun getDday(token: String): Response<SettingResponse> {
        val response = api.getDday(token)
        return response
    }
}