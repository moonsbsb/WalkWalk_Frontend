package com.withwalk.app.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.withwalk.app.ui.theme.white

/* 공통 버튼 함수 */
@Composable
fun CustomRecBtn(navController: NavController, route: String?, text: String, color: Color){
    IconButton(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(color = color, RoundedCornerShape(5.dp)),
        onClick = {navController.navigate("${route}")}
    ) {
        Text(
            text = text,
            color = white,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
@Composable
// 적갈색 버튼 구현
fun darkRoundBtn(text: String, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier.clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = white,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}