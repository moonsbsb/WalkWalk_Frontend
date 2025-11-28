package com.withwalk.app.api

import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
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
import retrofit2.http.Body
import retrofit2.http.DELETE
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