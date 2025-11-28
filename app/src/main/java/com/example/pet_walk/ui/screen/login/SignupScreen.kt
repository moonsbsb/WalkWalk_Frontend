
package com.withwalk.app.ui.screen.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.withwalk.app.ui.theme.error_
import com.withwalk.app.ui.theme.main
import com.withwalk.app.ui.theme.middle_grey
import com.withwalk.app.ui.theme.white
import com.withwalk.app.R
import com.withwalk.app.Screen
import com.withwalk.app.ui.component.CustomLabelText
import com.withwalk.app.ui.component.CustomUserTextField
import com.withwalk.app.ui.theme.PetWalkTheme

@Preview
@Composable
fun prevSignupScreen(){
    var navController = rememberNavController()
    PetWalkTheme { SignupScreen(navController) }
}
@Composable
fun SignupScreen(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = white)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordCheck by remember { mutableStateOf("") }




        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            color = main
        )
        CustomLabelText("아이디", modifier = Modifier.align(Alignment.Start))
        CustomUserTextField(
            value = email, onValueChange = { email = it }, placeholder = "이메일을 입력하세요"
        )

        val emailform = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
        if(email.isEmpty() || emailform.matches(email)){
            Text(text = "입력하신 이메일은 위드워크의 아이디와 함께 비밀번호 찾기에 이용됩니다.",style = MaterialTheme.typography.labelSmall, color = middle_grey, modifier = Modifier
                .weight(0.3f)
                .align(Alignment.Start))
        }else{
            Text(text = "이메일의 형식이 알맞지않습니다.",style = MaterialTheme.typography.labelSmall, color = error_, modifier = Modifier
                .weight(0.3f)
                .align(Alignment.Start))
        }

        CustomLabelText("비밀번호", modifier = Modifier.align(Alignment.Start))
        CustomUserTextField(
            value = password, onValueChange = { password = it }, placeholder = stringResource(R.string.password_check)
        )
        CustomUserTextField(
            value = passwordCheck, onValueChange = { passwordCheck = it }, placeholder = stringResource(R.string.password_check_add)
        )
        Box(
            modifier = Modifier
                .weight(2f)
                .align(Alignment.Start)
        ) {
            if (password != passwordCheck) {
                Text(
                    text = stringResource(R.string.password_wrong),
                    style = MaterialTheme.typography.labelSmall,
                    color = error_
                )
            } else {
                Text("")
            }
        }

        navController.currentBackStackEntry?.savedStateHandle?.set("userEmail", email)
        navController.currentBackStackEntry?.savedStateHandle?.set("userPassword", password)
        val context = LocalContext.current
        IconButton(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(color = main, RoundedCornerShape(5.dp)),
            onClick = {
                if(password==passwordCheck && emailform.matches(email) && password!="") {
                    navController.navigate(Screen.Regist.route)
                    val sharedPref = context.getSharedPreferences("email_pref", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("store_email", email).apply()
                    sharedPref.edit().putBoolean("store_check", false).apply()
                }
                if(email=="")Toast.makeText(context, "이메일을 입력해주세요",Toast.LENGTH_SHORT).show()
                if(password=="" || passwordCheck=="")Toast.makeText(context, "비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show()
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


