package com.withwalk.app.ui.screen.walk

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import com.withwalk.app.R
import com.withwalk.app.api.TokenManager
import com.withwalk.app.api.model.RecordRequest
import com.withwalk.app.data.Repository.ChartRepository
import com.withwalk.app.ui.MainActivity
import com.withwalk.app.ui.screen.chart.ChartViewModel
import com.withwalk.app.ui.screen.chart.ChartViewModelFactory
import com.withwalk.app.ui.theme.PetWalkTheme
import com.withwalk.app.ui.theme.dark_grey
import com.withwalk.app.ui.theme.grey
import com.withwalk.app.ui.theme.light_grey
import com.withwalk.app.ui.theme.main
import com.withwalk.app.ui.theme.middle_grey
import com.withwalk.app.ui.theme.point_green
import com.withwalk.app.ui.theme.white
import com.withwalk.app.util.ForegroundService
import java.time.LocalDate

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
private fun PrevWalk(){
    val context = LocalContext.current
    val drawable = ContextCompat.getDrawable(context, R.drawable.img_album_exp3)
    val bitmap = drawable?.toBitmap()!!
    val service = ForegroundService()
    val walkViewModel = WalkViewModel(service)
    PetWalkTheme {
        WalkResultScreen(bitmap, walkViewModel)
    }
}

@SuppressLint("ViewModelConstructorInComposable", "StateFlowValueCalledInComposition")
@Composable
fun WalkResultScreen(bitmap: Bitmap, walkViewModel: WalkViewModel){
    val distance = walkViewModel.distance.value
    var time = walkViewModel.time.value
    val formattedTime = walkViewModel.timeFormat(time)
    val requestTime = walkViewModel.timeFormatForRequest(time)
    val step = walkViewModel.step.value
    var slowStep = walkViewModel.slowStep.value
    var nomalStep = walkViewModel.nomalStep.value
    val formatNomalStep = walkViewModel.nomalStepFormat(nomalStep)
    val formatSlowStep = walkViewModel.calculateSlowStep(formatNomalStep, time)
    val ment = walkViewModel.comment(time, formatNomalStep, formatSlowStep)
    val repository = ChartRepository()
    val factory = ChartViewModelFactory(repository)
    val viewModel: ChartViewModel = viewModel(factory = factory)
    val postWalkResult by viewModel.postWalkResult.collectAsState()

    val tokenManager = TokenManager(LocalContext.current)
    val token = tokenManager.getToken()!!

    val context = LocalContext.current
    LaunchedEffect(postWalkResult) {
        if (postWalkResult == true) {
            context.stopService(Intent(context, ForegroundService::class.java))
        }
    }

    ConstraintLayout(
        //0xffFFF9F2
        modifier = Modifier.fillMaxSize().background(color = Color(0xffffffff)).padding(20.dp)
    ) {
        val (map, receipt, location, date, walk, check) = createRefs()
        val (dash1, dash2, dash3) = createRefs()

        Text(
            text = "TODAY WALK MAP",
            color = dark_grey,
            fontFamily = FontFamily(Font(R.font.notosanskr_semibold)),
            fontSize = 20.sp,
            modifier = Modifier.constrainAs(location){
                top.linkTo(parent.top, 7.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        val today = LocalDate.now()
        Text(
            text = "${today.year}. ${today.monthValue}. ${today.dayOfMonth}",
            style = MaterialTheme.typography.labelLarge,
            color = middle_grey,
            modifier = Modifier.constrainAs(date){
                top.linkTo(location.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        val guideLine2 = createGuidelineFromTop(0.76f)


        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .aspectRatio(1f)
                .constrainAs(map){
                    top.linkTo(date.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Box(
            modifier = Modifier.constrainAs(dash2){ top.linkTo(map.bottom)}
        ) {
            DashedDivider()
        }
        Column(
            modifier = Modifier.constrainAs(walk){
                top.linkTo(dash2.bottom, 10.dp)
                bottom.linkTo(guideLine2)
            }
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp) // 세트 간격
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${step.toInt()}", fontFamily = FontFamily(Font(R.font.gmarketsans_medium)), fontSize = 32.sp, color = grey)
                Text("총 걸음 수", style = MaterialTheme.typography.labelMedium, color = middle_grey,
                    modifier = Modifier.padding(top = 10.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val formattedDistance = String.format("%.1f", distance / 1000.0)
                Text("${formattedDistance} km", fontFamily = FontFamily(Font(R.font.gmarketsans_medium)), fontSize = 32.sp, color = grey)
                Text("총 산책 거리", style = MaterialTheme.typography.labelMedium, color = middle_grey,
                    modifier = Modifier.padding(top = 10.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${formattedTime}", fontFamily = FontFamily(Font(R.font.gmarketsans_medium)), fontSize = 32.sp, color = grey)
                Text("총 산책 시간", style = MaterialTheme.typography.labelMedium, color = middle_grey,
                    modifier = Modifier.padding(top = 10.dp))
            }

        }
        Box(
            modifier = Modifier.constrainAs(dash3){ top.linkTo(guideLine2)}
        ) {
            DashedDivider()
        }

        val (walkTag, stopTag, comment) = createRefs()
        Text(
            text = "# ${formatNomalStep}분 걷고",
            color = grey,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.constrainAs(walkTag) { top.linkTo(dash3.bottom, 20.dp) }
                .padding(horizontal = 30.dp)
        )
        Text(
            text = "# ${formatSlowStep}분 쉬었어요!",
            color = grey,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.constrainAs(stopTag) {top.linkTo(walkTag.bottom, 10.dp)}
                .padding(horizontal = 30.dp)
        )
        Text(
            text = ment,
            color = main,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.constrainAs(comment) {top.linkTo(stopTag.bottom, 10.dp)}
                .padding(horizontal = 30.dp)
        )


        IconButton(
            modifier = Modifier.constrainAs(check){
                bottom.linkTo(parent.bottom)
            }.fillMaxWidth(),
            onClick = {
                Log.d("산책 정보 등록", "클릭됨")
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
                Log.d("산책 정보 등록", "데이터형식확인 " +
                        "시간: ${requestTime} - " +
                        "날짜: ${today.toString()} - " +
                        "거리: ${distance} - " +
                        "걸음수: ${step.toInt()} - " +
                        "느린 걸음수: ${formatSlowStep.toInt()} - " +
                        "보통 걸음수: ${formatNomalStep.toInt()}"
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
                    .background(color = point_green, shape = RoundedCornerShape(3.dp))
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }
    }
}
@Composable
fun DashedDivider() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        val pathEffect = PathEffect.dashPathEffect(
            floatArrayOf(10.dp.toPx(), 5.dp.toPx()), 0f
        )
        drawLine(
            color = light_grey,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = 1.dp.toPx(),
            pathEffect = pathEffect
        )
    }
}
