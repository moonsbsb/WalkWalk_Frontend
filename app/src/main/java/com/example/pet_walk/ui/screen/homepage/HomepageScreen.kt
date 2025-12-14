package com.withwalk.app.ui.screen.homepage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.graphics.RectangleShape
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
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.BuildConfig
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kakao.vectormap.LatLng
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
import com.withwalk.app.ui.MainActivity
import com.withwalk.app.ui.component.ProfileImageViewModel
import com.withwalk.app.ui.component.darkRoundBtn
import com.withwalk.app.ui.screen.homepage.GeoTransform.latLngToGrid
import com.withwalk.app.ui.screen.login.AuthViewModel
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel(), weatherViewModel: WeatherViewModel = hiltViewModel()){
    // 뒤로가기 시 종료
    NavBack()

    val context = LocalContext.current
    val activity = context as MainActivity
    val tokenManager = TokenManager(context)
    val token = tokenManager.getToken()!!

    // 홈페이지 API 호출
    LaunchedEffect(Unit) {
        viewModel.getHomePage(token)
    }
    // 강아지 정보
    val dog = viewModel.dog.collectAsState().value
    val dogName = dog.name
    val dogBirth = dog.birth
    val img = dog.img
    val profile: ProfileImageViewModel = viewModel()
    val dogImg = profile.dogKind[img] ?: R.drawable.transparent
    val dogWeight = dog.weight

    // 위치 권한 받아오기
    val fused = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationRequest = remember {
        LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            90000L
        ).setWaitForAccurateLocation(true).build()
    }

    // 날씨 정보 받아오기
    val weatherMap = weatherViewModel.weather.collectAsState().value
    var warningMsg = weatherViewModel.weatherMsg.collectAsState().value
    val temperature = weatherMap["T1H"] ?: "0"  // 온도
    val humidity = weatherMap["PTY"]?.toIntOrNull()

    val lottieJson: Int? = remember(humidity){
        Log.d("날씨조회", "${humidity}")
        when(humidity){
            1, 2, 5, 6 -> R.raw.rain
            3, 7 -> R.raw.snow
            else -> null
        }
    }
    // 날씨 메세지 받아오기
    if(weatherMap != null){ weatherViewModel.instructionBasedonWeight(dogWeight, temperature) }

    val now = LocalDate.now()
    val dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")
    val date = now.format(dateFormat)
    val hour = LocalDateTime.now().minusHours(1)
    val hourFormat = String.format("%02d", hour.hour)
    val callback = remember {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val lat = result.lastLocation?.latitude
                val lng = result.lastLocation?.longitude

                if (lat != null && lng != null) {
                    val grid = latLngToGrid(lat, lng)
                    weatherViewModel.getWeather(
                        com.withwalk.app.BuildConfig.WEATHER_ENCODING_KEY,
                        1,
                        1000,
                        "JSON",
                        date,
                        "${hourFormat}00",
                        grid.first,
                        grid.second
                    )
                }
            }
        }

    }
    // 위치 정보 확인
    LaunchedEffect(Unit) {
        val check = activity.requestCheckAndRequestPermissions()
        if(check){
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) { }
            fused.requestLocationUpdates(
                locationRequest,
                callback,
                Looper.getMainLooper()
            )
        }
    }


    // 홈 화면 UI
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(checkSky())
    ) {
        val (title, homepageImg, dogBirthDday, walkBtn, source) = createRefs()
        val (backgrounfImage, weatherTxt, weather, speechBubble, warningLight, lottie, sun) = createRefs()
        val centerGuideline = createGuidelineFromStart(0.5f)

        if(lottieJson != null) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(lottieJson)
            )
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever,

                )

            LottieAnimation(
                composition = composition,
                progress = progress,
                contentScale = ContentScale.Crop,
                modifier = Modifier.constrainAs(lottie) {
                    top.linkTo(sun.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(backgrounfImage.top)
                }
            )
        }


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

        // 경고등 이미지
        Image(
            painter = painterResource(warningMsg.second),
            contentDescription = null,
            modifier = Modifier.constrainAs(warningLight){
                bottom.linkTo(speechBubble.top)
                start.linkTo(speechBubble.start, 10.dp)
            }
        )
        // 말풍선
        Box(
            modifier = Modifier
                .background(color = white, shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomEnd = 10.dp))
                .padding(10.dp)
                .constrainAs(speechBubble){
                    bottom.linkTo(homepageImg.top, 10.dp)
                    start.linkTo(centerGuideline)
            }
        ) {
            Text(text = warningMsg.first, style = MaterialTheme.typography.labelMedium)
        }

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

        // 날씨
        Text(
            text = "오늘의 날씨",
            color = grey,
            fontSize = 13.sp,
            fontFamily = FontFamily(Font(R.font.helvetica_rounded_black)),
            modifier = Modifier.constrainAs(weatherTxt){
                top.linkTo(homepageImg.bottom)
                bottom.linkTo(walkBtn.top)
                start.linkTo(centerGuideline)
            }
        )
        // 날씨
        Text(
            text = "${temperature}°C",
            fontFamily = FontFamily(Font(R.font.helvetica_rounded_black)),
            fontSize = 29.sp,
            color = grey,
            modifier = Modifier.constrainAs(weather){
                top.linkTo(weatherTxt.bottom, 5.dp)
                start.linkTo(weatherTxt.start)
            }

        )
        Text(
            text = "출처: 기상청",
            style = MaterialTheme.typography.displaySmall,
            color = point_green,
            modifier = Modifier.constrainAs(source){
                start.linkTo(weatherTxt.end, 5.dp)
                bottom.linkTo(weatherTxt.bottom)
                top.linkTo(weatherTxt.top)
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
            .padding(end = 35.dp, top = 35.dp)
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



/* 위도 경도 변환 */
object GeoTransform {

    private const val RE = 6371.00877 // 지구 반경(km)
    private const val GRID = 5.0       // 격자 간격(km)
    private const val SLAT1 = 30.0     // 투영 위도1(deg)
    private const val SLAT2 = 60.0     // 투영 위도2(deg)
    private const val OLON = 126.0     // 기준점 경도(deg)
    private const val OLAT = 38.0      // 기준점 위도(deg)
    private const val XO = 43.0        // 기준점 X좌표
    private const val YO = 136.0       // 기준점 Y좌표

    fun latLngToGrid(lat: Double, lng: Double): Pair<Int, Int> {
        val DEGRAD = Math.PI / 180.0

        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD

        var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) /
                Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn)

        var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn

        var ro = Math.tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / Math.pow(ro, sn)

        var ra = Math.tan(Math.PI * 0.25 + lat * DEGRAD * 0.5)
        ra = re * sf / Math.pow(ra, sn)

        var theta = lng * DEGRAD - olon
        if (theta > Math.PI) theta -= 2.0 * Math.PI
        if (theta < -Math.PI) theta += 2.0 * Math.PI
        theta *= sn

        val x = (ra * Math.sin(theta) + XO + 0.5).toInt()
        val y = (ro - ra * Math.cos(theta) + YO + 0.5).toInt()

        return Pair(x, y)
    }
}
