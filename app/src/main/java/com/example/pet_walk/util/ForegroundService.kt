package com.withwalk.app.util

import android.annotation.SuppressLint
import android.app.Service;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager
import android.content.Context
import android.content.Intent;
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kakao.vectormap.LatLng
import com.withwalk.app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Collections.emptyList

class ForegroundService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private val _pathFlow = MutableStateFlow<List<LatLng>>(emptyList())
    val pathFlow: StateFlow<List<LatLng>> = _pathFlow

    private val _distanceFlow = MutableStateFlow(0f)
    val distanceFlow: StateFlow<Float> = _distanceFlow

    private val _timeFlow = MutableStateFlow(0L)
    val timeFlow: StateFlow<Long> = _timeFlow

    private var _stepFlow = MutableStateFlow(0L)
    val stepFlow: StateFlow<Long> = _stepFlow
    private var initialStep = -1f

    private var _slowStep = MutableStateFlow(0f)
    val slowStep : StateFlow<Float> = _slowStep
    private var _nomalStep = MutableStateFlow(0f)
    val nomalStep : StateFlow<Float> = _nomalStep

    private var startTime = 0L
    private var timerJob: Job? = null
    var pathRoute = mutableListOf<LatLng>()
    var timeToDistance = mutableListOf<Long>()

    private var _pauseTime = MutableStateFlow(0L)
    val pauseTime: StateFlow<Long> = _pauseTime
    var _pauseDistance = MutableStateFlow(0f)
    var _pauseStep =MutableStateFlow(0L)
    var _pauseSlowStep = MutableStateFlow(0f)
    var _pauseNomalStep = MutableStateFlow(0f)
    var _pauseRoute = mutableSetOf<Int>()
    var isTracking = false

    private val sensorManager by lazy { getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val sensor: Sensor? by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) }

    /* 걸음 수 측정 */
    private val stepListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent){
            // 걸음 센서 이벤트 발생 시
            if(event.sensor.type == Sensor.TYPE_STEP_COUNTER){
                if(initialStep < 0f) initialStep = event.values[0]
                _stepFlow.value = (event.values[0] - initialStep).toLong().coerceAtLeast(0L) + _pauseStep.value
            }
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    override fun onCreate() {
        super.onCreate()
        if(sensor == null) Toast.makeText(this, "Step Sensor가 없습니다.", Toast.LENGTH_SHORT).show()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L).build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                for (loc in result.locations) {
                    val latlng = LatLng.from(loc.latitude, loc.longitude)
                    pathRoute.add(latlng)
                    timeToDistance.add(loc.time)

                    _distanceFlow.value = calculateDistance(pathRoute, timeToDistance) //+ _pauseDistance.value
                    _pathFlow.value = pathRoute.toList()
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createNotification())
        startLocationUpdates()
        startTimer()
        if (sensor == null) {
            Log.d("센서확인", "걸음 센서가 없습니다")
        } else {
            Log.d("센서확인", "걸음 센서 있음, 권한 체크 필요")
            sensorManager.registerListener(stepListener, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        }
        return START_STICKY
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "tracking_channel",
                "Tracking Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
    private fun createNotification(): Notification{
        createNotificationChannel()

        val builder = Notification.Builder(this, "tracking_channel")
            .setContentText("산책 중")
            .setContentTitle("위치를 계산하고있어요..")
            .setSmallIcon(R.drawable.test_small_icon)
            .setOngoing(true)
        return builder.build()
    }
    /* 위치 업데이트 요청 */
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun startTimer() {
        if (isTracking) return
        isTracking = true

        startTime = System.currentTimeMillis()
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while(isActive){
                _timeFlow.value = System.currentTimeMillis() - startTime + pauseTime.value
                delay(1000)
            }
        }
    }

    private fun calculateDistance(route: List<LatLng>, time: List<Long>): Float {
        var total = 0f

        var slowVal = _pauseSlowStep.value
        var nomalVal = _pauseNomalStep.value

        for(i in 0 until route.size-1){
            if(_pauseRoute.contains(i)) continue

            val first = route[i]
            val second = route[i+1]
            val firstTime = time[i]
            val secondTime = time[i+1]

            val result = FloatArray(1)
            Location.distanceBetween(
                first.latitude, first.longitude,
                second.latitude, second.longitude,
                result
            )
            val segmentDistance = result[0]
            total+=segmentDistance

            val segmentTime = (secondTime-firstTime) / 1000f

            if(segmentTime > 0f){
                val speed = segmentDistance / segmentTime
                if(speed < 0.9f)
                    slowVal += segmentTime
                    //_slowStep.value += segmentTime + _pauseSlowStep.value
                else
                    nomalVal += segmentTime
                    //_nomalStep.value += segmentTime + _pauseNomalStep.value
            }
        }
        val distanceVlaue = total
        _slowStep.value = slowVal
        _nomalStep.value = nomalVal
        return distanceVlaue
    }

    override fun onBind(intent: Intent?): IBinder? = WalkBinder()
    inner class WalkBinder : Binder() {
        fun getService(): ForegroundService = this@ForegroundService
    }
    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        timerJob?.cancel()
        sensorManager.unregisterListener(stepListener)
    }
    fun stopTarcking(){
        if(!isTracking) return
        isTracking = false

        val time = System.currentTimeMillis() - startTime
        _pauseTime.value += time

        _pauseDistance.value = _distanceFlow.value
        _pauseStep.value = _stepFlow.value
        _pauseSlowStep.value = _slowStep.value
        _pauseNomalStep.value = _nomalStep.value

        val lastIdx = if(pathRoute.isNotEmpty()) pathRoute.size-1 else -1
        if(lastIdx >= 0) _pauseRoute.add(lastIdx)

        _timeFlow.value = _pauseTime.value

        fusedLocationClient.removeLocationUpdates(locationCallback) // 위치 업데이트 중지
        timerJob?.cancel()
        sensorManager.unregisterListener(stepListener)
    }
    @SuppressLint("MissingPermission")
    fun restartTracking(){
        startLocationUpdates()  // 위치 업데이트 요청
        startTimer()
        if (sensor != null) {
            sensorManager.registerListener(stepListener, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }
}
