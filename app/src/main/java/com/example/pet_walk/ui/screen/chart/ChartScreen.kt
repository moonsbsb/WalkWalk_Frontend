package com.withwalk.app.ui.screen.chart

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.withwalk.app.ui.theme.point_red
import com.withwalk.app.ui.theme.sub_main
import java.time.LocalDate
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.withwalk.app.ui.theme.dark_grey
import com.withwalk.app.ui.theme.error_
import com.withwalk.app.ui.theme.grey
import com.withwalk.app.ui.theme.light_grey
import com.withwalk.app.ui.theme.main
import com.withwalk.app.ui.theme.middle_grey
import com.withwalk.app.ui.theme.sky_night
import com.withwalk.app.ui.theme.white
import com.withwalk.app.R
import com.withwalk.app.api.TokenManager
import com.withwalk.app.data.Repository.ChartRepository
import com.withwalk.app.ui.introText
import com.withwalk.app.ui.screen.homepage.NavBack
import com.withwalk.app.ui.theme.PetWalkTheme
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.StringFormat
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale



@Preview(showBackground = true)
@Composable
fun prev(){
    PetWalkTheme {
        ChartToday()
    }
}
@Composable
fun ChartToday(){
    NavBack()

    val repository = ChartRepository()
    val factory = ChartViewModelFactory(repository)
    val viewModel: ChartViewModel = viewModel(factory = factory)

    val tokenManager = TokenManager(LocalContext.current)
    val token = tokenManager.getToken()!!

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = sub_main),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

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
                style = MaterialTheme.typography.labelLarge,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        var year by rememberSaveable { mutableStateOf(LocalDate.now().year) }
        var month by rememberSaveable { mutableStateOf(LocalDate.now().monthValue) }
        Box(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .height(45.dp)
                .background(
                    color = Color(0xffFFE3AD),
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
                Text("선택된 날짜: $year 년 $month 월")
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        val m = "%02d".format(month)
        LaunchedEffect(Unit) { viewModel.getChartByDate(token, LocalDate.now().toString()) }
        LazyDay(year, month){ onDaySeleted ->
            val day = "%02d".format(onDaySeleted)
            viewModel.getChartByDate(token, "$year-$m-$day")
        }
        val result by viewModel.dayChart.collectAsState()

        val meal = result.meal
        val water = result.water
        val poo = result.poo
        val stepCount = result.stepCount
        val distance = String.format("%.2f", result.distance).toFloat()
        val time = result.time
        val slowPercent = result.slowPercent ?: 0
        val nomalPercent = result.nomalPercent ?: 0
        val kcal = result.kcal
        val stepPercent = result.stepPercent ?: 0

        val data = if(nomalPercent!=0){100 - nomalPercent }else{0}
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(450.dp)
                .background(
                    color = Color(0xccffffff),
                    RoundedCornerShape(30.dp)
                )
                .padding(start = 15.dp, top = 25.dp, end = 15.dp)
        ){
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
            ){
                val (kcalNum, kcals) = createRefs()
                Text(
                    text = "$kcal",
                    style = MaterialTheme.typography.displayLarge,
                    color = point_red,
                    modifier = Modifier.constrainAs(kcalNum){

                    }
                )
                Text(
                    text = "kcal",
                    fontFamily = FontFamily(Font(R.font.gmarketsans_light)),
                    fontSize = 15.sp,
                    color = Color(0xff65320E),
                    modifier = Modifier.constrainAs(kcals){
                        start.linkTo(kcalNum.end, 10.dp)
                        bottom.linkTo(kcalNum.bottom)
                    }
                )
                val (distances, kmNum, km) = createRefs()
                introText("오늘걸은 거리",
                    modifier = Modifier.constrainAs(distances){
                        top.linkTo(kcalNum.bottom, 40.dp)
                    }
                )
                Text(
                    text = "$distance",
                    fontFamily = FontFamily(Font(R.font.gmarketsan_bold)),
                    fontSize = 14.sp,
                    modifier = Modifier.constrainAs(kmNum){
                        top.linkTo(distances.bottom, 10.dp)
                    }
                )
                Text(
                    text = "km",
                    fontSize = 13.sp,
                    fontFamily = FontFamily(Font(R.font.gmarketsans_light)),
                    modifier = Modifier.constrainAs(km){
                        start.linkTo(kmNum.end, 5.dp)
                        bottom.linkTo(kmNum.bottom)
                    }
                )
                val (hour, hourNum) = createRefs()

                introText("오늘 걸은 시간",
                    modifier = Modifier.constrainAs(hour){
                        top.linkTo(kmNum.bottom, 40.dp)
                    }
                )
                Text(
                    text = "$time",
                    fontFamily = FontFamily(Font(R.font.gmarketsan_bold)),
                    fontSize = 14.sp,
                    modifier = Modifier.constrainAs(hourNum){
                        top.linkTo(hour.bottom, 10.dp)
                    }
                )
                val (walk,walkBar, stop, stopBar, step, circleBar, sPercent, nPercent, steps) = createRefs()
                introText(
                    stringResource(id = R.string.chart_walk),
                    modifier = Modifier.constrainAs(walk){
                        top.linkTo(hourNum.bottom, 40.dp)
                    }
                )
                Box(
                    modifier = Modifier.constrainAs(walkBar){
                        top.linkTo(walk.bottom, 10.dp)
                        start.linkTo(walk.start)
                    }
                ) {
                    //val sampleData = if(nomalPercent!=0){100 - nomalPercent }else{0}
                    SimpleBarChart(progress = nomalPercent, color = main)

                }
                Text(
                    text = "${nomalPercent}%",
                    style = MaterialTheme.typography.displaySmall,
                    color = grey,
                    modifier = Modifier.constrainAs(nPercent){
                        top.linkTo(walkBar.bottom)
                        end.linkTo(walkBar.end)
                    }.padding(5.dp)
                )
                val verticalGuideLine1 = createGuidelineFromEnd(0.6f)
                introText(
                    stringResource(id = R.string.chart_stop),
                    modifier = Modifier.constrainAs(stop){
                        top.linkTo(walkBar.bottom, 20.dp)
                        start.linkTo(walk.start)
                    }
                )
                Box(
                    modifier = Modifier.constrainAs(stopBar){
                        top.linkTo(stop.bottom, 10.dp)
                        start.linkTo(stop.start)
                    }
                ) {
                    val sampleData = if(nomalPercent!=0){100 - nomalPercent }else{0}
                    Log.d("퍼센트확인", "$sampleData")
                    SimpleBarChart(progress = data, color = sky_night)
                }

                Text(
                    text = "${data}%",
                    style = MaterialTheme.typography.displaySmall,
                    color = grey,
                    modifier = Modifier.constrainAs(sPercent){
                        top.linkTo(stopBar.bottom)
                        end.linkTo(stopBar.end)
                    }.padding(5.dp)
                )

                Text(
                    text = "$stepCount",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(step){
                        start.linkTo(circleBar.start)
                        end.linkTo(circleBar.end)
                        top.linkTo(circleBar.top)
                        bottom.linkTo(circleBar.bottom)
                    }
                )
                Text(
                    text = "steps",
                    style = MaterialTheme.typography.labelMedium,
                    color = middle_grey,
                    modifier = Modifier.constrainAs(steps){
                        start.linkTo(step.start)
                        end.linkTo(step.end)
                        top.linkTo(step.bottom)
                    }
                )
                Box(
                    modifier = Modifier.constrainAs(circleBar){
                        start.linkTo(kcals.end, 20.dp)
                        end.linkTo(stopBar.end)
                        top.linkTo(kcals.bottom)
                        bottom.linkTo(stop.top)
                    }
                ) {
                    val data = stepPercent / 100f
                    circleBar(data)
                }

            }

        }
        Column (
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .background(
                    color = Color(0xccffffff),
                    RoundedCornerShape(30.dp)
                )
                .padding(15.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(100.dp, Alignment.Start)
            ) {
                Text(text="음수량", style = MaterialTheme.typography.labelMedium, color = point_red)
                Text(text="배변횟수", style = MaterialTheme.typography.labelMedium, color = point_red)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Image(painterResource(R.drawable.chart_water), contentDescription = null)
                Image(painterResource(R.drawable.chart_poo), contentDescription = null)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, end = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(100.dp, Alignment.End)
            ) {
                Text(text="$water ml", style = MaterialTheme.typography.labelMedium, color = dark_grey)
                Text(text="$poo 번", style = MaterialTheme.typography.labelMedium, color = dark_grey)
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
        horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                        if (isSelected) point_red
                        else sub_main,
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
                    color = if(isSelected) white else point_red
                )
                Text(
                    text = day.toString(),
                    color = if (isSelected) white else point_red,
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
                        color = point_red,
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
fun SimpleBarChart(progress: Int, color: Color) {
    val clampedProgress = progress.coerceIn(0, 100)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(light_grey)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(clampedProgress / 100f)
                .background(color)
        )
    }
}

/* 원형 바 */
@Composable
fun circleBar(data: Float){
    Canvas(modifier = Modifier.size(190.dp)) {
        val strokeWidth = 20f
        val radius = size.minDimension / 2 - strokeWidth / 2
        val centerOffset = Offset(size.width / 2, size.height / 2)

        // 배경 반원
        drawArc(
            color = Color.LightGray,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
        // 데이터 반원
        drawArc(
            color = error_,
            startAngle = 180f,
            sweepAngle = 180f * data,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}


