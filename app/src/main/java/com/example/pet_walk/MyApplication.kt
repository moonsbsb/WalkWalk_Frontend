package com.withwalk.app

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        val appKey = BuildConfig.KAKAO_SDK_APP_KEY
        KakaoMapSdk.init(this, appKey)
        KakaoSdk.init(this, appKey)
    }
}