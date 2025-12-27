package com.withwalk.app.ui.screen.walk

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.withwalk.app.R
import com.withwalk.app.ui.component.ProfileImageViewModel
import com.withwalk.app.util.ForegroundService
import kotlinx.coroutines.flow.MutableStateFlow

class WalkViewModel(private val service: ForegroundService): ViewModel() {
    val distance = service.distanceFlow
    var time = service.timeFlow
    val path = service.pathFlow
    val step = service.stepFlow
    val slowStep = service.slowStep
    val nomalStep = service.nomalStep


    /* 위치 권한 */
    private val _walkLocationPermission = MutableStateFlow(false)
    val walkLocationPermission: Boolean = _walkLocationPermission.value

    fun onPermissionResult(grated: Boolean){
        _walkLocationPermission.value = grated
    }

    fun timeFormat(time: Long): String{
        val formatTime = time / 1000
        val h = formatTime / 3600
        val m = (formatTime % 3600) / 60
        val s = formatTime % 60
        return String.format("%02d:%02d:%02d", h, m, s)
    }
    fun timeFormatForRequest(time: Long): String{
        val formatTime = time / 1000
        val h = formatTime / 3600
        val m = (formatTime % 3600) / 60
        val s = formatTime % 60
        return String.format("%02d:%02d:%02d", h, m, s)
    }
    fun nomalStepFormat(time: Float): String{
        val m = (time / 60f).toInt()
        return m.toString()
    }
    fun calculateSlowStep(nomalTime: String, time: Long): String{
        val totalMinutes = ((time / 1000) / 60).toInt()
        val normalMinutes = nomalTime.toIntOrNull() ?: 0 // 정상 걸음 시간(분)

        val slowMinutes = (totalMinutes - normalMinutes).coerceAtLeast(0)

        return slowMinutes.toString()
    }
    fun comment(time: Long, nomalTime: String, slowTime: String): String{
        val _formatTime = time / 1000
        val formatTime = (_formatTime/60).toInt()

        val nomalM = nomalTime.toIntOrNull() ?: 0
        val slowM = slowTime.toIntOrNull() ?: 0

        val ratio = if (nomalM > 0) (formatTime.toDouble() / nomalM * 100) else 0.0

        return when {
            ratio >= 60 -> "# 멈추지않는 강아지"
            ratio <= 55 -> "# 여유로운 강아지"
            else -> "# 반반 강아지"
        }
    }
}