package com.withwalk.app.ui.screen.homepage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pet_walk.domain.WeatherMessageUseCase
import com.withwalk.app.R
import com.withwalk.app.data.Repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository, private val service: WeatherMessageUseCase): ViewModel() {
    private val _weather = MutableStateFlow<Map<String, String>>(emptyMap())
    var weather: StateFlow<Map<String, String>> = _weather
    fun getWeather(
        serviceKey: String,
        pageNo: Int = 1,
        numOfRows: Int = 1000,
        dataType: String = "JSON",
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int
    ){
        viewModelScope.launch {
            try {
                val response = repository.getWeather(
                    serviceKey,
                    pageNo,
                    numOfRows,
                    dataType,
                    baseDate,
                    baseTime,
                    nx,
                    ny
                )
                _weather.value = response
            }catch (e: Exception){
                Log.d("날씨 조회", "에러: ${e.message}")
            }
        }
    }

    /* 날씨 알림 메시지 */
    private val _weatherMsg = MutableStateFlow(Pair("", R.drawable.transparent))
    var weatherMsg: StateFlow<Pair<String, Int>> = _weatherMsg

    fun instructionBasedonWeight(weight: Float, temperature: String){
        viewModelScope.launch {
            val pair = service.instructionBasedonWeight(weight, temperature)
            _weatherMsg.value = pair
        }
    }
}
