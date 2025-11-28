package com.withwalk.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.withwalk.app.R

@Composable
fun PetWalkTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = AppTypography(),
        content = content
    )
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
}
@Composable
fun AppTypography(): Typography {
    return Typography(
        /* 텍스트 강조 */
        displayLarge = TextStyle(
            fontSize = 35.sp,
            fontFamily = FontFamily(Font(R.font.gmarketsan_bold))
        ),

        /* 로고 */
        displayMedium = TextStyle(
            fontSize = 45.sp,
            fontFamily = FontFamily(Font(R.font.gmarketsan_bold))
        ),

        /* 숫자 강조 */
        headlineLarge = TextStyle(
            fontSize = 35.sp,
            fontFamily = FontFamily(Font(R.font.notosanskr_bold))
        ),

        /* 일반 텍스트 */
        bodyMedium = TextStyle(
            fontSize = 22.sp,
            fontFamily = FontFamily(Font(R.font.gmarketsans_medium))
        ),

        labelLarge = TextStyle(
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.notosanskr_bold))
        ),

        labelMedium = TextStyle(
            fontSize = 13.sp,
            fontFamily = FontFamily(Font(R.font.gmarketsans_medium))
        ),

        labelSmall = TextStyle(
            fontSize = 10.sp,
            fontFamily = FontFamily(Font(R.font.gmarketsans_light))
        ),

        displaySmall = TextStyle(
            fontSize = 9.sp,
            fontFamily = FontFamily(Font(R.font.gmarketsans_medium))
        ),

        /* 갤러리 폰트 */
        headlineMedium = TextStyle(
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.noto_sans))
        )
    )
}
