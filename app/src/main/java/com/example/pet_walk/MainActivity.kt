package com.withwalk.app.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.withwalk.app.R
import com.example.pet_walk.ui.Screen
import com.withwalk.app.ui.screen.chart.ChartToday
import com.withwalk.app.ui.screen.homepage.HomeScreen
import com.withwalk.app.ui.screen.login.CheckNumberScreen
import com.withwalk.app.ui.screen.login.CompleteScreen
import com.withwalk.app.ui.screen.login.DeleteCompleteScreen
import com.withwalk.app.ui.screen.login.DeleteScreen
import com.withwalk.app.ui.screen.login.FindPasswordScreen
import com.withwalk.app.ui.screen.login.GuideScreen
import com.withwalk.app.ui.screen.login.LoginScreen
import com.withwalk.app.ui.screen.login.PasswordResetScreen
import com.withwalk.app.ui.screen.login.RegistScreen
import com.withwalk.app.ui.screen.login.ResetCompleteScreen
import com.withwalk.app.ui.screen.login.SignupScreen
import com.withwalk.app.ui.screen.setting.AccountUpdateScreen
import com.withwalk.app.ui.screen.setting.SettingScreen
import com.withwalk.app.ui.screen.walk.WalkActivity
import com.withwalk.app.ui.theme.PetWalkTheme
import com.withwalk.app.ui.theme.nav_inactive
import com.withwalk.app.ui.theme.navigation
import com.withwalk.app.ui.theme.point_red
import com.withwalk.app.ui.theme.sub_main
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.example.pet_walk.ui.screen.attendance.AttendanceScreen
import com.example.pet_walk.core.TokenManager
import com.withwalk.app.ui.theme.black
import com.withwalk.app.ui.theme.middle_grey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val locationPermission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACTIVITY_RECOGNITION
    )
    private fun checkPermission(): Boolean {
        return locationPermission.all {
            ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }
    private fun checkAndRequestPermissions(): Boolean {
        if (checkPermission()) {
            return true
        } else {
            permissionLauncher.launch(locationPermission)
            return false
        }
    }
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.values.all { it }
            if (granted) {
                Toast.makeText(this, "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "앱 설정에서 권한을 허용해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    fun requestCheckAndRequestPermissions(): Boolean{
        if(checkAndRequestPermissions()) return true
        else return false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Pet_walk)
        super.onCreate(savedInstanceState)

        val startRoute = intent.getStringExtra("startDestination") ?: "auth"
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent { PetWalkTheme { RootNav(startRoute) } }
    }
    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val tokenManager = TokenManager(this@MainActivity)
            val token = tokenManager.getToken()

            val currentRoute = intent.getStringExtra("startDestination")

            if (token.isNullOrEmpty() && currentRoute != "auth") {
                val intent = Intent(this@MainActivity, MainActivity::class.java).apply {
                    putExtra("startDestination", "auth")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        }
    }
}

@Composable
fun RootNav(startRoute: String){
    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = black,
            darkIcons = false
        )
    }

    Scaffold(
        bottomBar = {
            val current = currentRoute(navController)
            if (current in listOf(Screen.Home.route, Screen.Chart.route, Screen.Setting.route, Screen.Attendance.route)) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startRoute,
            modifier = Modifier.padding(innerPadding)
        ){
            authNav(navController)
            mainNav(navController)
        }
    }
}

fun NavGraphBuilder.authNav(navController: NavController){
    navigation(
        startDestination = Screen.Login.route,
        route = "auth"
    ){
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Signup.route) { SignupScreen(navController) }
        composable(Screen.Regist.route) {
            val scroll = rememberScrollState()
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scroll)
            ) {
                RegistScreen(navController){
                    navController.navigate(Screen.Login.route)
                }
            }
        }
        composable("${Screen.Complete.route}/{dogName}/{dogProfile}") { backStackEntry ->
            val dogName = backStackEntry.arguments?.getString("dogName") ?: ""
            val dogProfile = backStackEntry.arguments?.getString("dogProfile") ?: "error"
            CompleteScreen(navController, dogName, dogProfile)
        }
        composable(Screen.Guide.route) {
            val scroll = rememberScrollState()
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scroll)
            ) { GuideScreen(navController)  }
        }
        composable(Screen.FindPassword.route) { FindPasswordScreen(navController) }
        composable(Screen.DeletetComplete.route) { DeleteCompleteScreen(navController) }

    }
}
fun NavGraphBuilder.mainNav(navController: NavController){
    navigation(
        startDestination = Screen.Home.route,
        route = "main"
    ){
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Chart.route) {ChartToday()
            val scroll = rememberScrollState()
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)) {
                ChartToday()
            }
        }
        composable(Screen.Attendance.route) {
            val scroll = rememberScrollState()
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scroll)
            ) { AttendanceScreen()  }
        }
        composable(Screen.Setting.route) {
            val scroll = rememberScrollState()
            Column(
                modifier = Modifier.fillMaxSize().background(sub_main).verticalScroll(scroll)
            ) { SettingScreen(navController)  }

        }
        composable(Screen.Walk.route) {
            val context = LocalContext.current
            val intent = Intent(context, WalkActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)

        }
        composable(Screen.AccountUpdate.route) {
            val scroll = rememberScrollState()
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = sub_main
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(scroll)
                ) { AccountUpdateScreen(navController) }
            }
        }
        composable(Screen.Unsubscribe.route) { DeleteScreen(navController) }
        composable(Screen.CheckNumber.route) { CheckNumberScreen(navController) }
        composable(Screen.PasswordReset.route) { PasswordResetScreen(navController) }
        composable(Screen.ResetComplete.route) { ResetCompleteScreen(navController) }

    }
}

@Composable
fun BottomNavBar(navController: NavController){
    val items = listOf(Screen.Home, Screen.Chart, Screen.Attendance, Screen.Setting)
    val currentRoute = currentRoute(navController)

    NavigationBar(
        modifier = Modifier.height(120.dp),
        containerColor = navigation
    ) {
        items.forEach { screen ->
            val icon = when(screen){
                Screen.Home -> R.drawable.home_icon
                Screen.Attendance -> R.drawable.today_icon
                Screen.Setting -> R.drawable.setting_icon
                else -> R.drawable.chart_icon
            }
            /* 아이콘 커스텀 */
            NavigationBarItem(
                icon = { Icon(
                    painter = painterResource(id = icon),
                    contentDescription = screen.label,
                    modifier = Modifier.size(25.dp)
                ) },
                label = { Text(screen.label, fontSize = 11.sp, lineHeight = 11.sp.times(0.9f)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = point_red,
                    unselectedIconColor = nav_inactive,
                    selectedTextColor = point_red,
                    unselectedTextColor = nav_inactive,
                    indicatorColor = Color(0x33FFE3AD)
                ),
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route){
                        // 스택 중복 방지
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

// 현재 선택된 route확인
@Composable
fun currentRoute(navController: NavController): String?{
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
@Composable
fun introText(text: String, modifier: Modifier){
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = middle_grey,
        modifier = modifier
    )
}
