package com.withwalk.app.ui.screen.login

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.withwalk.app.ui.theme.main
import com.withwalk.app.ui.theme.white
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.withwalk.app.ui.theme.error_
import com.withwalk.app.ui.theme.middle_grey
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.withwalk.app.R
import com.example.pet_walk.ui.Screen
import com.example.pet_walk.core.TokenManager
import com.example.pet_walk.data.remote.model.LoginRequest
import com.withwalk.app.ui.component.CustomUserTextField
import com.withwalk.app.ui.screen.homepage.NavBack
import com.withwalk.app.ui.theme.PetWalkTheme


private val TAG = "카카오 로그인"

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()){

    NavBack()

    val context = LocalContext.current

    val token by viewModel.token.collectAsState()
    val message by viewModel.message.collectAsState()

    val kakaoMsg by viewModel.kakaoMessage.collectAsState()
    val kakaoEmail by viewModel.kakaoEmail.collectAsState()
    Log.d(TAG, "카카오 메세지: ${kakaoMsg}")
    Log.d(TAG, "메세지: ${message}")
    LaunchedEffect(token) {
        if(token.isNotEmpty()) {
            val tokenManager = TokenManager(context)
            tokenManager.saveToken("Bearer $token")
            navController.navigate(Screen.Home.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
    LaunchedEffect(kakaoMsg) {
        when (kakaoMsg) {
            "Success" -> {
                TokenManager(context).saveToken("Bearer $token")
                navController.navigate(Screen.Home.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            "KAKAO_NEW" -> {
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("userEmail", kakaoEmail)
                navController.navigate(Screen.Regist.route)
            }
            "Empty" -> {
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("userEmail", kakaoEmail)
                navController.navigate(Screen.Regist.route)
            }
        }
        viewModel.clearKakaoMessage()
    }

    LaunchedEffect(message) {
        when (message) {
            "KAKAO_ACCOUNT" -> {
                Toast.makeText(context, "카카오로 가입된 계정입니다.\n카카오 로그인을 이용해주세요.", Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }

            "INVALID_CREDENTIALS" -> {
                Toast.makeText(context, "이메일 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .background(white)
            .padding(15.dp)
            .fillMaxSize()
    ) {
        val (logo, emailBox, storeEmail, emailCheck, passwordBox, passwordTxt, loginBox, loginTxt, findPassword, singupTxt, loginWithKakao, errorMsg) = createRefs()
        val horizonGuideLine1 = createGuidelineFromTop(0.15f)
        var msg = viewModel.errorMsg.collectAsState().value

        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            color = main,
            modifier = Modifier.constrainAs(logo){
                top.linkTo(horizonGuideLine1)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        val horizonGuideLine2 = createGuidelineFromTop(0.3f)


        val sharedPref = context.getSharedPreferences("email_pref", Context.MODE_PRIVATE)
        var checked by remember {
            mutableStateOf(sharedPref.getBoolean("store_check", false))
        }
        var emailText by remember {
            mutableStateOf(if(checked)sharedPref.getString("store_email", "") ?: "" else "")
        }

        Box(
            modifier = Modifier.constrainAs(emailBox) {
                top.linkTo(horizonGuideLine2)
            }
        ) {
            CustomUserTextField(
                value = emailText,
                onValueChange = { emailText = it },
                placeholder = "이메일을 입력해주세요"
            )
        }
        Text(
            text = stringResource(R.string.store_email),
            style = MaterialTheme.typography.labelSmall,
            color = middle_grey,
            modifier = Modifier.constrainAs(storeEmail){
                top.linkTo(emailBox.bottom, 8.dp)
                start.linkTo(emailCheck.end, 5.dp) }
        )


        Checkbox(
            checked = checked,
            onCheckedChange = { isChecked ->
                checked= isChecked
                sharedPref.edit().putBoolean("store_check", isChecked).apply()
                              },
            modifier = Modifier
                .size(20.dp)
                .constrainAs(emailCheck) {
                    start.linkTo(emailBox.start)
                    top.linkTo(storeEmail.top)
                    bottom.linkTo(storeEmail.bottom)
                },
            colors = CheckboxDefaults.colors(
                checkmarkColor = main,
                uncheckedColor = middle_grey,
                checkedColor = white
            )
        )

        var passwordText by remember { mutableStateOf("") }
        Box(
            modifier = Modifier.constrainAs(passwordBox) {
                top.linkTo(emailCheck.bottom, 30.dp)
            }
        ) {
            CustomUserTextField(
                value = passwordText,
                onValueChange = { passwordText = it },
                placeholder = "비밀번호를 입력해주세요"
            )
        }
        if(msg == "401") {
            Text(
                modifier = Modifier.constrainAs(errorMsg){
                    top.linkTo(passwordBox.bottom, 3.dp)
                    start.linkTo(passwordBox.start)
                },
                text = "비밀번호나 아이디가 일치하지않습니다.", style = MaterialTheme.typography.labelSmall, color = error_)
        }

        Button(
            modifier = Modifier
                .constrainAs(loginBox) {
                    top.linkTo(passwordBox.bottom, 40.dp)
                }
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = main),
            onClick = {
                val requst = LoginRequest(emailText, passwordText)
                viewModel.postLogin(requst)
            }
        ) {
            Text(text = stringResource(R.string.login), style = MaterialTheme.typography.bodyMedium)
        }
        TextButton(
            modifier = Modifier.constrainAs(singupTxt){
                top.linkTo(loginBox.bottom, 10.dp)
                end.linkTo(parent.end)
            },
            onClick = {
                navController.navigate(Screen.Signup.route)
            }
        ){
            Text(text = "회원가입",
                color = middle_grey,
                style = MaterialTheme.typography.labelMedium)
        }

        IconButton(
            modifier = Modifier
                .constrainAs(loginWithKakao) {
                    top.linkTo(loginBox.bottom, 70.dp)
                }
                .fillMaxWidth()
                .background(color = Color.Yellow),

            onClick = {
                /* 카카로오 로그인 */
                LoginWithKakao(context, navController, viewModel)
            }
        ) {
            Image(
                painter = painterResource(R.drawable.kakao),
                contentDescription = null
            )
        }
    }
}
// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
private fun LoginWithKakao(context: Context, navController: NavController, viewModel: AuthViewModel){
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            loginWithNewScopes(token.accessToken, navController, viewModel)
        }
    }

    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오톡으로 로그인 실패", error)
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) { return@loginWithKakaoTalk }
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            } else if (token != null) {
                Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                loginWithNewScopes(token.accessToken, navController, viewModel)
            }
        }
    } else {
        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
    }
}
// 사용자 정보 요청 (사용 가능한 모든 동의항목을 대상으로 추가 동의 필요 여부 확인 및 추가 동의를 요청하는 예제입니다.)
private fun loginWithNewScopes(token: String, navController: NavController, viewModel: AuthViewModel){
    UserApiClient.instance.me { user, error ->
        if (error != null) {
            Log.e(TAG, "사용자 정보 요청 실패", error)
        }
        else if (user != null) {
            Log.i(TAG, "사용자 정보 요청 성공")
            val email = user.kakaoAccount?.email!!
            val nickname = user.kakaoAccount?.profile?.nickname
            val profile = user.kakaoAccount?.profile?.profileImageUrl

            viewModel.kakaoLogin("Bearer $token")
            viewModel._kakaoEmail.value = email
        }

    }
}
