package com.withwalk.app.ui.screen.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import com.withwalk.app.R
import com.withwalk.app.api.model.AuthRequest
import com.withwalk.app.api.model.CodeRequest
import com.withwalk.app.api.model.DogRequest
import com.withwalk.app.api.model.HomeInfo
import com.withwalk.app.api.model.LoginRequest
import com.withwalk.app.api.model.UnsubscribeResponse
import com.withwalk.app.api.model.UpdateUserRequest
import com.withwalk.app.data.Repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class AuthViewModel(private val repository: AuthRepository): ViewModel() {

    /* 회원가입 */
    val _message = MutableStateFlow<String>("")
    var message: StateFlow<String> = _message
    fun postAuth(authRequest: AuthRequest){
        viewModelScope.launch {
            try{
                val response = repository.postAuth(authRequest)

                if(response.isSuccessful) {
                    _message.value = response.body()!!.message
                    Log.d("회원가입", "성공: ${response.body()!!.message}")
                }
                else{
                    Log.d("회원가입", "응답실패: ${response.code()} - ${response.message()}")
                    _message.value = response.errorBody().toString()
                }
            } catch (e:Exception) { Log.e("회원가입", "에러: ${e.message}") }

        }
    }

    /* 로그인 */
    val token_ = MutableStateFlow<String>("")
    val token: MutableStateFlow<String> = token_
    val errorMsg_ = MutableStateFlow<String>("")
    val errorMsg: MutableStateFlow<String> = errorMsg_
    fun postLogin(loginRequest: LoginRequest){
        viewModelScope.launch {
            try{
                val response = repository.postLogin(loginRequest)
                val body = response.body()
                if(response.isSuccessful && body != null){
                    token_.value = body.token
                    Log.d("로그인", "로그인 성공: ${body.message}")
                }else {Log.d("로그인", "로그인 실패: ${response.code()} - ${response.message()}")}
                Log.d("로그인", "로그인 메세지 확인: ${response.body()!!.message}")
            }catch (e:Exception){Log.e("로그인", "에러: ${e}")}
        }
    }

    /* 홈페이지 정보 조회 */
    val dog_ = MutableStateFlow(HomeInfo("", "", R.drawable.dog1.toString()))
    val dog: MutableStateFlow<HomeInfo> = dog_
    fun getHomePage(token: String){
        viewModelScope.launch {
            try {
                val response = repository.getHome(token)
                if(response.isSuccessful){
                    dog_.value = HomeInfo(response.body()!!.result.name , response.body()!!.result.birth, response.body()!!.result.img)
                    Log.d("홈페이지 정보 조회", "조회 성공: ${response.message()} / ${response.body()!!.result.img}")
                }else {Log.d("홈페이지 정보 조회", "조회 실패: ${response.code()} - ${response.message()}")}
            }catch (e: Exception){ Log.e("홈페이지 정보 조회", "에러:  ${e.message}") }
        }
    }

    /* 유저 개인정보 수정 */
    fun postUserUpdate(token: String, request: UpdateUserRequest){
        viewModelScope.launch {
            Log.d("유저 정보 업데이트 요청", "요청내용: name=${request.dog.name} img=${request.dog.img}")
            try {
                val response = repository.postUpdateUser(token, request)
                if(response.isSuccessful){
                    Log.d("유저 정보 업데이트 요청", "요청 성공: ${response.body()!!.message}")
                }else{ Log.d("유저 정보 업데이트 요청", "요청 실패: ${response.code()} - ${response.message()}") }
            }catch (e: Exception) { Log.e("유저 정보 업데이트 요청", "에러: ${e.message}")}
        }
    }
    /* 유저 개인정보 조회 */
    val _user = MutableStateFlow<DogRequest>(DogRequest("", 0, 0f, "", ""))
    var user: StateFlow<DogRequest> = _user
    fun getUser(token: String){
        viewModelScope.launch {
            try {
                val response = repository.getUser(token)
                if(response.isSuccessful){
                    _user.value = response.body()!!.user
                    Log.d("유저 정보 조회", "요청 성공: ${response.body()!!.message}")
                }else{ Log.d("유저 정보 조회", "요청 실패: ${response.code()} - ${response.message()}") }
            }catch (e: Exception) { Log.e("유저 정보 조회", "에러: ${e.message}")}
        }
    }
    /* 탈퇴 인증코드 전송 */
    fun verifyCode(token: String){
        viewModelScope.launch {
            try {
                val response = repository.verifyCode(token)
                Log.d("탈퇴 인증코드 전송", "코드 확인: ${response.body()!!.message}")
                if(response.isSuccessful){
                    Log.d("탈퇴 인증코드 전송", "요청 성공: ${response.body()!!.message}")
                }else{ Log.d("탈퇴 인증코드 전송", "요청 실패: ${response.code()} - ${response.message()}") }
            }catch (e: Exception) { Log.e("탈퇴 인증코드 전송", "에러: ${e.message}")}
        }
    }

    /* 탈퇴 인증코드 확인 */
    private val _quite = MutableStateFlow<UnsubscribeResponse>(UnsubscribeResponse(false, ""))
    var quite: StateFlow<UnsubscribeResponse> = _quite
    fun getVerificationCode(token: String, code: CodeRequest){
        Log.d("탈퇴 인증코드 확인", "인증코드 확인: ${code.code}")
        viewModelScope.launch {
            try {
                val response = repository.getVerificationCode(token, code)
                _quite.value = UnsubscribeResponse(
                    code = response.body()?.code ?: false,
                    message = response.body()?.message ?: ""
                )
                if(response.isSuccessful){
                    Log.d("탈퇴 인증코드 확인", "요청 성공: ${response.body()!!.message}")
                }else{ Log.d("탈퇴 인증코드 확인", "요청 실패: ${response.code()} - ${response.message()}") }
            }catch (e: Exception) { Log.e("탈퇴 인증코드 확인", "에러: ${e.message}")}
        }
    }

    /* 로그아웃 */
    fun logoutUser(token: String){
        viewModelScope.launch {
            try {
                Log.d("로그아웃", "토큰확인: ${token}")
                val response = repository.logoutUser(token)
                if(response.isSuccessful){
                    Log.d("로그아웃", "요청 성공: ${response.body()!!}")
                }else{ Log.d("로그아웃", "요청 실패: ${response.code()} - ${response.message()}") }
            }catch (e: Exception) { Log.e("로그아웃", "에러: ${e.message}")}
        }
    }

    /* 카카로 로그인 */
    val _kakaoMessage = MutableStateFlow<String>("")
    val kakaoMessage: MutableStateFlow<String> = _kakaoMessage
    fun kakaoLogin(token: String){
        viewModelScope.launch {
            try {
                val response = repository.kakaoLogin(token)
                if(response.isSuccessful){
                    token_.value = response.body()!!.token
                    _kakaoMessage.value = response.body()!!.message

                    Log.d("카카오 로그인", "요청 성공: ${response.body()!!.message}")
                }else{ Log.d("카카오 로그인", "요청 실패: ${response.code()} - ${response.message()}") }
            }catch (e: Exception) { Log.e("카카오 로그인", "에러: ${e.message}")}

        }
    }
}