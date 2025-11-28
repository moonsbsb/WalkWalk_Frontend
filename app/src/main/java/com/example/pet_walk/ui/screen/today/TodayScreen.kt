package com.withwalk.app.ui.screen.today

import android.annotation.SuppressLint
import com.withwalk.app.R
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.withwalk.app.ui.theme.dark_grey
import com.withwalk.app.ui.theme.point_green
import com.withwalk.app.ui.theme.sky_morning
import com.withwalk.app.ui.theme.sky_night
import com.withwalk.app.ui.theme.sky_sunset
import com.withwalk.app.ui.theme.sun_night
import com.withwalk.app.ui.theme.sun_sunset
import java.time.LocalDateTime
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.withwalk.app.ui.component.ProfileImageViewModel
import com.withwalk.app.ui.theme.PetWalkTheme
import kotlinx.coroutines.launch
import com.withwalk.app.data.Repository.TodayRepository
import com.withwalk.app.api.TokenManager
import com.withwalk.app.api.model.TodayRequest
import com.withwalk.app.ui.screen.homepage.NavBack

@Preview
@Composable
fun TodayScreen(){
    NavBack()

    val repository = TodayRepository()
    val factory = TodayViewModelFactory(repository)
    val viewModel : TodayViewModel = viewModel(factory = factory)

    val tokenMagager = TokenManager(LocalContext.current)
    val token = tokenMagager.getToken()!!

    /* 정보 조회 요청 */
    LaunchedEffect(Unit) { viewModel.getToday(token) }
    val today = viewModel.todayData.collectAsState()
    var mealCount by remember { mutableStateOf(0) }
    var waterCount by remember { mutableStateOf(0) }
    var pooCount by remember { mutableStateOf(0) }

    mealCount = today.value.meal
    waterCount = today.value.water
    pooCount = today.value.poo

    var name = today.value.name

    var profileViewModel: ProfileImageViewModel = viewModel()
    val profile = profileViewModel.dogKind[today.value.img] ?: R.drawable.transparent

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            //.background(checkSky())
    ) {
        val (backgrounfImage, dogImage, title, table) = createRefs()
        //checkSun()

        val horizonGuideLine2 = createGuidelineFromTop(0.2f)

        Image(
            painter = painterResource(profile),
            contentDescription = null,
            modifier = Modifier.constrainAs(dogImage) {
                top.linkTo(horizonGuideLine2)
                centerHorizontallyTo(parent)
                width = Dimension.percent(0.7f)
                height = Dimension.ratio("280:190")
            }
        )
        val horizonGuideLine3 = createGuidelineFromTop(0.45f)
        val verticalGuideLine1 = createGuidelineFromStart(0.05f)
        val verticalGuideLine2 = createGuidelineFromStart(0.95f)
        Text(
            text = stringResource(id = R.string.today_pet, name),
            style = MaterialTheme.typography.displayLarge,
            color = point_green,
            modifier = Modifier.constrainAs(title){
                top.linkTo(horizonGuideLine3)
                start.linkTo(verticalGuideLine1)
            }
        )
        Column(
            modifier = Modifier.constrainAs(table){
                start.linkTo(verticalGuideLine1)
                end.linkTo(verticalGuideLine2)
                top.linkTo(title.bottom, 30.dp)
            }
        ) {
            todayCheck(R.drawable.today_meal, R.string.today_meal, mealCount, onCount = {count -> if(count < 10 )mealCount = count else mealCount = 9})
            todayCheck(R.drawable.today_water_icon, R.string.today_water, waterCount, onCount = {count -> if(count < 990 ) waterCount = count else waterCount = 990})
            todayCheck(R.drawable.today_poo, R.string.today_poo, pooCount, onCount = {count -> if(count < 10 )pooCount = count else pooCount = 9})
        }
    }


    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver{_, event ->
            if(event == Lifecycle.Event.ON_PAUSE){
                Log.d("오늘의 어쩌구 스크린", "화면 벗어남이 감지")
                val todayRequest = TodayRequest(mealCount, waterCount, pooCount)
                viewModel.postToday(token, todayRequest)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}
/* 반복 형식 */

@Composable
fun todayCheck(icon: Int, title: Int, count: Int, onCount: (Int) -> Unit){
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(height = 70.dp) // 최소 높이
    ) {
        var (img, text, prev, num, next) = createRefs()
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.constrainAs(img) {
                width = Dimension.value(40.dp)
                height = Dimension.value(40.dp)
            }
        )
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.displaySmall,
            color = dark_grey,
            modifier = Modifier.constrainAs(text) {
                start.linkTo(img.end, 20.dp)
                bottom.linkTo(img.bottom)
            }
        )
        IconButton(
            onClick = {
                if (title == R.string.today_water) {
                    if (count > 10) onCount(count - 10)
                } else {
                    if (count > 0) onCount(count - 1)
                }
            },
            modifier = Modifier.constrainAs(prev) {
                width = Dimension.value(40.dp)
                height = Dimension.value(40.dp)
                end.linkTo(next.start, 20.dp)
            }
        ) {
            Icon(
                painterResource(R.drawable.prev_btn),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }

        Text(
            /* 여기서 prev, next 버튼 누르면 맞춰서 수 조정하고 화면을 나가면 기록하기 */
            text = "${count}",
            style = MaterialTheme.typography.bodyMedium,
            color = sky_morning,
            modifier = Modifier.constrainAs(num) {
                top.linkTo(prev.top)
                bottom.linkTo(prev.bottom)
                start.linkTo(prev.end)
                end.linkTo(next.start)
            }
        )
        IconButton(
            onClick = {
                if (title == R.string.today_water) {
                    onCount(count + 10)
                } else {
                    onCount(count + 1)
                }
            },
            modifier = Modifier.constrainAs(next) {
                width = Dimension.value(40.dp)
                height = Dimension.value(40.dp)
                end.linkTo(parent.end)
            }
        ) {
            Icon(
                painterResource(R.drawable.next_btn),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}
