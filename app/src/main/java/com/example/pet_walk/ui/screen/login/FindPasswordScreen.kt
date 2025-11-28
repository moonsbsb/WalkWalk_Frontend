package com.withwalk.app.ui.screen.login

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.withwalk.app.ui.theme.dark_grey
import com.withwalk.app.ui.theme.grey
import com.withwalk.app.ui.theme.light_grey
import com.withwalk.app.ui.theme.main
import com.withwalk.app.ui.theme.middle_grey
import com.withwalk.app.ui.theme.point_red
import com.withwalk.app.ui.theme.white
import com.withwalk.app.R
import com.withwalk.app.Screen
import com.withwalk.app.api.TokenManager
import com.withwalk.app.api.model.CodeRequest
import com.withwalk.app.data.Repository.AuthRepository
import com.withwalk.app.ui.theme.PetWalkTheme

@Preview
@Composable
private fun prevFindPasswordScreen(){
    val navController = rememberNavController()
    PetWalkTheme {
        //FindPasswordScreen(navController)
        CheckNumberScreen(navController)
        //PasswordResetScreen(navController)
        //ResetCompleteScreen(navController)
        //DeleteCompleteScreen(navController)
    }
}

/* 이메일 작성 화면 */
@Composable
fun FindPasswordScreen(navController: NavController){
    var email by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.1f))

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            color = main
        )

        Spacer(modifier = Modifier.weight(0.2f))

        Text(
            text = stringResource(R.string.email),
            style = MaterialTheme.typography.labelLarge,
            color = grey,
            modifier = Modifier.align(Alignment.Start)
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("가입한 이메일을 작성해주세요!") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = light_grey,
                unfocusedIndicatorColor = light_grey,
                unfocusedLabelColor = light_grey,
                unfocusedPlaceholderColor = light_grey
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().align(Alignment.Start)
        )
        Text(
            text = stringResource(R.string.email_info),
            style = MaterialTheme.typography.labelMedium,
            color = middle_grey,
            modifier = Modifier.align(Alignment.Start).padding(top = 5.dp)
        )

        Spacer(modifier = Modifier.weight(0.3f))

        moveNext(navController, Screen.CheckNumber.route)
    }
}

/* 인증코드 전송 안내 및 확인 확면 */
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CheckNumberScreen(navController: NavController){
    var verificationCode by remember { mutableStateOf("") }
    val repository = AuthRepository()
    val factory = AuthViewModelFactory(repository)
    val viewModel: AuthViewModel = viewModel(factory = factory)

    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    val token = tokenManager.getToken()!!

    val quiteState by viewModel.quite.collectAsState()

    LaunchedEffect(quiteState) {
        Log.d("탈퇴 인증코드 확인", "들어옴")
        if(quiteState.message.equals("Success")){
            navController.navigate(Screen.DeletetComplete.route)
        }
        else if (quiteState.message.isNotEmpty()) {
            Toast.makeText(context, quiteState.message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.background(color = white).fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.1f))

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            color = main
        )

        Spacer(modifier = Modifier.weight(0.2f))

        TextField(
            value = verificationCode,
            onValueChange = { verificationCode = it },
                colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = light_grey,
                unfocusedIndicatorColor = light_grey,
                unfocusedLabelColor = light_grey,
                unfocusedPlaceholderColor = light_grey
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().align(Alignment.Start)
        )

        infoMent(
            text = stringResource(R.string.certification_number),
            style = MaterialTheme.typography.labelMedium,
            color = middle_grey,
            modifier = Modifier.align(Alignment.Start).padding(top = 5.dp)
        )

        Spacer(modifier = Modifier.weight(0.3f))

        /* 탈퇴 안내 화면으로 변경 */
        IconButton(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(color = main, RoundedCornerShape(5.dp)),
            onClick = {
                val code = CodeRequest(verificationCode)
                viewModel.getVerificationCode(token, code)
            }
        ) {
            Text(
                text = stringResource(R.string.ok),
                style = MaterialTheme.typography.bodyMedium,
                color = white
            )
        }
    }
}

@Composable
fun infoMent(text: String, style: TextStyle, color: Color, modifier: Modifier){
    Text(
        text = text,
        style = style,
        color = color,
        modifier = modifier
    )
}



/* 탈퇴 확인 화면 */
@Composable
fun DeleteCompleteScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize().background(white).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.1f))
        Text(
            text = stringResource(R.string.delete_complete),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(R.string.delete_thanks),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(15.dp)
        )
        Spacer(modifier = Modifier.weight(0.1f))

        moveNext(navController, Screen.Login.route)

        Spacer(modifier = Modifier.weight(0.01f))
    }
}

/* 비밀번호 재설정 화면 */
@Composable
fun PasswordResetScreen(navController: NavController){
    var resetPassword by remember { mutableStateOf("") }
    var resetPasswordAgain by remember { mutableStateOf("") }
    var check = false
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.1f))

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            color = main
        )

        Spacer(modifier = Modifier.weight(0.2f))

        Text(
            text = stringResource(R.string.password_reset_text),
            style = MaterialTheme.typography.labelLarge,
            color = grey,
            modifier = Modifier.align(Alignment.Start)
        )
        TextField(
            value = resetPassword,
            onValueChange = { resetPassword = it },
            label = { Text("비밀번호를 입력하세요") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = light_grey,
                unfocusedIndicatorColor = light_grey,
                unfocusedLabelColor = light_grey,
                unfocusedPlaceholderColor = light_grey
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().align(Alignment.Start)
        )

        if(resetPassword != resetPasswordAgain){
            Text(
                text = stringResource(R.string.certification_wrong),
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xffF55411),
                modifier = Modifier.align(Alignment.Start).padding(top = 5.dp)
            )
            check = false
        }else{
            check = true
        }

        TextField(
            value = resetPasswordAgain,
            onValueChange = { resetPasswordAgain = it },
            label = { Text("비밀번호를 다시 입력해주세요") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = light_grey,
                unfocusedIndicatorColor = light_grey,
                unfocusedLabelColor = light_grey,
                unfocusedPlaceholderColor = light_grey
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().align(Alignment.Start).padding(top = 10.dp)
        )
        Spacer(modifier = Modifier.weight(0.3f))

        IconButton(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(color = main, RoundedCornerShape(5.dp)),
            onClick = {
                if(check){
                    navController.navigate(Screen.ResetComplete.route)
                    /* 비밀번호 보내기 */
                }else{
                    Toast.makeText(context, "비밀번호를 알맞게 설정해주세요", Toast.LENGTH_SHORT).show()
                }

            }
        ) {
            Text(
                text = stringResource(R.string.ok),
                style = MaterialTheme.typography.bodyMedium,
                color = white
            )
        }
    }
}

/* 비밀번호 재설정 완료 화면 */
@Composable
fun ResetCompleteScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.3f))
        Text(
            text = stringResource(R.string.password_reset_complete),
            style = MaterialTheme.typography.displayLarge,
            color = dark_grey
        )

        Spacer(modifier = Modifier.weight(0.3f))
        moveNext(navController, Screen.Login.route)
    }
}

@Composable
fun moveNext(navController: NavController, route: String){
    IconButton(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(color = main, RoundedCornerShape(5.dp)),
        onClick = { navController.navigate(route) }
    ) {
        Text(
            text = stringResource(R.string.ok),
            style = MaterialTheme.typography.bodyMedium,
            color = white
        )
    }
}