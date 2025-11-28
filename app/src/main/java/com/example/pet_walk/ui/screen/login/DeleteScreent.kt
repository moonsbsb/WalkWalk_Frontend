package com.withwalk.app.ui.screen.login

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.withwalk.app.ui.theme.dark_grey
import com.withwalk.app.ui.theme.main
import com.withwalk.app.ui.theme.middle_grey
import com.withwalk.app.ui.theme.sky_morning
import com.withwalk.app.ui.theme.white
import com.withwalk.app.R
import com.withwalk.app.Screen
import com.withwalk.app.api.TokenManager
import com.withwalk.app.data.Repository.AuthRepository
import com.withwalk.app.ui.component.ProfileImageViewModel
import com.withwalk.app.ui.theme.PetWalkTheme

@Preview
@Composable
private fun prevDeleteScreen(){
    val navController = rememberNavController()
    PetWalkTheme {
        DeleteScreen(navController)
    }
}
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DeleteScreen(navController: NavController){
    val repository = AuthRepository()
    val factory = AuthViewModelFactory(repository)
    val viewModel: AuthViewModel = viewModel(factory = factory)
    val tokenManager = TokenManager(LocalContext.current)
    val token = tokenManager.getToken()!!

    LaunchedEffect(Unit) { viewModel.getHomePage(token) }

    val dog by viewModel.dog.collectAsState()
    val _dogProfile = dog.img
    val profile: ProfileImageViewModel = viewModel()
    val dogProfile = profile.dogKind[_dogProfile] ?: R.drawable.transparent
    Column(
        modifier = Modifier.fillMaxSize().background(color = white).padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.3f))
        Text(
            text = "정말 삭제하시겠어요?",
            fontFamily = FontFamily(Font(R.font.gmarketsans_medium)),
            fontSize = 30.sp,
            color = dark_grey
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "소중한 반려견의 산책기록이 사라져요!",
            style = MaterialTheme.typography.labelMedium,
            color = dark_grey
        )
        Spacer(modifier = Modifier.weight(0.2f))
        Image(
            painter = painterResource(dogProfile),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .aspectRatio(280f/190f)
        )
        Spacer(modifier = Modifier.weight(0.2f))
        TextButton(onClick = {
            /* 뒤로가기 */
            navController.popBackStack()
        }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(color = main, RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.Center,
            ){
                Text(
                    text="유지하기",
                    color = white,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            }


        }

        TextButton(onClick = {
            /* 인증메일 보내는 API 연결 */
            Log.d("탈퇴 인증코드", "클릭됨")
            viewModel.verifyCode(token)
            navController.navigate(Screen.CheckNumber.route)
        }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(color = sky_morning, RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.Center,
            ){
                Text(text="삭제하기",
                    color = middle_grey,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center)
            }

        }
        Spacer(modifier = Modifier.weight(0.3f))
    }
}