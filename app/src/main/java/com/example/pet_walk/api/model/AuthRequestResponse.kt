package com.withwalk.app.api.model

data class AuthRequest(
    val email: String,
    val password: String,
    val name: String,
    val dogs: List<DogRequest>
)
data class DogRequest(
    val name: String,
    val age: Int,
    val weight: Float,
    val birth: String,
    val img: String
)
data class AuthResponse(
    val message: String
)
data class LoginResponse(
    val message: String,
    val token: String
)
data class LoginRequest(
    val email: String,
    val password: String
)

data class HomeResponse(
    val message: String,
    val result: HomeInfo
)
data class HomeInfo(
    val name: String,
    val birth: String,
    val img: String
)
/* 정보 수정 */
data class UpdateUserRequest(
    val dog: DogRequest
)
data class UpdateUserResponse(
    val message: String,
)
/* 유저 정보 */
data class UserInfoResponse(
    val message: String,
    val user: DogRequest
)
/* 탈퇴하기 */
data class UnsubscribeResponse(
    var code: Boolean,
    var message: String
)
/* 탈퇴 인증코드 요청 */
data class VerifyCodeResponse(
    val code: Boolean,
    val message: String
)
/* 탈퇴 인증 코드 전송 */
data class CodeRequest(
    val code: String
)
/* 카카오 로그인 */
data class KakaoResponse(
    val id: Long,
    val connected_at: String?,
    val kakao_account: KakaoAccount?
)
data class KakaoAccount(
    val email: String?,
    val name: String?,
    val profile: KakaoProfile?
)
data class KakaoProfile(
    val nickname: String?,
    val profile_image_url: String?,
    val thumbnail_image_url: String?
)