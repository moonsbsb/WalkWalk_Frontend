package com.withwalk.app.ui.screen.login

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.withwalk.app.ui.theme.dark_grey
import com.withwalk.app.ui.theme.light_grey
import com.withwalk.app.ui.theme.main
import com.withwalk.app.ui.theme.middle_grey
import com.withwalk.app.ui.theme.point_red
import com.withwalk.app.ui.theme.sub_main
import com.withwalk.app.ui.theme.white
import com.withwalk.app.R
import com.withwalk.app.Screen
import com.withwalk.app.api.model.AuthRequest
import com.withwalk.app.api.model.DogRequest
import com.withwalk.app.data.Repository.AuthRepository
import com.withwalk.app.ui.component.CustomLabelText
import com.withwalk.app.ui.component.CustomUserDogField
import com.withwalk.app.ui.component.ProfileImageViewModel
import com.withwalk.app.ui.theme.PetWalkTheme
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Preview
@Composable
fun preRegistScreen(){
    val navController = rememberNavController()
    PetWalkTheme {
        RegistScreen(navController)
    }
}


@Composable
fun RegistScreen(navController: NavController){
    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
    val userEmail = savedStateHandle?.get<String>("userEmail") ?: ""
    val userPassword = savedStateHandle?.get<String>("userPassword") ?: ""

    Log.d("카카오", "넘겨받은 이메일 확인: $userEmail")

    val repository = AuthRepository()
    val factory = AuthViewModelFactory(repository)
    val viewModel: AuthViewModel = viewModel(factory = factory)

    val message by viewModel.message.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = sub_main)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        Spacer(modifier = Modifier.weight(0.01f))
        Text(
            text = stringResource(R.string.introduce),
            style = MaterialTheme.typography.displayMedium,
            color = main,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(0.4f))
        var img by remember { mutableStateOf("") }
        SelectImage(
            modifier = Modifier.fillMaxWidth(1f),
            onSelect = { img = it }
        )

        // 이름
        var dogName by remember { mutableStateOf("") }
        CustomLabelText(stringResource(R.string.name), modifier = Modifier.fillMaxWidth())
        CustomUserDogField(
            value = dogName,
            onValueChange = { dogName = it },
            label = stringResource(R.string.name_check),
            modifier = Modifier.fillMaxWidth()
        )

        // 몸무게
        var dogWeight by remember { mutableStateOf("") }
        CustomLabelText(stringResource(R.string.weight), modifier = Modifier.fillMaxWidth())
        CustomUserDogField(
            value = dogWeight,
            onValueChange = { input ->
                if(input.all { it.isDigit() }){
                    dogWeight = input
                }
            },
            label = stringResource(R.string.weight_check),
            modifier = Modifier.fillMaxWidth()
        )

        // 나이
        var dogAge by remember { mutableStateOf("") }
        CustomLabelText(stringResource(R.string.age), modifier = Modifier.fillMaxWidth())
        CustomUserDogField(
            value = dogAge,
            onValueChange = {input ->
                if(input.all{it.isDigit()}){
                    dogAge = input
                }
            },
            label = stringResource(R.string.age_check),
            modifier = Modifier.fillMaxWidth()
        )

        // 생일
        var dogBirth by remember { mutableStateOf("") }
        var show by remember { mutableStateOf(false) }
        CustomLabelText(stringResource(R.string.birth), modifier = Modifier.fillMaxWidth())
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 50.dp)
                .background(color = white, shape = RoundedCornerShape(90.dp))
                .clickable { show = true }
                .padding(start = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = if(dogBirth.isEmpty()) stringResource(R.string.birth_check) else dogBirth,
                style = MaterialTheme.typography.labelMedium,
                color = if(dogBirth.isEmpty()) light_grey else Color.Black
            )
        }

        if (show) {
            DatePickerModal(
                onDateSelected = { millis ->
                    millis?.let {
                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        dogBirth = formatter.format(Date(it))
                    }
                    show = false
                },
                onDismiss = { show = false }
            )
        }

        // 개인정보 동의
        var privacyCheck by remember { mutableStateOf(false) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Checkbox(
                checked = privacyCheck,
                onCheckedChange = { privacyCheck = it },
                colors = CheckboxDefaults.colors(
                    checkmarkColor = main,
                    uncheckedColor = middle_grey,
                    checkedColor = sub_main
                ),
            )
            Text(
                text = stringResource(R.string.privacy_confirm),
                style = MaterialTheme.typography.labelMedium,
                color = dark_grey
            )
            TextButton(onClick = { navController.navigate(Screen.Guide.route) }) {
                Text(
                    text="자세히",
                    style = MaterialTheme.typography.labelSmall,
                    color = middle_grey,
                    textDecoration = TextDecoration.Underline
                )
            }
        }



        // 완료 버튼
        val context = LocalContext.current
        val lieBirth = try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val birthDate = LocalDate.parse(dogBirth, formatter)
            birthDate.isBefore(LocalDate.now())
        } catch (e: Exception) {
            false
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 50.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = point_red),
            onClick = {
                val errorMsg = when {
                    img.isEmpty() -> "강아지의 프로필을 설정해주세요"
                    dogName.isEmpty() -> "강아지의 이름을 설정해주세요"
                    dogWeight.isEmpty() -> "강아지의 몸무게를 설정해주세요"
                    dogAge.isEmpty() -> "강아지의 나이를 설정해주세요"
                    dogBirth.isEmpty() -> "강아지의 생일 설정해주세요"
                    !privacyCheck -> "개인정보 수집 및 이용을 동의해주세요"
                    !lieBirth -> "정확한 출생일을 입력해주세요"
                    else -> null
                }
                if(errorMsg != null) {
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                } else {
                    val request = AuthRequest(
                        email = userEmail,
                        password = userPassword,
                        name = "ㅇㅇ",
                        dogs = listOf(
                            DogRequest(
                                name = dogName,
                                age = dogAge.toIntOrNull() ?: 0,
                                weight = dogWeight.toFloatOrNull() ?: 0f,
                                birth = dogBirth,
                                img = img
                            )
                        )
                    )
                    viewModel.postAuth(request)
                    if(message == "Success"){
                        navController.navigate("${Screen.Complete.route}/$dogName/$img"){ popUpTo(0) }
                    } else if(message.isNotEmpty()){
                        Toast.makeText(context, "이메일이 중복되었습니다.", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "한 번 더 눌러주세요!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        ){
            Text(
                text = "소개완료",
                color = white,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.weight(0.05f))
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun SelectImage(onSelect: (String) -> Unit, modifier: Modifier){
    val profileImageViewModle: ProfileImageViewModel = viewModel()
    val kind = profileImageViewModle.dogKind
    var state = profileImageViewModle.SeletedProfile

    LazyRow(
        modifier = modifier,
    ) {
        items(kind.toList()){(key, image) ->
            Column(
                modifier = Modifier.padding(start = 10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                IconButton (
                    onClick = {
                        state.keys.forEach { state[it] = false }
                        state[key] = true
                        onSelect(key)
                    },
                    modifier = Modifier
                        .size(220.dp)
                        .clip(RoundedCornerShape(8.dp)),
                ) {
                    Image(
                        painter = painterResource(image),
                        contentDescription = null,
                        modifier = Modifier.size(180.dp),
                    )
                }
            }
        }
    }
    //return img.toString()
}

@Preview
@Composable
fun preCompleteScreen(){
    val navController = rememberNavController()
    PetWalkTheme { CompleteScreen(navController, "", "") }
}
@Composable
fun CompleteScreen(navController: NavController, dogname: String, dogProfile: String){

    val profileViewModel: ProfileImageViewModel = viewModel()
    val dogImg = profileViewModel.dogKind[dogProfile] ?: R.drawable.transparent

    Column (
        modifier = Modifier
            .background(color = main)
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = dogImg),
            contentDescription = null,
            modifier = Modifier
                .height(300.dp)
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "${dogname}! 환영해요",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayLarge,
            color = white,
        )
        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(color = point_red, RoundedCornerShape(5.dp)),
            onClick = {navController.navigate(Screen.Login.route){popUpTo(0)} }
        ) {
            Text(
                text = "확인",
                color = white,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
){
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}