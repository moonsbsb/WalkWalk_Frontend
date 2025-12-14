package com.withwalk.app.api

import com.withwalk.app.WeatherRetrofit
import com.withwalk.app.api.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val WEATHER_URL = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"
@Module
@InstallIn(SingletonComponent::class)
object WeatherModule{
    @WeatherRetrofit
    @Singleton
    @Provides
    fun getWeatherRetrofit(): Retrofit{
        val retrofit = Retrofit.Builder()
            .baseUrl(WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit
    }
    @Singleton
    @Provides
    // 날씨 api요청
    fun provideWeatherApi(@WeatherRetrofit retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }
}