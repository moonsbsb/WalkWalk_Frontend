package com.example.pet_walk.data.remote.api

import com.example.pet_walk.data.remote.model.AuthRequest
import com.example.pet_walk.data.remote.model.AuthResponse
import com.example.pet_walk.data.remote.model.CodeRequest
import com.example.pet_walk.data.remote.model.HomeResponse
import com.example.pet_walk.data.remote.model.LoginRequest
import com.example.pet_walk.data.remote.model.LoginResponse
import com.example.pet_walk.data.remote.model.UnsubscribeResponse
import com.example.pet_walk.data.remote.model.UpdateUserRequest
import com.example.pet_walk.data.remote.model.UpdateUserResponse
import com.example.pet_walk.data.remote.model.UserInfoResponse
import com.example.pet_walk.data.remote.model.VerifyCodeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApi {
    /* 회원가입 */
    @POST("auth/signup")
    suspend fun postSignup(
        @Body request: AuthRequest
    ): Response<AuthResponse>

    /* 로그인 */
    @POST("auth/login")
    suspend fun postLogin(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    /* 홈페이지 */
    @GET("home/info")
    suspend fun getHome(
        @Header("Authorization")token: String
    ): Response<HomeResponse>

    /* 개인정보 수정 */
    @PATCH("user/update")
    suspend fun postUpdateUser(
        @Header("Authorization")token: String,
        @Body request: UpdateUserRequest
    ): Response<UpdateUserResponse>

    /* 개인정보 조회 */
    @GET("user/user")
    suspend fun getUser(
        @Header("Authorization")token: String
    ): Response<UserInfoResponse>

    /* 탈퇴 인증코드 전송 */
    @GET("user/code")
    suspend fun verifyCode(
        @Header("Authorization") token: String
    ): Response<VerifyCodeResponse>

    /* 탈퇴하기 */
    @POST("user/quit")
    suspend fun getVerificationCode(
        @Header("Authorization") token: String,
        @Body request: CodeRequest
    ): Response<UnsubscribeResponse>

    /* 로그아웃 */
    @POST("auth/logout")
    suspend fun logoutUser(
        @Header("Authorization")token: String
    ): Response<Unit>

    /* 카카오 로그인 */
    @GET("auth/kakao")
    suspend fun kakaoLogin(
        @Header("Authorization")token: String
    ): Response<LoginResponse>
}