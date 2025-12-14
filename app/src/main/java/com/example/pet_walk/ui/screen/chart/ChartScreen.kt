package com.withwalk.app.ui.screen.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.withwalk.app.ui.theme.point_red
import com.withwalk.app.ui.theme.sub_main
import java.time.LocalDate
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.withwalk.app.ui.theme.dark_grey
import com.withwalk.app.ui.theme.error_
import com.withwalk.app.ui.theme.light_grey
import com.withwalk.app.ui.theme.main
import com.withwalk.app.ui.theme.middle_grey
import com.withwalk.app.ui.theme.white
import com.withwalk.app.R
import com.withwalk.app.api.TokenManager
import com.withwalk.app.ui.introText
import com.withwalk.app.ui.screen.homepage.NavBack
import com.withwalk.app.ui.theme.point_green
import com.withwalk.app.ui.theme.sky_morning
import com.withwalk.app.ui.theme.sky_night
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale



/*@Preview(showBackground = true)
@Composable
fun prev(){
    PetWalkTheme {
        ChartToday()
    }
}*/
@Composable
fun ChartToday(viewModel: ChartViewModel = hiltViewModel()){
    // 뒤로가기 종료
    NavBack()
    // 토큰 받아오기
    val tokenManager = TokenManager(LocalContext.current)
    val token = tokenManager.getToken()!!

    // 차트 정보 받아오기
    val result by viewModel.dayChart.collectAsState()

    val stepCount = result.stepCount
    val distance = String.format("%.2f", result.distance).toFloat()
    val time = result.time
    val slowPercent = result.slowPercent ?: 0
    val nomalPercent = result.nomalPercent ?: 0
    val kcal = result.kcal
    val stepPercent = result.stepPercent ?: 0
    val slowStepTime = result.slowStepTime ?: 0
    val nomalStepTime = result.nomalStepTime ?: 0

    val data = if(nomalPercent!=0){100 - nomalPercent }else{0}


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = sky_morning),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource( R.string.today_walk ),
                color = dark_grey,
                style = MaterialTheme.typography.labelMedium,
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        var year by rememberSaveable { mutableStateOf(LocalDate.now().year) }
        var month by rememberSaveable { mutableStateOf(LocalDate.now().monthValue) }
        Box(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .background(
                    color = white,
                    RoundedCornerShape(13.dp)
                ),
        ) {
            Column {
                YearMonthPicker(
                    year = year,
                    month = month,
                    onYearChange = { year = it },
                    onMonthChange = { month = it }
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        val m = "%02d".format(month)
        LaunchedEffect(Unit) { viewModel.getChartByDate(token, LocalDate.now().toString()) }
        LazyDay(year, month){ onDaySeleted ->
            val day = "%02d".format(onDaySeleted)
            viewModel.getChartByDate(token, "$year-$m-$day")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(
                    color = white,
                    RoundedCornerShape(30.dp)
                )
                .padding(30.dp)
        ){
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                val (kcalTxt,kcalNum, kcals) = createRefs()
                val (walk,walkBar, stop, stopBar, step, circleBar, sPercent, nPercent, steps) = createRefs()
                val verticalCenter = createGuidelineFromStart(0.5f)

                introText(
                    "산책 칼로리",
                    modifier = Modifier.constrainAs(kcalTxt){
                    }
                )
                Text(
                    text = "${kcal} kcal",
                    style = MaterialTheme.typography.labelLarge,
                    color = main,
                    modifier = Modifier.constrainAs(kcalNum){
                        top.linkTo(kcalTxt.bottom, 5.dp)
                    }
                )
                introText(
                    "산책 걸음수",
                    modifier = Modifier.constrainAs(steps){
                        start.linkTo(verticalCenter)
                    }
                )
                Text(
                    text = "$stepCount Steps",
                    style = MaterialTheme.typography.labelLarge,
                    color = main,
                    modifier = Modifier.constrainAs(step){
                        start.linkTo(verticalCenter)
                        top.linkTo(steps.bottom, 5.dp)
                    }
                )
                val (distances, kmNum, km) = createRefs()
                introText(
                    "산책 거리",
                    modifier = Modifier.constrainAs(distances){
                        top.linkTo(kcalNum.bottom, 40.dp)
                    }
                )
                Text(
                    text = "${distance} km",
                    style = MaterialTheme.typography.labelLarge,
                    color = point_green,
                    modifier = Modifier.constrainAs(kmNum){
                        top.linkTo(distances.bottom, 5.dp)
                    }
                )

                val (hour, hourNum) = createRefs()

                introText(
                    "산책 시간",
                         modifier = Modifier.constrainAs(hour){
                             top.linkTo(distances.top)
                             start.linkTo(verticalCenter)
                         }
                    )
                Text(
                    text = "$time",
                    style = MaterialTheme.typography.labelLarge,
                    color = point_green,
                    modifier = Modifier.constrainAs(hourNum){
                        top.linkTo(hour.bottom, 5.dp)
                        start.linkTo(hour.start)
                    }
                )

            }

        }
        Column (
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .background(
                    color = white,
                    RoundedCornerShape(30.dp),
                )
                .padding(30.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text="총 걸음 비율", style = MaterialTheme.typography.labelMedium, color = middle_grey)
            }
            // 바 차트
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp)
            ) {
                SimpleBarChart(nomalStepTime, slowStepTime)
            }


            // 걸음 소개
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Image( painterResource(R.drawable.nomal_walk_icon), contentDescription = null )
                    Text( text = "빠르게 걸었어요", style = MaterialTheme.typography.labelMedium, color = middle_grey )
                }
                Text( text = "${nomalStepTime}분", style = MaterialTheme.typography.labelMedium, color = middle_grey)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Image(painterResource(R.drawable.slow_walk_icon), contentDescription = null)
                    Text(text="천천히 걸었어요", style = MaterialTheme.typography.labelMedium, color = middle_grey)
                }
                Text(text="${slowStepTime}분", style = MaterialTheme.typography.labelMedium, color = middle_grey)
            }


        }
    }
}

