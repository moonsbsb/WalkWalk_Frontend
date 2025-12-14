package com.example.pet_walk.ui.screen.attendance

import android.content.Context
import  androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pet_walk.data.HiddenMessage
import com.withwalk.app.R
import com.withwalk.app.api.TokenManager
import com.withwalk.app.ui.theme.PetWalkTheme
import com.withwalk.app.ui.theme.light_grey
import com.withwalk.app.ui.theme.main
import com.withwalk.app.ui.theme.sky_sunset
import com.withwalk.app.ui.theme.sub_main
import com.withwalk.app.ui.theme.white
import java.time.LocalDate
import kotlin.random.Random


@Preview
@Composable
private fun prevAttendanve(){
    PetWalkTheme { AttendanceScreen() }
}

// 팝업 메세지
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun popupMessage(message: String, status: () -> Unit){
    BasicAlertDialog(
        onDismissRequest = status
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.5f)
                .padding(32.dp),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBE6)) // 노란색 배경
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "행운의 멍멍", fontFamily = FontFamily(Font(R.font.gmarketsan_bold)), fontSize = 15.sp)
                Text(text = message, fontFamily = FontFamily(Font(R.font.gmarketsans_medium)), fontSize = 13.sp)
            }
            Spacer(Modifier.height(16.dp))
            Button(
                modifier = Modifier.padding(12.dp),
                onClick = status,
                colors = ButtonColors(
                    containerColor = main,
                    contentColor = white,
                    disabledContainerColor = sky_sunset,
                    disabledContentColor = sky_sunset
                ),
                shape = RoundedCornerShape(10)
            ) {
                Text("닫기")
            }
        }
    }

}

// 스크린 가로비율 적용
@Composable
fun calculateScreenWidth(percentage: Float): Dp{
    val config = LocalConfiguration.current
    val screenWidthDp  = config.screenWidthDp.dp
    return screenWidthDp * percentage
}

