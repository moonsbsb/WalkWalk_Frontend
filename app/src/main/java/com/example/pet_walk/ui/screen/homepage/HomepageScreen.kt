package com.withwalk.app.ui.screen.homepage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.withwalk.app.ui.theme.point_red
import com.withwalk.app.ui.theme.sub_main
import com.withwalk.app.ui.theme.third_main
import com.withwalk.app.ui.theme.white
import com.withwalk.app.R
import com.withwalk.app.Screen
import com.withwalk.app.api.TokenManager
import com.withwalk.app.api.model.TodayRequest
import com.withwalk.app.data.Repository.AuthRepository
import com.withwalk.app.data.Repository.TodayRepository
import com.withwalk.app.ui.component.ProfileImageViewModel
import com.withwalk.app.ui.component.darkRoundBtn
import com.withwalk.app.ui.screen.login.AuthViewModel
import com.withwalk.app.ui.screen.login.AuthViewModelFactory
import com.withwalk.app.ui.screen.today.TodayViewModel
import com.withwalk.app.ui.screen.today.TodayViewModelFactory
import com.withwalk.app.ui.screen.today.todayCheck
import com.withwalk.app.ui.screen.walk.WalkActivity
import com.withwalk.app.ui.theme.PetWalkTheme
import com.withwalk.app.ui.theme.grey
import com.withwalk.app.ui.theme.point_green
import com.withwalk.app.ui.theme.sky_morning
import com.withwalk.app.ui.theme.sky_night
import com.withwalk.app.ui.theme.sky_sunset
import com.withwalk.app.ui.theme.sun_night
import com.withwalk.app.ui.theme.sun_sunset
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    PetWalkTheme {
        HomeScreen(navController)
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(navController: NavController){
    NavBack()
    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    val token = tokenManager.getToken()!!

    val repository = AuthRepository()
    val factory = AuthViewModelFactory(repository)
    val viewModel: AuthViewModel = viewModel(factory=factory)

    LaunchedEffect(Unit) {
        viewModel.getHomePage(token)
    }

    val dog = viewModel.dog.collectAsState()
    val dogName = dog.value.name
    val dogBirth = dog.value.birth
    val img = dog.value.img
    val profile: ProfileImageViewModel = viewModel()
    val dogImg = profile.dogKind[img] ?: R.drawable.transparent

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(checkSky())
    ) {
        val (title, homepageImg, dogBirthDday, walkBtn) = createRefs()
        val (backgrounfImage, dogImage, table) = createRefs()
        checkSun()

        // 배경 이미지(언덕)
        Image(
            painter = painterResource(R.drawable.home_hill),
            contentDescription = null,
            modifier = Modifier.constrainAs(backgrounfImage) {
                top.linkTo(homepageImg.bottom, -35.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            },
            contentScale = ContentScale.Crop
        )
        val middleGuide = createGuidelineFromTop(0.7f)

        // 프로필 이미지
        Image(
            painter = painterResource(dogImg),
            contentDescription = null,
            modifier = Modifier.constrainAs(homepageImg) {
                bottom.linkTo(middleGuide, margin = 15.dp)
                centerHorizontallyTo(parent)
                width = Dimension.percent(0.85f)
                height = Dimension.ratio("280:190")
            }
        )
        // 강아지 이름
        Text(
            text = "${dogName}의 생일까지",
            color = grey,
            fontSize = 13.sp,
            fontFamily = FontFamily(Font(R.font.helvetica_rounded_black)),
            modifier = Modifier.constrainAs(title){
                top.linkTo(homepageImg.bottom)
                bottom.linkTo(walkBtn.top)
                start.linkTo(walkBtn.start)
            }
        )
        // 강아지 생일 디데이
        Text(
            text = "${dogBirth}",
            fontFamily = FontFamily(Font(R.font.helvetica_rounded_black)),
            fontSize = 29.sp,
            color = grey,
            modifier = Modifier.constrainAs(dogBirthDday){
                top.linkTo(title.bottom, 5.dp)
                start.linkTo(title.start)
            }

        )

        // 산책하기 버튼
        darkRoundBtn(
            text = stringResource(id = R.string.walk),
            modifier = Modifier
                .constrainAs(walkBtn) {
                    bottom.linkTo(parent.bottom, 20.dp)
                    centerHorizontallyTo(parent)
                    width = Dimension.percent(0.85f)
                    height = Dimension.percent(0.06f)
                }
                .background(color = point_green, RoundedCornerShape(25.dp))
        ){
            val intent = Intent(context, WalkActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            context.startActivity(intent)
            //navController.navigate(Screen.Walk.route)
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun NavBack(){
    val context = LocalContext.current
    var backTime by remember { mutableStateOf(0L) }

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if(currentTime - backTime < 2000){
            (context as Activity).finishAffinity()
        }else{
            backTime = currentTime
                Toast.makeText(context, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }
}

/* 현재 시간 반영하여 배경색 바꾸기 */
fun checkSky(): Color {
    val hour = LocalDateTime.now().hour

    val color = when{
        hour in 19..23 || hour in 0 .. 5 -> sky_night
        hour in 16 .. 18 -> sky_sunset
        else -> sky_morning
    }
    return color
}

/* 현재 시간 반영하여 배경색 바꾸기 */
fun changeSunColor(): Color {
    val hour = LocalDateTime.now().hour

    val color = when{
        hour in 19..23 || hour in 0 .. 5 -> sun_night
        hour in 16 .. 18 -> sun_sunset
        else -> sun_sunset
    }
    return color
}

/* 현재 시간 반영하여 해 -> 달 바꾸기 */
@Composable
fun checkSun(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 15.dp, top = 25.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(changeSunColor(), Color.Transparent),
                            center = Offset(size.width / 2, size.height / 2),
                            radius = size.minDimension / 1.4f
                        ),
                        radius = size.minDimension,
                        center = center
                    )
                }
                .background(changeSunColor(), CircleShape)
                .align(Alignment.CenterEnd)
        )
    }
}
