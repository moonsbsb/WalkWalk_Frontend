package com.example.pet_walk.ui.screen.homepage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pet_walk.data.Repository.HomepageRepository
import com.example.pet_walk.data.remote.model.HomeInfo
import com.withwalk.app.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    private val repository: HomepageRepository
): ViewModel(){
    /* 홈페이지 정보 조회 */
    private val dog_ = MutableStateFlow(HomeInfo("", "", R.drawable.dog1.toString(), 1f))
    val dog: MutableStateFlow<HomeInfo> = dog_
    fun getHomePage(token: String){
        viewModelScope.launch {
            try {
                val response = repository.getHome(token)
                if(response.isSuccessful){
                    dog_.value = HomeInfo(response.body()!!.result.name , response.body()!!.result.birth, response.body()!!.result.img, response.body()!!.result.weight)
                    Log.d("홈페이지 정보 조회", "조회 성공: ${response.message()} / ${response.body()!!.result.weight}")
                }else {
                    Log.d("홈페이지 정보 조회", "조회 실패: ${response.code()} - ${response.message()}")}
            }catch (e: Exception){ Log.e("홈페이지 정보 조회", "에러:  ${e.message}") }
        }
    }
}