// UI 화면
@Composable
fun AttendanceScreen(viewModel: AttendenceViewModel = hiltViewModel()){

    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    val token = tokenManager.getToken()!!

    val year = LocalDate.now().year
    val date = LocalDate.now().lengthOfMonth()
    val month = LocalDate.now().monthValue

    LaunchedEffect(Unit) { viewModel.getAttendence(token,"${year}-${month}") }
    val attendence = viewModel.attendence.collectAsState()
    var distance by remember { mutableStateOf(0f) }
    var min by remember { mutableStateOf(0) }
    var count by remember { mutableStateOf(0) }

    distance = (attendence.value.distanceSum)
    min = attendence.value.minSum
    count = attendence.value.count

    val m by viewModel.message.collectAsState()
    var popupCount by remember { mutableStateOf(0) }
    var popupStatus by remember { mutableStateOf(false) }

    if(popupStatus){
        popupMessage(
            message = m[popupCount],
            status = {popupStatus = false}
        )
    }
    var notOpenStatus by remember { mutableStateOf(false) }
    val notOpenMessage = "\n\n" + "아직 잠겨있어요.\n" + "산책 도장을 찍어서 도착하면 열려요!\n"
    if(notOpenStatus){
        popupMessage(
            message = notOpenMessage,
            status = {notOpenStatus = false}
        )
    }

    val expressions = listOf(R.drawable.expression1, R.drawable.expression2, R.drawable.expression3,R.drawable.expression4,R.drawable.expression5)

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (distanceSum, hourSum, imoji1, imoji2, imoji3, imoji4, imoji5) = createRefs()
        val (b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15) = createRefs()
        val (b16, b17, b18, b19, b20, b21, b22, b23, b24, b25, b26, b27, b28, b29, b30, b31) = createRefs()

        /* 박스 관련 변수 */
        val boxSize = calculateScreenWidth(0.155f)
        val boxColor = Color(0xffFFFAED)
        val corner = RoundedCornerShape(8.dp)
        val closeHorizonMargin = 10.dp
        val horizonMargin = 20.dp

        // 배경 삽입
        Image(
            painter = painterResource(R.drawable.attendance_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = "${month}월 걸은 거리: ${distance}m",
            fontFamily = FontFamily(Font(R.font.gmarketsans_medium)),
            fontSize = 15.sp,
            modifier = Modifier.constrainAs(distanceSum){
                start.linkTo(parent.start, 15.dp)
                top.linkTo(parent.top, 15.dp)
            }
        )
        Text(
            text = "${month}월 걸은 시간: ${min}분",
            fontFamily = FontFamily(Font(R.font.gmarketsans_medium)),
            fontSize = 15.sp,
            modifier = Modifier.constrainAs(hourSum){
                start.linkTo(distanceSum.end)
                end.linkTo(parent.end)
                top.linkTo(distanceSum.top)
            }
        )

        /* ================== 1열 ==================================== */
        val startGuide = createGuidelineFromStart(0.05f)
        val endGuide = createGuidelineFromEnd(0.05f)
        val comment = createRef()
        if(count >= 1){
            Image(
                painter = painterResource(expressions[0]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b1) {
                        start.linkTo(startGuide)
                        top.linkTo(parent.top, 80.dp)
                    }
            )
        }else{
            Image(
                painter = painterResource(R.drawable.expression_not),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b1) {
                        start.linkTo(startGuide)
                        top.linkTo(parent.top, 80.dp)
                    }
            )
            Box(
                modifier = Modifier
                    .background(color = white, shape = RoundedCornerShape(topStart = 0.dp, topEnd = 8.dp, bottomStart = 8.dp, bottomEnd = 8.dp))
                    .constrainAs(comment) {
                        start.linkTo(startGuide)
                        top.linkTo(b1.bottom, 5.dp)
                    }.padding(10.dp)
            ) {
                Text(text = "산책을 해서 빈 칸을 채워주세요!", style = MaterialTheme.typography.labelMedium)
            }
        }
        if(count >= 2) {
            Image(
                painter = painterResource(expressions[1]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b2) {
                        start.linkTo(b1.end, closeHorizonMargin)
                        top.linkTo(b1.top)
                        bottom.linkTo(b1.bottom)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b2) {
                        start.linkTo(b1.end, closeHorizonMargin)
                        top.linkTo(b1.top)
                        bottom.linkTo(b1.bottom)
                    }
            )
        }

        if(count >= 3) {
            Image(
                painter = painterResource(expressions[2]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b3) {
                        start.linkTo(b2.end, closeHorizonMargin)
                        top.linkTo(b2.top)
                        bottom.linkTo(b2.bottom)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b3) {
                        start.linkTo(b2.end, closeHorizonMargin)
                        top.linkTo(b2.top)
                        bottom.linkTo(b2.bottom)
                    }
            )
        }

        if(count >= 4) {
            Image(
                painter = painterResource(expressions[3]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b4) {
                        start.linkTo(b3.end, closeHorizonMargin)
                        top.linkTo(b3.top)
                        bottom.linkTo(b3.bottom)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b4) {
                        start.linkTo(b3.end, closeHorizonMargin)
                        top.linkTo(b3.top)
                        bottom.linkTo(b3.bottom)
                    }
            )
        }

        if(count >= 5) {
            Image(
                painter = painterResource(expressions[4]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b5) {
                        //start.linkTo(b4.end, closeHorizonMargin)
                        top.linkTo(b4.top, horizonMargin)
                        bottom.linkTo(b4.bottom)
                        end.linkTo(endGuide)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b5) {
                        //start.linkTo(b4.end, closeHorizonMargin)
                        top.linkTo(b4.top, horizonMargin)
                        bottom.linkTo(b4.bottom)
                        end.linkTo(endGuide)
                    }
            )
        }

        if(count >= 6) {
            Image(
                painter = painterResource(expressions[3]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b6) {
                        top.linkTo(b5.bottom, horizonMargin)
                        end.linkTo(endGuide)

                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b6) {
                        top.linkTo(b5.bottom, horizonMargin)
                        end.linkTo(endGuide)

                    }
            )
        }

        /* ================== 2열 ==================================== */
        if(count >= 7) {
            Image(
                painter = painterResource(R.drawable.hidden_msg),
                contentDescription = null,
                modifier = Modifier.constrainAs(b7) {
                    top.linkTo(b6.bottom, horizonMargin)
                    end.linkTo(b5.end)
                }.clickable {
                    popupCount = 0
                    popupStatus = true
                    notOpenStatus = false
                }
            )
        }else{
            Image(
                painter = painterResource(R.drawable.hidden_msg),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(b7) {
                        top.linkTo(b6.bottom, horizonMargin)
                        end.linkTo(b5.end)
                    }.clickable {
                        popupStatus = false
                        notOpenStatus = true
                    }
            )
        }
        if(count >= 8) {
            Image(
                painter = painterResource(expressions[2]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b8) {
                        top.linkTo(b7.top)
                        bottom.linkTo(b7.bottom)
                        end.linkTo(b4.end)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b8) {
                        top.linkTo(b7.top)
                        bottom.linkTo(b7.bottom)
                        end.linkTo(b4.end)
                    }
            )
        }
        if(count >= 9) {
            Image(
                painter = painterResource(expressions[1]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b9) {
                        end.linkTo(b3.end)
                        start.linkTo(b3.start)
                        bottom.linkTo(b8.bottom)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b9) {
                        end.linkTo(b3.end)
                        start.linkTo(b3.start)
                        bottom.linkTo(b8.bottom)
                    }
            )
        }
        if(count >= 10) {
            Image(
                painter = painterResource(expressions[0]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b10) {
                        end.linkTo(b2.end)
                        start.linkTo(b2.start)
                        bottom.linkTo(b9.bottom)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b10) {
                        end.linkTo(b2.end)
                        start.linkTo(b2.start)
                        bottom.linkTo(b9.bottom)
                    }
            )
        }

        if(count >= 11) {
            Image(
                painter = painterResource(expressions[2]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b11) {
                        end.linkTo(b1.end)
                        start.linkTo(b1.start)
                        bottom.linkTo(b10.bottom, -horizonMargin)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b11) {
                        end.linkTo(b1.end)
                        start.linkTo(b1.start)
                        bottom.linkTo(b10.bottom, -horizonMargin)
                    }
            )
        }

        /* ================== 3열 ==================================== */

        if(count >= 12) {
            Image(
                painter = painterResource(expressions[1]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b12) {
                        end.linkTo(b11.end)
                        start.linkTo(b11.start)
                        top.linkTo(b11.bottom, horizonMargin)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b12) {
                        end.linkTo(b11.end)
                        start.linkTo(b11.start)
                        top.linkTo(b11.bottom, horizonMargin)
                    }
            )
        }
        if(count >= 13) {
            Image(
                painter = painterResource(expressions[4]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b13) {
                        end.linkTo(b10.end)
                        start.linkTo(b10.start)
                        top.linkTo(b12.top, horizonMargin)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b13) {
                        end.linkTo(b10.end)
                        start.linkTo(b10.start)
                        top.linkTo(b12.top, horizonMargin)
                    }
            )
        }
        if(count >= 14) {
            Image(
                painter = painterResource(R.drawable.hidden_msg),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(b14) {
                        start.linkTo(b9.start)
                        top.linkTo(b13.top)
                        bottom.linkTo(b13.bottom)
                    }.clickable {
                        popupCount = 1
                        popupStatus = true
                        notOpenStatus = false
                    }
            )
        }else{
            Image(
                painter = painterResource(R.drawable.hidden_msg),
                contentDescription = null,
                modifier = Modifier.constrainAs(b14) {
                    start.linkTo(b9.start)
                    top.linkTo(b13.top)
                    bottom.linkTo(b13.bottom)
                }.clickable {
                    popupStatus = false
                    notOpenStatus = true
                }
            )
        }
        if(count >= 15) {
            Image(
                painter = painterResource(expressions[0]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b15) {
                        start.linkTo(b8.start)
                        top.linkTo(b14.top)
                        bottom.linkTo(b14.bottom)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b15) {
                        start.linkTo(b8.start)
                        top.linkTo(b14.top)
                        bottom.linkTo(b14.bottom)
                    }
            )

        }

        if(count >= 16) {
            Image(
                painter = painterResource(expressions[3]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b16) {
                        //start.linkTo(b7.start)
                        top.linkTo(b15.top, horizonMargin)
                        end.linkTo(endGuide)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b16) {
                        //start.linkTo(b7.start)
                        top.linkTo(b15.top, horizonMargin)
                        end.linkTo(endGuide)
                    }
            )

        }

        /* ================== 4열 ==================================== */
        if(count >= 17) {
            Image(
                painter = painterResource(expressions[1]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b17) {
                        //start.linkTo(b16.start)
                        top.linkTo(b16.bottom, horizonMargin)
                        end.linkTo(endGuide)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b17) {
                        //start.linkTo(b16.start)
                        top.linkTo(b16.bottom, horizonMargin)
                        end.linkTo(endGuide)
                    }
            )

        }
        if(count >= 18) {
            Image(
                painter = painterResource(expressions[4]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b18) {
                        end.linkTo(b15.end)
                        top.linkTo(b17.top, horizonMargin)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b18) {
                        end.linkTo(b15.end)
                        top.linkTo(b17.top, horizonMargin)
                    }
            )

        }
        if(count >= 19) {
            Image(
                painter = painterResource(expressions[2]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b19) {
                        top.linkTo(b18.top)
                        start.linkTo(b9.start)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b19) {
                        top.linkTo(b18.top)
                        start.linkTo(b9.start)
                    }
            )
        }
        if(count >= 20) {
            Image(
                painter = painterResource(expressions[0]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b20) {
                        top.linkTo(b19.top)
                        end.linkTo(b13.end)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b20) {
                        top.linkTo(b19.top)
                        end.linkTo(b13.end)
                    }
            )
        }
        if(count >= 21) {
            Image(
                painter = painterResource(R.drawable.hidden_msg),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(b21) {
                        top.linkTo(b20.bottom, -horizonMargin)
                        start.linkTo(b11.start)
                    }
                    .clickable {
                        popupCount = 2
                        popupStatus = true
                        notOpenStatus = false
                    }
            )
        }else{
            Image(
                painter = painterResource(R.drawable.hidden_msg),
                contentDescription = null,
                modifier = Modifier.constrainAs(b21) {
                    top.linkTo(b20.bottom, -horizonMargin)
                    start.linkTo(b11.start)
                }.clickable {
                        popupStatus = false
                        notOpenStatus = true
                }
            )
        }


        /* ================== 5열 ==================================== */
        if(count >= 22) {
            Image(
                painter = painterResource(expressions[1]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b22) {
                        start.linkTo(b21.start)
                        end.linkTo(b21.end)
                        top.linkTo(b21.bottom, horizonMargin)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b22) {
                        start.linkTo(b21.start)
                        end.linkTo(b21.end)
                        top.linkTo(b21.bottom, horizonMargin)
                    }
            )
        }
        if(count >= 23) {
            Image(
                painter = painterResource(expressions[3]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b23) {
                        start.linkTo(b20.start)
                        top.linkTo(b22.top, horizonMargin)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b23) {
                        start.linkTo(b20.start)
                        top.linkTo(b22.top, horizonMargin)
                    }
            )
        }
        if(count >= 24) {
            Image(
                painter = painterResource(expressions[4]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b24) {
                        start.linkTo(b19.start)
                        top.linkTo(b23.top)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b24) {
                        start.linkTo(b19.start)
                        top.linkTo(b23.top)
                    }
            )
        }
        if(count >= 25) {
            Image(
                painter = painterResource(expressions[0]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b25) {
                        start.linkTo(b18.start)
                        top.linkTo(b24.top)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b25) {
                        start.linkTo(b18.start)
                        top.linkTo(b24.top)
                    }
            )
        }

        if(count >= 26) {
            Image(
                painter = painterResource(expressions[0]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b26) {
                        //start.linkTo(b17.start)
                        top.linkTo(b25.top, horizonMargin)
                        end.linkTo(endGuide)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b26) {
                        //start.linkTo(b17.start)
                        top.linkTo(b25.top, horizonMargin)
                        end.linkTo(endGuide)
                    }
            )
        }

        /* ================== 6열 ==================================== */
        if(count >= 27) {
            Image(
                painter = painterResource(expressions[1]),
                contentDescription = null,
                modifier = Modifier
                    .size(boxSize)
                    .constrainAs(b27) {
                        //start.linkTo(b26.start)
                        top.linkTo(b26.bottom, horizonMargin)
                        end.linkTo(endGuide)
                    }
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(color = boxColor, corner)
                    .constrainAs(b27) {
                        //start.linkTo(b26.start)
                        top.linkTo(b26.bottom, horizonMargin)
                        end.linkTo(endGuide)
                    }
            )
        }






        if(date == 28){
            if(count >= 28) {
                Image(
                    painter = painterResource(R.drawable.destination),
                    contentDescription = null,
                    modifier = Modifier.constrainAs(b31) {
                        end.linkTo(b25.end)
                        top.linkTo(b27.top, horizonMargin)
                    }
                )
            }else{
                Image(
                    painter = painterResource(R.drawable.transparent),
                    contentDescription = null,
                    modifier = Modifier.constrainAs(b31) {
                        end.linkTo(b25.end)
                        top.linkTo(b27.top, horizonMargin)
                    }
                )
            }

        }else if(date == 29){
            if(count >= 28) {
                Image(
                    painter = painterResource(expressions[0]),
                    contentDescription = null,
                    modifier = Modifier
                        .size(boxSize)
                        .constrainAs(b28) {
                            end.linkTo(b25.end)
                            top.linkTo(b27.top, horizonMargin)
                        }
                )
            }else{
                Box(
                    modifier = Modifier
                        .size(boxSize)
                        .background(color = boxColor, corner)
                        .constrainAs(b28) {
                            end.linkTo(b25.end)
                            top.linkTo(b27.top, horizonMargin)
                        }
                )
            }
            if(count >= 29) {
                Image(
                    painter = painterResource(R.drawable.destination),
                    contentDescription = null,
                    modifier = Modifier.constrainAs(b31) {
                        end.linkTo(b24.end)
                        top.linkTo(b28.top)
                        bottom.linkTo(b28.bottom)
                    }
                )
            }else{
                Image(
                    painter = painterResource(R.drawable.transparent),
                    contentDescription = null,
                    modifier = Modifier.constrainAs(b31) {
                        end.linkTo(b24.end)
                        top.linkTo(b28.top)
                        bottom.linkTo(b28.bottom)
                    }
                )
            }

        }else if(date == 30){
            if(count >= 28) {
                Image(
                    painter = painterResource(expressions[3]),
                    contentDescription = null,
                    modifier = Modifier
                        .size(boxSize)
                        .constrainAs(b28) {
                            end.linkTo(b25.end)
                            top.linkTo(b27.top, horizonMargin)
                        }
                )
            }else{
                Box(
                    modifier = Modifier
                        .size(boxSize)
                        .background(color = boxColor, corner)
                        .constrainAs(b28) {
                            end.linkTo(b25.end)
                            top.linkTo(b27.top, horizonMargin)
                        }
                )
            }
            if(count >= 29) {
                Image(
                    painter = painterResource(R.drawable.hidden_msg),
                    contentDescription = null,
                    modifier = Modifier
                        .constrainAs(b29) {
                            end.linkTo(b24.end)
                            top.linkTo(b28.top)
                            bottom.linkTo(b28.bottom)
                        }
                        .clickable {
                            popupCount = 3
                            popupStatus = true
                            notOpenStatus = false
                        }
                )
            }else{
                Image(
                    painter = painterResource(R.drawable.hidden_msg),
                    contentDescription = null,
                    modifier = Modifier.constrainAs(b29) {
                        end.linkTo(b24.end)
                        top.linkTo(b28.top)
                        bottom.linkTo(b28.bottom)
                    }.clickable {
                        popupStatus = false
                        notOpenStatus = true
                    }
                )
            }
            if(count >= 30) {
                Image(
                    painter = painterResource(R.drawable.destination),
                    contentDescription = null,
                    modifier = Modifier.constrainAs(b31) {
                        end.linkTo(b23.end)
                        top.linkTo(b28.top)
                    }
                )
            }else{
                Image(
                    painter = painterResource(R.drawable.transparent),
                    contentDescription = null,
                    modifier = Modifier.constrainAs(b31) {
                        end.linkTo(b23.end)
                        top.linkTo(b28.top)
                    }
                )
            }

        }else{
            if(count >= 28) {
                Image(
                    painter = painterResource(expressions[2]),
                    contentDescription = null,
                    modifier = Modifier
                        .size(boxSize)
                        .constrainAs(b28) {
                            end.linkTo(b25.end)
                            top.linkTo(b27.top, horizonMargin)
                        }
                )
            }else{
                Box(
                    modifier = Modifier
                        .size(boxSize)
                        .background(color = boxColor, corner)
                        .constrainAs(b28) {
                            end.linkTo(b25.end)
                            top.linkTo(b27.top, horizonMargin)
                        }
                )
            }
            if(count >= 29) {
                Image(
                    painter = painterResource(R.drawable.hidden_msg),
                    contentDescription = null,
                    modifier = Modifier
                        .constrainAs(b29) {
                            end.linkTo(b24.end)
                            top.linkTo(b28.top)
                            bottom.linkTo(b28.bottom)
                        }
                        .clickable {
                            popupCount = 3
                            popupStatus = true
                            notOpenStatus = false
                        }
                )
            }else{
                Image(
                    painter = painterResource(R.drawable.hidden_msg),
                    contentDescription = null,
                    modifier = Modifier.constrainAs(b29) {
                        end.linkTo(b24.end)
                        top.linkTo(b28.top)
                        bottom.linkTo(b28.bottom)
                    }.clickable {
                        popupStatus = false
                        notOpenStatus = true
                    }
                )
            }
            if(count >= 30) {
                Image(
                    painter = painterResource(expressions[0]),
                    contentDescription = null,
                    modifier = Modifier
                        .size(boxSize)
                        .constrainAs(b30) {
                            end.linkTo(b23.end)
                            top.linkTo(b28.top)
                        }
                )
            }else{
                Box(
                    modifier = Modifier
                        .size(boxSize)
                        .background(color = boxColor, corner)
                        .constrainAs(b30) {
                            end.linkTo(b23.end)
                            top.linkTo(b28.top)
                        }
                )
            }
            if(count >= 30) {
                Image(
                    painter = painterResource(R.drawable.destination),
                    contentDescription = null,
                    modifier = Modifier.constrainAs(b31) {
                        end.linkTo(b22.end)
                        top.linkTo(b28.top)
                    }
                )
            }else{
                Image(
                    painter = painterResource(R.drawable.transparent),
                    contentDescription = null,
                    modifier = Modifier.constrainAs(b31) {
                        end.linkTo(b22.end)
                        top.linkTo(b28.top)
                    }
                )
            }

        }





    }
}
