package com.withwalk.app.ui.screen.walk

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.withwalk.app.R
import com.example.pet_walk.core.TokenManager
import com.example.pet_walk.data.remote.model.RecordRequest
import com.example.pet_walk.ui.screen.homepage.HomepageViewModel
import com.withwalk.app.ui.MainActivity
import com.withwalk.app.ui.component.ProfileImageViewModel
import com.withwalk.app.ui.screen.chart.ChartViewModel
import com.withwalk.app.ui.screen.login.AuthViewModel
import com.withwalk.app.ui.theme.PetWalkTheme
import com.withwalk.app.ui.theme.black
import com.withwalk.app.ui.theme.dark_grey
import com.withwalk.app.ui.theme.main
import com.withwalk.app.ui.theme.point_green
import com.withwalk.app.ui.theme.sky_morning
import com.withwalk.app.ui.theme.white
import com.withwalk.app.util.ForegroundService
import java.time.LocalDate

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
private fun PrevWalk(){
    val context = LocalContext.current
    val service = ForegroundService()
    val walkViewModel = WalkViewModel(service)
    PetWalkTheme {
        WalkResultScreen(walkViewModel)
    }
}

@SuppressLint("ViewModelConstructorInComposable", "StateFlowValueCalledInComposition")
@Composable
fun WalkResultScreen(
    walkViewModel: WalkViewModel = hiltViewModel(),
    viewModel: ChartViewModel = hiltViewModel(),
    homeViewModel: HomepageViewModel = hiltViewModel()
){
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = black,
            darkIcons = false
        )
    }

    val distance = walkViewModel.distance.value
    var time = walkViewModel.time.value
    val formattedTime = walkViewModel.timeFormat(time)
    val requestTime = walkViewModel.timeFormatForRequest(time)
    val step = walkViewModel.step.value
    var slowStep = walkViewModel.slowStep.value
    var nomalStep = walkViewModel.nomalStep.value
    val formatNomalStep = walkViewModel.nomalStepFormat(nomalStep)
    val formatSlowStep = walkViewModel.calculateSlowStep(formatNomalStep, time)
    var ment = walkViewModel.comment(time, formatNomalStep, formatSlowStep)
    val postWalkResult by viewModel.postWalkResult.collectAsState()

    val tokenManager = TokenManager(LocalContext.current)
    val token = tokenManager.getToken()!!

    LaunchedEffect(Unit) {
        homeViewModel.getHomePage(token)
    }
    val dog = homeViewModel.dog.collectAsState().value
    val dogImg = dog.img

    val profile: ProfileImageViewModel = viewModel()
    val dogProfile = when (ment) {
        "# 멈추지않는 강아지" -> profile.runDogKind[dogImg] ?: R.drawable.transparent
        "# 여유로운 강아지" -> profile.slowDogKind[dogImg] ?: R.drawable.transparent
        else -> profile.dogKind[dogImg] ?: R.drawable.transparent

    }
    val context = LocalContext.current
    LaunchedEffect(postWalkResult) {
        if (postWalkResult == true) {
            context.stopService(Intent(context, ForegroundService::class.java))
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = sky_morning)
    ) {
        val today = LocalDate.now()

        val (background, dogImg, walk, check) = createRefs()
        val (dash2, dash3) = createRefs()

        val guideLine1 = createGuidelineFromTop(0.35f)
        val guideLine2 = createGuidelineFromTop(0.45f)
        val guideLine3 = createGuidelineFromTop(0.65f)

        Image(
            painter = painterResource(R.drawable.walk_result_hill),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(background){
                    top.linkTo(guideLine1)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            )
        Image(
            painter = painterResource(id = dogProfile),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .constrainAs(dogImg) {
                    bottom.linkTo(guideLine1, -45.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Column(
            modifier = Modifier
                .constrainAs(walk) {
                    bottom.linkTo(guideLine3)
                }
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp) // 세트 간격
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${step.toInt()}", fontFamily = FontFamily(Font(R.font.gmarketsans_medium)), fontSize = 32.sp, color = dark_grey)
                Text("총 걸음 수", style = MaterialTheme.typography.labelMedium, color = dark_grey,
                    modifier = Modifier.padding(top = 15.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val formattedDistance = String.format("%.1f", distance / 1000.0)
                Text("${formattedDistance} km", fontFamily = FontFamily(Font(R.font.gmarketsans_medium)), fontSize = 32.sp, color = dark_grey)
                Text("총 산책 거리", style = MaterialTheme.typography.labelMedium, color = dark_grey,
                    modifier = Modifier.padding(top = 15.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${formattedTime}", fontFamily = FontFamily(Font(R.font.gmarketsans_medium)), fontSize = 32.sp, color = dark_grey)
                Text("총 산책 시간", style = MaterialTheme.typography.labelMedium, color = dark_grey,
                    modifier = Modifier.padding(top = 15.dp))
            }

        }

        Box(
            modifier = Modifier.constrainAs(dash3){
                top.linkTo(walk.bottom, 20.dp)
                start.linkTo(walk.start)
                end.linkTo(walk.end)
            }
        ) {
            //DashedDivider()
        }

        val (walkTag, stopTag, comment) = createRefs()
        Text(
            text = "# ${formatNomalStep}분 걷고",
            color = dark_grey,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.notosanskr_bold)),
            modifier = Modifier
                .constrainAs(walkTag) { top.linkTo(dash3.bottom, 20.dp) }
                .padding(horizontal = 30.dp)
        )
        Text(
            text = "# ${formatSlowStep}분 쉬었어요!",
            color = dark_grey,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.notosanskr_bold)),
            modifier = Modifier
                .constrainAs(stopTag) { top.linkTo(walkTag.bottom, 10.dp) }
                .padding(horizontal = 30.dp)
        )
        Text(
            text = ment,
            color = main,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.notosanskr_bold)),
            modifier = Modifier
                .constrainAs(comment) { top.linkTo(stopTag.bottom, 10.dp) }
                .padding(horizontal = 30.dp)
        )

        Box(
            modifier = Modifier.constrainAs(dash2){
                top.linkTo(comment.bottom, 20.dp)
                start.linkTo(walk.start)
                end.linkTo(walk.end)
            }
        ) {
           // DashedDivider()
        }

        IconButton(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(check) {
                    top.linkTo(dash2.bottom, 20.dp)
                },
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("startDestination", "main")
                context.startActivity(intent)
                /* 결과 보내기 */
                val request = RecordRequest(
                    stepCount= step.toInt(),
                    distance= String.format("%.1f", distance / 1000.0).toFloat(),
                    time=requestTime,
                    slowStepTime=formatSlowStep.toInt(),
                    nomalStepTime=formatNomalStep.toInt(),
                    date= today.toString()
                )

                viewModel.postWalk(token, request)
            }
        ) {
            Text(
                text = "확인",
                color = white,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(color = main, shape = RoundedCornerShape(3.dp))
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }
    }
}