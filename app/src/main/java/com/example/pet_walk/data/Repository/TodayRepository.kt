package com.withwalk.app.data.Repository

import com.withwalk.app.api.NetworkModule
import com.withwalk.app.api.TodayApi
import com.withwalk.app.api.model.TodayRequest
import com.withwalk.app.api.model.TodayResponse
import retrofit2.Response
import javax.inject.Inject

class TodayRepository @Inject constructor(private val api: TodayApi) {
    /* 오늘의 정보 보내기 */
    suspend fun postToday(token: String, todayRequest: TodayRequest): Response<Unit> {
        return api.postToday(token, todayRequest)
    }
    /* 오늘의 정보 받기 */
    suspend fun getToday(token: String): Response<TodayResponse>{
        val response = api.getToday(token)
        return response
    }
}