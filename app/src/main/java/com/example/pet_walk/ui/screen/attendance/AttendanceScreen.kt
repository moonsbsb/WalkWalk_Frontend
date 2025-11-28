package com.example.pet_walk.ui.screen.attendance

import android.health.connect.datatypes.units.Percentage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.pet_walk.data.HiddenMessage
import com.withwalk.app.R
import com.withwalk.app.ui.theme.PetWalkTheme
import java.time.LocalDate
import kotlin.random.Random


@Preview
@Composable
private fun prevAttendanve(){
    PetWalkTheme { AttendanceScreen() }
}
@Composable
fun AttendanceScreen(){
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

        val date = LocalDate.now().lengthOfMonth()
        val month = LocalDate.now().monthValue

        var message by remember { mutableStateOf<String?>(null) }

        message?.let { popupMessage(it) }

        // 배경 삽입
        Image(
            painter = painterResource(R.drawable.attendance_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = "${month}월 걸은 거리: ",
            fontFamily = FontFamily(Font(R.font.gmarketsans_light)),
            fontSize = 15.sp,
            modifier = Modifier.constrainAs(distanceSum){
                start.linkTo(parent.start, 15.dp)
                top.linkTo(parent.top, 15.dp)
            }
        )
        Text(
            text = "${month}월 걸은 시간: ",
            fontFamily = FontFamily(Font(R.font.gmarketsans_light)),
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
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b1) {
                    start.linkTo(startGuide)
                    top.linkTo(parent.top, 80.dp)
                }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b2) {
                    start.linkTo(b1.end, closeHorizonMargin)
                    top.linkTo(b1.top)
                    bottom.linkTo(b1.bottom)
                }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b3) {
                    start.linkTo(b2.end, closeHorizonMargin)
                    top.linkTo(b2.top)
                    bottom.linkTo(b2.bottom)
                }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b4) {
                    start.linkTo(b3.end, closeHorizonMargin)
                    top.linkTo(b3.top)
                    bottom.linkTo(b3.bottom)
                }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b5) {
                    //start.linkTo(b4.end, closeHorizonMargin)
                    top.linkTo(b4.top, horizonMargin)
                    bottom.linkTo(b4.bottom)
                    end.linkTo(endGuide)
                }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b6) {
                    top.linkTo(b5.bottom, horizonMargin)
                    end.linkTo(endGuide)

                }
        )
        /* ================== 2열 ==================================== */
        Image(
            painter = painterResource(R.drawable.hidden_msg),
            contentDescription = null,
            modifier = Modifier.constrainAs(b7) {
                top.linkTo(b6.bottom, horizonMargin)
                end.linkTo(b5.end)
            }.clickable {
                if(true){
                    message = randomMessage()
                }
            }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b8) {
                    top.linkTo(b7.top)
                    bottom.linkTo(b7.bottom)
                    end.linkTo(b4.end)
                }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b9) {
                    end.linkTo(b3.end)
                    start.linkTo(b3.start)
                    bottom.linkTo(b8.bottom)
                }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b10) {
                    end.linkTo(b2.end)
                    start.linkTo(b2.start)
                    bottom.linkTo(b9.bottom)
                }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b11) {
                    end.linkTo(b1.end)
                    start.linkTo(b1.start)
                    bottom.linkTo(b10.bottom, -horizonMargin)
                }
        )
        /* ================== 3열 ==================================== */
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b12) {
                    end.linkTo(b11.end)
                    start.linkTo(b11.start)
                    top.linkTo(b11.bottom, horizonMargin)
                }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b13) {
                    end.linkTo(b10.end)
                    start.linkTo(b10.start)
                    top.linkTo(b12.top, horizonMargin)
                }
        )
        Image(
            painter = painterResource(R.drawable.hidden_msg),
            contentDescription = null,
            modifier = Modifier.constrainAs(b14) {
                start.linkTo(b9.start)
                top.linkTo(b13.top)
                bottom.linkTo(b13.bottom)
            }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b15) {
                    start.linkTo(b8.start)
                    top.linkTo(b14.top)
                    bottom.linkTo(b14.bottom)
                }
        )

        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b16) {
                    //start.linkTo(b7.start)
                    top.linkTo(b15.top, horizonMargin)
                    end.linkTo(endGuide)
                }
        )
        /* ================== 4열 ==================================== */
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b17) {
                    //start.linkTo(b16.start)
                    top.linkTo(b16.bottom, horizonMargin)
                    end.linkTo(endGuide)
                }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b18) {
                    end.linkTo(b15.end)
                    top.linkTo(b17.top, horizonMargin)
                }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b19) {
                    top.linkTo(b18.top)
                    start.linkTo(b9.start)
                }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b20) {
                    top.linkTo(b19.top)
                    end.linkTo(b13.end)
                }
        )
        Image(
            painter = painterResource(R.drawable.hidden_msg),
            contentDescription = null,
            modifier = Modifier.constrainAs(b21) {
                top.linkTo(b20.bottom, -horizonMargin)
                end.linkTo(b12.end)
            }

        )

        /* ================== 5열 ==================================== */
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b22) {
                    start.linkTo(b21.start)
                    end.linkTo(b21.end)
                    top.linkTo(b21.bottom, horizonMargin)
                }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b23) {
                    start.linkTo(b20.start)
                    top.linkTo(b22.top, horizonMargin)
                }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b24) {
                    start.linkTo(b19.start)
                    top.linkTo(b23.top)
                }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b25) {
                    start.linkTo(b18.start)
                    top.linkTo(b24.top)

                }
        )
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b26) {
                    //start.linkTo(b17.start)
                    top.linkTo(b25.top, horizonMargin)
                    end.linkTo(endGuide)
                }
        )
        /* ================== 6열 ==================================== */
        Box(
            modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                .constrainAs(b27) {
                    //start.linkTo(b26.start)
                    top.linkTo(b26.bottom, horizonMargin)
                    end.linkTo(endGuide)
                }
        )




        if(date == 28){
            Image(
                painter = painterResource(R.drawable.destination),
                contentDescription = null,
                modifier = Modifier.constrainAs(b31) {
                    end.linkTo(b25.end)
                    top.linkTo(b27.top, horizonMargin)
                }
            )
        }else if(date == 29){
            Box(
                modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                    .constrainAs(b28) {
                        end.linkTo(b25.end)
                        top.linkTo(b27.top, horizonMargin)
                    }
            )
            Image(
                painter = painterResource(R.drawable.destination),
                contentDescription = null,
                modifier = Modifier.constrainAs(b31) {
                    end.linkTo(b24.end)
                    top.linkTo(b28.top)
                    bottom.linkTo(b28.bottom)
                }
            )
        }else if(date == 30){
            Box(
                modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                    .constrainAs(b28) {
                        end.linkTo(b25.end)
                        top.linkTo(b27.top, horizonMargin)
                    }
            )
            Image(
                painter = painterResource(R.drawable.hidden_msg),
                contentDescription = null,
                modifier = Modifier.constrainAs(b29) {
                    end.linkTo(b24.end)
                    top.linkTo(b28.top)
                    bottom.linkTo(b28.bottom)
                }
            )
            Image(
                painter = painterResource(R.drawable.destination),
                contentDescription = null,
                modifier = Modifier.constrainAs(b31) {
                    end.linkTo(b23.end)
                    top.linkTo(b28.top)
                }
            )
        }else{
            Box(
                modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                    .constrainAs(b28) {
                        end.linkTo(b25.end)
                        top.linkTo(b27.top, horizonMargin)
                    }
            )
            Image(
                painter = painterResource(R.drawable.hidden_msg),
                contentDescription = null,
                modifier = Modifier.constrainAs(b29) {
                    end.linkTo(b24.end)
                    top.linkTo(b28.top)
                    bottom.linkTo(b28.bottom)
                }
            )
            Box(
                modifier = Modifier.size(boxSize).background(color = boxColor, corner)
                    .constrainAs(b30) {
                        end.linkTo(b23.end)
                        top.linkTo(b28.top)
                    }
            )
            Image(
                painter = painterResource(R.drawable.destination),
                contentDescription = null,
                modifier = Modifier.constrainAs(b31) {
                    end.linkTo(b22.end)
                    top.linkTo(b28.top)
                }
            )
        }





    }
}
// 쿠키 랜덤
fun randomMessage(): String{
    val messages = HiddenMessage().msg
    val rd = Random.nextInt(messages.size)
    return messages[rd]
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun popupMessage(message: String){
    var dialog by remember { mutableStateOf(true) }

    if(dialog){
        BasicAlertDialog(
            onDismissRequest = {dialog = false}
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize(0.9f)
                    .padding(32.dp),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBE6)) // 노란색 배경
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = message, fontFamily = FontFamily(Font(R.font.gmarketsans_medium)), fontSize = 10.sp)
                }
                Spacer(Modifier.height(16.dp))
                Button(onClick = { dialog = false }) {
                    Text("닫기")
                }
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
