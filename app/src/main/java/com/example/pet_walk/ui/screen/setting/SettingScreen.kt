package com.withwalk.app.ui.screen.setting

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.withwalk.app.ui.theme.main
import com.withwalk.app.ui.theme.middle_grey
import com.withwalk.app.ui.theme.sub_main
import com.withwalk.app.ui.theme.white
import com.withwalk.app.R
import com.withwalk.app.Screen
import com.withwalk.app.api.TokenManager
import com.withwalk.app.data.Repository.AuthRepository
import com.withwalk.app.data.Repository.SettingRepository
import com.withwalk.app.ui.component.ProfileImageViewModel
import com.withwalk.app.ui.screen.homepage.NavBack
import com.withwalk.app.ui.screen.login.AuthViewModel
import com.withwalk.app.ui.screen.login.AuthViewModelFactory
import com.withwalk.app.ui.theme.PetWalkTheme
import com.withwalk.app.ui.theme.grey

@Preview
@Composable
fun prevSetting(){
    val navController = rememberNavController()
    PetWalkTheme {
        SettingScreen(navController)
    }
}
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SettingScreen(navController: NavController){
    NavBack()

    val repository = SettingRepository()
    val factory = SettingViewModelFactory(repository)
    val viewModel: SettingViewModel = viewModel(factory = factory)

    val tokenManager = TokenManager(LocalContext.current)
    val token = tokenManager.getToken()!!

    LaunchedEffect(Unit) {
        viewModel.getDday(token)
    }

    val settingInfo by viewModel.dDay.collectAsState()
    val profileViewModel: ProfileImageViewModel = viewModel()
    val profile = profileViewModel.dogKind[settingInfo.img] ?: R.drawable.transparent

    val authRepository = AuthRepository()
    val authFactory = AuthViewModelFactory(authRepository)
    val authViewModel: AuthViewModel = viewModel(factory = authFactory)

    Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "${settingInfo.name}와 함께한 지",
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.helvetica_rounded_black)),
                    color = grey
                )
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "+${settingInfo.dday}",
                        fontSize = 29.sp,
                        fontFamily = FontFamily(Font(R.font.helvetica_rounded_black)),
                        color = grey
                    )
                    Text(
                        text = " days!",
                        fontSize = 19.sp,
                        fontFamily = FontFamily(Font(R.font.helvetica_rounded_black)),
                        color = grey
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    painter = painterResource(profile),
                    contentDescription = null,
                    modifier = Modifier.size(140.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))



                /*
                * 정보 박스 칸
                * */
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                        .padding(top = 10.dp)
                        .background(Color(0xffffffff), RoundedCornerShape(20.dp)),
                        ){
                    Column (
                        modifier = Modifier
                            .padding(start = 20.dp, top = 20.dp, end = 20.dp),)
                    {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                Modifier.clickable { navController.navigate(Screen.AccountUpdate.route) }
                            ){
                                Text(
                                    text = "개인정보 수정",
                                    fontFamily = FontFamily(Font(R.font.gmarketsans_medium)),
                                    fontSize = 15.sp,
                                    color = Color(0xff3C3C3C)
                                )
                            }

                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .clickable {
                                    authViewModel.logoutUser(token)
                                    navController.navigate(Screen.Login.route){popUpTo(0){inclusive = true} }
                                }
                        ) {
                            Text(
                                text = "로그아웃",
                                fontFamily = FontFamily(Font(R.font.gmarketsans_medium)),
                                fontSize = 15.sp,
                                color = Color(0xff3C3C3C)
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
                                .clickable {
                                    authViewModel.logoutUser(token)
                                    navController.navigate(Screen.Unsubscribe.route)
                                }
                        ) {
                            Text(
                                text="회원 탈퇴",
                                fontFamily = FontFamily(Font(R.font.gmarketsans_medium)),
                                fontSize = 15.sp,
                                color = Color(0xff3C3C3C)
                            )
                    }
                }
            }
                Spacer(modifier = Modifier.height(20.dp))
        }
    }

}
