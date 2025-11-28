package com.withwalk.app.ui.screen.login

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.withwalk.app.ui.theme.error_
import com.withwalk.app.ui.theme.middle_grey
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.withwalk.app.R
import com.withwalk.app.Screen
import com.withwalk.app.api.TokenManager
import com.withwalk.app.api.model.LoginRequest
import com.withwalk.app.data.Repository.AuthRepository
import com.withwalk.app.ui.component.CustomUserTextField
import com.withwalk.app.ui.theme.PetWalkTheme
import com.withwalk.app.ui.theme.grey


@Preview(showBackground = true)
@Composable
fun prev(){
    val navController = rememberNavController()
    PetWalkTheme {
        LoginScreen(navController)
    }
}
private val TAG = "카카오 로그인"
private lateinit var viewModel: AuthViewModel
private lateinit var kakaoEmail: String

@Composable
fun LoginScreen(navController: NavController){
    val repository = AuthRepository()
    val factory = AuthViewModelFactory(repository)
    viewModel = viewModel(factory = factory)
    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .background(white)
            .padding(15.dp)
            .fillMaxSize()
    ) {
        val (logo, emailBox, storeEmail, emailCheck, passwordBox, passwordTxt, loginBox, loginTxt, findPassword, singupTxt, loginWithKakao, errorMsg) = createRefs()
        val horizonGuideLine1 = createGuidelineFromTop(0.15f)
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
            modifier = Modifier.size(20.dp).constrainAs(emailCheck){
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
        val token by viewModel.token.collectAsState()
        Button(
            modifier = Modifier
                .constrainAs(loginBox) {
                    top.linkTo(passwordBox.bottom, 40.dp)
                }.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = main),
            onClick = {
                val requst = LoginRequest(emailText, passwordText)
                viewModel.postLogin(requst)
            }
        ) {
            if(token.isEmpty()){
            }else{
                val tokenManager = TokenManager(context)
                tokenManager.saveToken("Bearer $token")
                navController.navigate(Screen.Home.route)
            }
            Text(text = stringResource(R.string.login), style = MaterialTheme.typography.bodyMedium)
        }

        /*IconButton (
            modifier = Modifier
                .constrainAs(findPassword) {
                    top.linkTo(loginBox.bottom, 10.dp)
                }
                .fillMaxWidth(0.2f),
            onClick = {}
        ){
            Text(text = "비밀번호찾기",
                style = MaterialTheme.typography.labelSmall)
        }*/

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

        val kakaoMsg by viewModel.kakaoMessage.collectAsState()
        val kakaoToken by viewModel.token.collectAsState()

        var loginTriggered by remember { mutableStateOf(false) }
        Log.d("카카오", "loginTriggered: $loginTriggered")
        if (loginTriggered) {
            LaunchedEffect(kakaoMsg) {
                when (kakaoMsg) {
                    "Success" -> {
                        Log.d("카카오", "메세지: Success")
                        val tokenManager = TokenManager(context)
                        tokenManager.saveToken("Bearer $token")
                        navController.navigate(Screen.Home.route)
                    }
                    "Empty" -> {
                        Log.d("카카오", "메세지: Empty")
                        navController.currentBackStackEntry?.savedStateHandle?.set("userEmail", kakaoEmail)
                        navController.navigate(Screen.Regist.route)
                    }
                    "Error" -> {
                        Log.d("카카오", "메세지: Error")
                    }
                }
            }
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
                loginTriggered = true
                LoginWithKakao(context, navController)
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
private fun LoginWithKakao(context: Context, navController: NavController){
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            loginWithNewScopes(token.accessToken, navController)
        }
    }

    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오톡으로 로그인 실패", error)
                // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우, 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) { return@loginWithKakaoTalk }
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            } else if (token != null) {
                Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                loginWithNewScopes(token.accessToken, navController)
            }
        }
    } else {
        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
    }
}
// 사용자 정보 요청 (사용 가능한 모든 동의항목을 대상으로 추가 동의 필요 여부 확인 및 추가 동의를 요청하는 예제입니다.)
private fun loginWithNewScopes(token: String, navController: NavController){
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
            kakaoEmail = email
            Log.d("카카오", "넘겨주는 이메일: $kakaoEmail")
        }

    }
}
@Composable
private fun errorMsg(messge: String, error: Boolean){
    if(error){
        Text(text = messge, style = MaterialTheme.typography.labelSmall, color = error_)
    }else{

    }
}
