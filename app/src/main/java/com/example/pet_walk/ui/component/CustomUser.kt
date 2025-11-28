package com.withwalk.app.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.unit.dp
import com.withwalk.app.ui.theme.light_grey
import com.withwalk.app.ui.theme.middle_grey
import com.withwalk.app.ui.theme.white

/* 공통 텍스트필드 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomUserTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
){
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .border(width = 1.dp, color = middle_grey, shape = RoundedCornerShape(5.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedLabelColor = light_grey,
            unfocusedPlaceholderColor = light_grey
        ),
        textStyle = MaterialTheme.typography.labelMedium,
        singleLine = true,
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomUserDogField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
){
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        ) },
        modifier = modifier
            .fillMaxWidth()
            .height(53.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = white,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedLabelColor = light_grey,
            unfocusedPlaceholderColor = light_grey
        ),
        textStyle = MaterialTheme.typography.labelMedium,
        shape = RoundedCornerShape(90.dp),
        singleLine = true
    )
}
/* 입력 앞머리 텍스트 */
@Composable
fun CustomLabelText(text: String, modifier: Modifier){
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = middle_grey,
        modifier = modifier
    )
}
