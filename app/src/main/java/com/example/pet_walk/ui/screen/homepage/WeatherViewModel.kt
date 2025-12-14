package com.withwalk.app.ui.screen.homepage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withwalk.app.R
import com.withwalk.app.data.Repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {
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
                val header = response.response?.header
                val body = response.response?.body

                if(header!=null && body!= null && header?.resultCode == "00"){
                    Log.d("날씨 조회", "성공: ${response.response?.header?.resultMsg}")
                    val item = body.items!!.item
                    val weatherMap = item.associate {
                        it.category!! to (it.obsrValue ?: "")
                    }
                    _weather.value = weatherMap
                }else{
                    Log.d("날씨 조회", "조회 실패: ${header?.resultCode} - ${header?.resultMsg}")
                    Log.d("날씨 조회", "요청 값: ${baseDate} - ${baseTime} - $nx - $ny")
                }
            }catch (e: Exception){
                Log.d("날씨 조회", "에러: ${e.message}")
            }
        }
    }

    /* 날씨 알림 메시지 */
    private val _weatherMsg = MutableStateFlow(Pair("", R.drawable.transparent))
    var weatherMsg: StateFlow<Pair<String, Int>> = _weatherMsg
    data class TempRange(val min: Int, val max: Int)

    private val DOG_WARNING_SIGN: Map<String, List<List<TempRange>>> = mapOf(
        "small" to listOf(
            listOf(TempRange(12, 18)),
            listOf(TempRange(7, 11), TempRange(19, 21)),
            listOf(TempRange(-1, 6), TempRange(22, 24)),
            listOf(TempRange(-4, -2), TempRange(25, 29)),
            emptyList()
        ),
        "middle" to listOf(
            listOf(TempRange(10, 18)),
            listOf(TempRange(7, 9), TempRange(19, 21)),
            listOf(TempRange(-1, 6), TempRange(22, 24)),
            listOf(TempRange(-9, -2), TempRange(25, 29)),
            emptyList()
        ),
        "big" to listOf(
            listOf(TempRange(7, 16)),
            listOf(TempRange(4, 6), TempRange(17, 18)),
            listOf(TempRange(-6, 3), TempRange(19, 23)),
            listOf(TempRange(-9, -5), TempRange(24, 24)),
            emptyList()
        )
    )

    private val PERFECT = Pair("산책하기 최적의 날씨!", R.drawable.warning_light_1_2)
    private val GOOD = Pair("산책하기 좋은 날씨네요!", R.drawable.warning_light_1_2)
    private val SOSO = Pair("조금 위험할 수 있어요\n" + "반려견을 잘 봐주세요", R.drawable.warning_light_3)
    private val CAREFUL = Pair("날씨가 추워요\n" + "야외 산책에 조심해주세요", R.drawable.warning_light_4)
    private val DANGER = Pair("날씨가 정말 추워요\n" + "오랜 야외 산책을 자제해주세요", R.drawable.warning_light_5)


    fun instructionBasedonWeight(weight: Float, temperature: String): Pair<String, Int>{
        val size = dogCategory(weight)
        val pair = temperatureRisk(size, temperature)
        _weatherMsg.value = pair
        return temperatureRisk(size, temperature)
    }

    private val RISK_LEVELS = listOf(
        PERFECT,
        GOOD,
        SOSO,
        CAREFUL,
        DANGER
    )
    private fun temperatureRisk(size: String, temperature: String): Pair<String, Int>{
        val temp = temperature.toFloat().toInt()
        val warning = DOG_WARNING_SIGN[size]

        for((warningIdx, ranges) in warning!!.withIndex()){
            if(warningIdx == RISK_LEVELS.lastIndex) return RISK_LEVELS[warningIdx]
            for(range in ranges){
                if(temp >= range.min && temp <= range.max){
                    return RISK_LEVELS[warningIdx]
                }
            }
        }

        return DANGER
    }
    /* 강아지 크기 구분 */
    private fun dogCategory(weight: Float): String{
        return when{
            weight < 10 ->"small"
            weight < 26 -> "middle"
            else -> "big"
        }
    }


    /* 강수량 */
    fun CheckHumidity(){

    }
}
