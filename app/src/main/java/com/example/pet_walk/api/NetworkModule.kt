package com.withwalk.app.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "https://walkwalk.shop/"
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun getRetrofit(): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit
    }

    @Singleton
    @Provides
    // 로그인, 회원 가입, 정보 수정, 홈, 탈퇴, 로그아웃
    fun provideAuthApi(retrofit: Retrofit): AuthApi{
        return retrofit.create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    // 설정
    fun provideSettingApi(retrofit: Retrofit): SettingApi{
        return retrofit.create(SettingApi::class.java)
    }

    @Singleton
    @Provides
    // 오늘의 화면
    fun provideTodayApi(retrofit: Retrofit): TodayApi{
        return retrofit.create(TodayApi::class.java)
    }

}