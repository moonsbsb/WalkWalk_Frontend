package com.withwalk.app.ui.screen.chart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withwalk.app.api.model.ChartResult
import com.withwalk.app.api.model.RecordRequest
import com.withwalk.app.data.Repository.ChartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChartViewModel(private val repository: ChartRepository): ViewModel() {
    /* 날짜 별 차트 정보 조회 */
    val _dayChart = MutableStateFlow<ChartResult>(ChartResult(0,0,0,0,0f,"", 0,0,0) )
    var dayChart: StateFlow<ChartResult> = _dayChart
    fun getChartByDate(token: String, date: String){
        Log.d("날짜 별 차트 정보 조회", "요청 날짜: ${date}")
        viewModelScope.launch {
            try {
                val response = repository.getChartByDate(token, date)
                val result = response.body()?.result
                if(response.isSuccessful && result!=null){
                    _dayChart.value = result
                    Log.d("날짜 별 차트 정보 조회", "요청 성공: ${response.body()?.message} 물: ${result.water} 칼로리: ${result.kcal} 스텝퍼센트: ${result.stepPercent} 걷기퍼센트: ${result.nomalPercent}")
                }else{ Log.d("날짜 별 차트 정보 조회", "요청 실패: ${response.code()} - ${response.message()}") }
            }catch (e: Exception) { Log.e("날짜 별 차트 정보 조회", "에러: ${e.message}")}
        }
    }
    /* 산책 정보 등록 */
    private val _postWalkResult = MutableStateFlow<Boolean?>(null)
    val postWalkResult: StateFlow<Boolean?> = _postWalkResult
    fun postWalk(token: String, result: RecordRequest){
        viewModelScope.launch {
            try {
                val response = repository.postWalk(token, result)
                Log.d("산책 정보 등록", "요청내용: 날짜: ${result.date} 걸음 수: ${result.stepCount} 거리: ${result.distance}")
                if(response.isSuccessful){
                    _postWalkResult.value = true
                    Log.d("산책 정보 등록", "요청 성공: ${response.body()?.message}")
                }else{
                    _postWalkResult.value = false
                    Log.d("산책 정보 등록", "요청 실패: ${response.code()} - ${response.message()}")
                }
            }catch (e: Exception) {
                _postWalkResult.value = false
                Log.e("산책 정보 등록", "에러: ${e.message}")
            }
        }
    }
}