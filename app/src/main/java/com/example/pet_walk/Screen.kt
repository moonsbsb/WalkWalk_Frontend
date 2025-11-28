package com.withwalk.app

sealed class Screen(val route: String, val label: String){
    object Login: Screen("auth/login", "로그인")
    object Signup: Screen("auth/signup", "회원가입")
    object Regist: Screen("auth/regist", "강아지 등록")
    object Complete: Screen("auth/complete", "가입 완료")
    object Guide: Screen("auth/guide", "정보수집동의안내서")
    object FindPassword: Screen("auth/find_password", "비밀번호 찾기")
    object CheckNumber: Screen("auth/check_number", "비밀번호 인증")
    object PasswordReset: Screen("auth/password_reset", "비밀번호 재설정")
    object ResetComplete: Screen("auth/reset_complete", "비밀번호 재설정 완료")
    object DeletetComplete: Screen("auth/delete_complete", "탈퇴 완료")


    object Home: Screen("main/home", "홈")
    object Today: Screen("main/today", "일지")
    object Gallery: Screen("main/gallery", "갤러리")
    object Chart: Screen("main/chart", "차트")
    object Setting: Screen("main/setting", "설정")
    object Walk: Screen("main/walk", "산책")
    object AccountUpdate: Screen("main/setting/update", "개인정보 수정")
    object Unsubscribe : Screen("main/setting/update/unsubscribe", "탈퇴하기")
    object Attendance : Screen("main/attendance", "출석")
}