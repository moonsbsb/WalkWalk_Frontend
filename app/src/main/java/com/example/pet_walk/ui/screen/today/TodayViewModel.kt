package com.withwalk.app.ui.screen.today

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withwalk.app.api.model.TodayInfo
import com.withwalk.app.api.model.TodayRequest
import com.withwalk.app.data.Repository.TodayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class TodayViewModel(val repository: TodayRepository): ViewModel() {

    /* 오늘의 정보 전송 */
    fun postToday(token: String, todayRequest: TodayRequest){
        viewModelScope.launch {
            try {
                val response = repository.postToday(token, todayRequest)
                if(response.isSuccessful)Log.d("오늘의 정보 전송", "오늘의 정보 전송 성공: ${response}")
                else {Log.d("오늘의 정보 전송", "오늘의 정보 전송 실패: $response")}
            }catch (e: Exception){Log.e("오늘의 정보 전송", "에러: ${e.message}")}
        }
    }
    /* 오늘의 정보 조회 */
    private val todayData_ = MutableStateFlow(TodayInfo("", 0, 0, 0, "dog1"))
    var todayData: MutableStateFlow<TodayInfo> = todayData_
    fun getToday(token: String){
        viewModelScope.launch {
            try {
                val response = repository.getToday(token)
                if(response.isSuccessful){
                    todayData_.value = response.body()?.result ?: TodayInfo("", 0, 0, 0, "dog1")
                    Log.d("오늘의 정보 조회", "조회 성공: ${response.body()?.message} / ${response.body()?.result?.img}")
                }else {Log.d("오늘의 정보 조회", "조회 실패: ${response.code()} - ${response.message()}")}
            }catch (e: Exception) { Log.e("오늘의 정보 조회", "에러: ${e.message}") }
        }
    }
}