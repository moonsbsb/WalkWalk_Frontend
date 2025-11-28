package com.withwalk.app.ui.screen.setting

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.withwalk.app.ui.theme.light_grey
import com.withwalk.app.ui.theme.main
import com.withwalk.app.ui.theme.middle_grey
import com.withwalk.app.ui.theme.point_red
import com.withwalk.app.ui.theme.sub_main
import com.withwalk.app.ui.theme.third_main
import com.withwalk.app.ui.theme.white
import com.withwalk.app.R
import com.withwalk.app.Screen
import com.withwalk.app.api.TokenManager
import com.withwalk.app.api.model.DogRequest
import com.withwalk.app.api.model.UpdateUserRequest
import com.withwalk.app.data.Repository.AuthRepository
import com.withwalk.app.ui.component.CustomUserTextField
import com.withwalk.app.ui.screen.login.AuthViewModel
import com.withwalk.app.ui.screen.login.AuthViewModelFactory
import com.withwalk.app.ui.screen.login.DatePickerModal
import com.withwalk.app.ui.screen.login.SelectImage
import com.withwalk.app.ui.theme.PetWalkTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Preview
@Composable
private fun prevAccountUpdateScreen(){
    val navController = rememberNavController()
    PetWalkTheme {
        AccountUpdateScreen(navController)
    }
}
@Composable
fun AccountUpdateScreen(navController: NavController){
    val repository = AuthRepository()
    val factory = AuthViewModelFactory(repository)
    val viewModel: AuthViewModel = viewModel(factory = factory)

    val tokenManager = TokenManager(LocalContext.current)
    val token = tokenManager.getToken()!!

    LaunchedEffect(Unit) { viewModel.getUser(token) }
    val dog by viewModel.user.collectAsState()

    var name by remember { mutableStateOf(dog.name) }
    var weight by remember { mutableStateOf("${dog.weight}") }
    var age by remember { mutableStateOf("${dog.age}") }
    var birth by remember { mutableStateOf(dog.birth) }
    var img by remember { mutableStateOf(dog.img) }

    LaunchedEffect(dog) {
        name = dog.name
        weight = "${dog.weight}"
        age = "${dog.age}"
        birth = dog.birth
        img = dog.img
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = sub_main)
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.padding(top = 15.dp)
        ) {
            Image(painter = painterResource(R.drawable.back), contentDescription = null, modifier = Modifier.clickable { navController.popBackStack() })
            Text(
                text = stringResource(R.string.update_account),
                color = main,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(start = 15.dp)
            )
        }


        SelectImage(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 20.dp),
            onSelect = { img = it }
        )

        category(stringResource(R.string.name))
        CustomUserTextField(
            value = name, onValueChange = { name = it }, placeholder = dog.name
        )

        category(stringResource(R.string.weight))
        CustomUserTextField(
            value = "${weight}", onValueChange = { weight = it }, placeholder = "${dog.weight}"
        )

        category(stringResource(R.string.age))
        CustomUserTextField(
            value = "${age}", onValueChange = { age = it }, placeholder = "${dog.age}"
        )

        category(stringResource(R.string.birth))
        var show by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp)
                .background(color = white)
                .clickable { show = true }
                .border(width = 1.dp, color = middle_grey, shape = RoundedCornerShape(5.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = if (birth.isNotEmpty()) birth else "생일을 선택하세요",
                color = if (birth.isNotEmpty()) Color.Black else light_grey,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        if (show) {
            DatePickerModal(
                onDateSelected = { millis ->
                    millis?.let {
                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        birth = formatter.format(Date(it))
                    }
                },
                onDismiss = { show = false }
            )
        }

        Spacer(Modifier.height(20.dp))

        IconButton(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(color = point_red, RoundedCornerShape(5.dp)),
            onClick = {
                val parseAge = age.toIntOrNull() ?: 0
                val parseWeight = weight.toFloatOrNull() ?: 0f
                val dog = DogRequest(name, parseAge, parseWeight, birth, img)
                val request = UpdateUserRequest(dog)
                viewModel.postUserUpdate(token, request)
                navController.navigate(Screen.Home.route)
            }
        ) {
            Text(
                text = "확인",
                color = white,
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }


}
@Composable
fun category(text: String){
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = middle_grey,
        modifier = Modifier.padding(top = 15.dp, bottom = 5.dp)
    )
}
