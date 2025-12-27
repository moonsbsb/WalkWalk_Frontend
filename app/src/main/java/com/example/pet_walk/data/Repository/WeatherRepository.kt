package com.withwalk.app.data.Repository

import android.util.Log
import com.example.pet_walk.domain.WeatherMessageUseCase
import com.example.pet_walk.data.remote.api.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApi) {
    /* 날씨 초단기실황조회 */
    suspend fun getWeather(
        serviceKey: String,
        pageNo: Int = 1,
        numOfRows: Int = 1000,
        dataType: String = "JSON",
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int
    ): Map<String, String> {
        val response =  api.getWeather(serviceKey, pageNo, numOfRows, dataType, baseDate, baseTime, nx, ny)
        val header = response.response?.header
        val body = response.response?.body

        if(header != null && body != null && header.resultCode == "00") {
            val item = body.items!!.item
            val weatherMap = item.associate {
                it.category!! to (it.obsrValue ?: "")
            }
            return weatherMap
        }else{
            Log.d("날씨 조회", "조회 실패: ${header?.resultCode} - ${header?.resultMsg}")
            Log.d("날씨 조회", "요청 값: ${baseDate} - ${baseTime} - $nx - $ny")
        }

        return mapOf("" to "")
    }
}