/* 일 선택 */
@Composable
fun LazyDay(year: Int = LocalDate.now().year, month: Int = LocalDate.now().monthValue, onDaySelected: (Int) -> Unit = {}){

    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
    val today = LocalDate.now().dayOfMonth
    var selectedDay by remember { mutableStateOf(today) }

    val listState = rememberLazyListState()
    LaunchedEffect(Unit) { listState.scrollToItem(selectedDay-1) }

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth(.9f)
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(daysInMonth) { index ->
            val day = index + 1
            val datOfWeek = LocalDate.of(year, month, day)
            val dayOfWeek = datOfWeek.dayOfWeek!!
            var isSelected = day == selectedDay

            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .background(
                        if (isSelected) dark_grey
                        else sky_morning,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(5.dp)
                    .clickable {
                        selectedDay = day
                        onDaySelected(day)
                    },
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                    style = MaterialTheme.typography.labelSmall,
                    color = if(isSelected) white else dark_grey
                )
                Text(
                    text = day.toString(),
                    color = if (isSelected) white else dark_grey,
                    style = MaterialTheme.typography.labelLarge
                )

            }

        }
    }
}

/* 년 월 선택 후 반환 */
@Composable
fun YearMonthPicker(year: Int, month: Int, onYearChange: (Int) -> Unit, onMonthChange: (Int) -> Unit){
    val years = (2025..2030).toList()
    val months = (1..12).toList()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ){
            /* 연도 드롭다운 */
            var yearDrop by remember { mutableStateOf(false) }
            Box {
                TextButton(onClick = { yearDrop = true }) {
                    Text(
                        text = "${year}.",
                        color = dark_grey,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                DropdownMenu(
                    expanded = yearDrop,
                    onDismissRequest = {yearDrop = false}
                ) {
                    years.forEach { y ->
                        DropdownMenuItem(
                            text = { Text("${y}년") },
                            onClick = {
                                yearDrop = false
                                onYearChange(y)
                            }
                        )
                    }
                }
            }
            /* 월 드롭다운 */
            var monthDrop by remember { mutableStateOf(false) }
            Box{
                TextButton(onClick = { monthDrop = true }) {
                    Text(
                        text = "${month}",
                        color = point_red,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                DropdownMenu(
                    expanded = monthDrop,
                    onDismissRequest = {monthDrop = false},
                ) {
                    months.forEach {  m ->
                        DropdownMenuItem(
                            text = { Text("${m}월") },
                            onClick = {
                                monthDrop = false
                                onMonthChange(m)
                            }
                        )
                    }
                }
            }
        }
    }


}

/* 바 차트 */
@Composable
fun SimpleBarChart(nomal: Int, slow: Int) {
    // 0~100 사이로 클램프
    val total = nomal + slow

    // 값이 0일 때
    if(total == 0){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(light_grey) // 전체 바의 배경색
        )
    }
    val slowRate = slow / total.toFloat()
    val nomalRate = nomal / total.toFloat()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(light_grey)
    ) {
        // nomal Step비율
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(nomalRate)
                .background(main)
        )
        // Slow Step비율
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val totalWidth = this.maxWidth
            val widthA = totalWidth * nomalRate

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    // A의 너비만큼 오른쪽으로 이동
                    .offset(x = widthA)
                    // B의 비율만큼 너비 설정
                    .fillMaxWidth(slowRate)
                    .background(sky_night)
            )
        }
    }
}



