package com.withwalk.app.ui.screen.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withwalk.app.api.model.SettingInfo
import com.withwalk.app.data.Repository.SettingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingViewModel(private val repository: SettingRepository) : ViewModel() {
    private val _dDay = MutableStateFlow<SettingInfo>(SettingInfo("", 1, "dog1"))
    var dDay: StateFlow<SettingInfo> = _dDay
    /* 탄생 디데이 조회 */
    fun getDday(token: String){
        viewModelScope.launch {
            try {
                val response = repository.getDday(token)
                if(response.isSuccessful){
                    _dDay.value = SettingInfo(response.body()!!.result.name, response.body()!!.result.dday, response.body()!!.result.img)
                    Log.d("탄생 디데이 조회", "조회 성공:  ${response.body()!!.message} - ${response.body()!!.result.img}")
                }else{
                    Log.d("탄생 디데이 조회", "조회 실패: ${response.code()} - ${response.message()}")}
            } catch (e: Exception){
                Log.d("탄생 디데이 조회", "에러: ${e.message}")}
        }
    }
}