package com.withwalk.app.data.Repository

import com.withwalk.app.api.AuthApi
import com.withwalk.app.api.NetworkModule
import com.withwalk.app.api.model.AuthRequest
import com.withwalk.app.api.model.AuthResponse
import com.withwalk.app.api.model.CodeRequest
import com.withwalk.app.api.model.HomeResponse
import com.withwalk.app.api.model.LoginRequest
import com.withwalk.app.api.model.LoginResponse
import com.withwalk.app.api.model.UnsubscribeResponse
import com.withwalk.app.api.model.UpdateUserRequest
import com.withwalk.app.api.model.UpdateUserResponse
import com.withwalk.app.api.model.UserInfoResponse
import com.withwalk.app.api.model.VerifyCodeResponse
import retrofit2.Response


class AuthRepository (private val api: AuthApi = NetworkModule.getRetrofit().create(AuthApi::class.java)) {
    /* 회원가입 */
    suspend fun postAuth(authRequest: AuthRequest): Response<AuthResponse>{
        val response = api.postSignup(authRequest)
        return response
    }
    /* 로그인 */
    suspend fun postLogin(loginRequest: LoginRequest): Response<LoginResponse>{
        val response = api.postLogin(loginRequest)
        return response
    }
    /* 홈 정보 */
    suspend fun getHome(token: String): Response<HomeResponse>{
        val response = api.getHome(token)
        return response
    }
    /* 개인정보 수정 */
    suspend fun postUpdateUser(token: String, request: UpdateUserRequest): Response<UpdateUserResponse>{
        val response = api.postUpdateUser(token, request)
        return response
    }
    /* 개인정보 조회 */
    suspend fun getUser(token: String): Response<UserInfoResponse>{
        val response = api.getUser(token)
        return response
    }
    /* 탈퇴 인증코드 요청 */
    suspend fun verifyCode(token: String): Response<VerifyCodeResponse>{
        return api.verifyCode(token)
    }
    /* 탈퇴 인증코드 전송 */
    suspend fun getVerificationCode(token: String, codeRequest: CodeRequest): Response<UnsubscribeResponse>{
        return api.getVerificationCode(token, codeRequest)
    }
    /* 로그아웃 */
    suspend fun logoutUser(token: String): Response<Unit>{
        return api.logoutUser(token)
    }
    /* 카카오 로그인 */
    suspend fun kakaoLogin(token: String): Response<LoginResponse>{
        return api.kakaoLogin(token)
    }
}