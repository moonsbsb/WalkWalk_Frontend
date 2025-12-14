package com.withwalk.app.data.Repository

import com.withwalk.app.api.ChartApi
import com.withwalk.app.api.NetworkModule
import com.withwalk.app.api.model.ChartResponse
import com.withwalk.app.api.model.RecordRequest
import com.withwalk.app.api.model.WalkResponse
import retrofit2.Response
import javax.inject.Inject

class ChartRepository @Inject constructor (private val api: ChartApi) {
    /* 날짜 별 차트 정보 조회 */
    suspend fun getChartByDate(token: String, date: String): Response<ChartResponse>{
        val repository = api.getChart(token, date)
        return repository
    }
    /* 산책 정보 등록 */
    suspend fun postWalk(token: String, request: RecordRequest): Response<WalkResponse>{
        val repository = api.postWalk(token, request)
        return repository
    }
}