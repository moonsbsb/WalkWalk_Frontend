package com.withwalk.app.data.Repository

import com.example.pet_walk.data.remote.api.WeatherApi
import com.example.pet_walk.data.remote.model.WeatherResponse
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
    ): WeatherResponse {
        return api.getWeather(serviceKey, pageNo, numOfRows, dataType, baseDate, baseTime, nx, ny)
    }
}