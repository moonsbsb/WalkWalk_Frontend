package com.withwalk.app.ui.screen.today

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.withwalk.app.ui.theme.dark_grey
import com.withwalk.app.ui.theme.point_green
import com.withwalk.app.ui.theme.white
import com.withwalk.app.R

@Preview
@Composable
fun GalleryScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = white)
    ) {
        Text(
            text = stringResource(id = R.string.today_pic),
            style = MaterialTheme.typography.headlineMedium,
            color = dark_grey,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(20.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .aspectRatio(1f)
                .border(
                    width = 1.dp,
                    color = dark_grey
                )
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
                .clickable { /* 사진 등록 */ }
        ) {
            //GalleryImagePicker()
        }
        Text(
            text = stringResource(id = R.string.pics),
            style = MaterialTheme.typography.headlineMedium,
            color = dark_grey,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(20.dp)
        )
        /* 사진 이미지 리스트에 저장 후 함수 출력 */
        val list = arrayListOf<Int>(1, 2, 3, 4, 5)
        LazyImage(list)
    }
}
@Composable
fun LazyImage(items: List<Int>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 100.dp), // 화면 크기에 맞춰 개수 조절
        //columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items.size){ index ->
            Box (
                modifier = Modifier
                    .aspectRatio(1f)
                    .background(point_green)
            ){
                Image(
                    painter = painterResource(R.drawable.dog1),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
@Composable
fun GalleryImagePicker(selected: () -> Unit){
    val contect = LocalContext.current
    
}