package com.withwalk.app.ui.screen.walk

import com.withwalk.app.R
import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.kakao.vectormap.*
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.graphics.gl.GLSurfaceView
import com.kakao.vectormap.label.*
import com.kakao.vectormap.route.RouteLine
import com.kakao.vectormap.route.RouteLineLayer
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import com.withwalk.app.databinding.ActivityWalkBinding
import com.withwalk.app.ui.screen.homepage.HomeScreen
import com.withwalk.app.ui.theme.PetWalkTheme
import com.withwalk.app.util.ForegroundService
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.opengles.GL10
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WalkActivity : AppCompatActivity() {

    private val binding by lazy { ActivityWalkBinding.inflate(layoutInflater) }
    private lateinit var mapView: MapView
    private lateinit var kakaoMap: KakaoMap

    private var centerLabel: Label? = null
    private lateinit var styleSet: RouteLineStylesSet
    private lateinit var routeLine: RouteLine

    private val locationPermission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACTIVITY_RECOGNITION
    )
    private var service: ForegroundService? = null
    private lateinit var walkViewModel: WalkViewModel
    private var isBind = false

    private var isMapInitialized = false

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.values.all { it }
            if (granted) {
                Toast.makeText(this, "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
                initMap()
            } else {
                Toast.makeText(this, "앱 설정에서 권한을 허용해주세요", Toast.LENGTH_SHORT).show()
                finish()
            }
        }


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = (binder as ForegroundService.WalkBinder).getService()
            walkViewModel = WalkViewModel(service!!)
            observeServiceFlows()
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mapView = binding.kakaoMapView

        styleSet = RouteLineStylesSet.from(
            "orangeStyle",
            RouteLineStyles.from(RouteLineStyle.from(16f, Color.rgb(255, 171, 35)))
        )
        checkAndRequestPermissions()

        initClickListener()

        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callBack)
    }

    private fun checkAndRequestPermissions() {
        if (checkPermission()) {
            initMap()
        } else {
            permissionLauncher.launch(locationPermission)
        }
    }

    private fun initClickListener() {
        // 시작 버튼
        binding.play.setOnClickListener {
            binding.play.visibility = View.GONE
            binding.stop.visibility = View.VISIBLE

            startForegroundService(Intent(this, ForegroundService::class.java))
            isBind = true

            if(service == null) {
                bindService(Intent(this, ForegroundService::class.java), connection, Context.BIND_AUTO_CREATE)
            }else{
                service?.restartTracking()
            }
        }

        // 멈춤 버튼
        binding.stop.setOnClickListener {
            binding.stop.visibility = View.GONE
            binding.play.visibility = View.VISIBLE
            service?.stopTarcking()
            isBind = false
        }

        // 끝내기 버튼
        binding.finish.setOnClickListener {
            service?.stopTarcking()
            stopService(Intent(this, ForegroundService::class.java))
            // 캡처 후 결과 화면
            mapView.postDelayed({
                captureMap { bitmap ->
                    if (bitmap != null) setContent { PetWalkTheme {  WalkResultScreen(bitmap,walkViewModel) } }
                    else {
                        Toast.makeText(this, "지도 캡처 실패", Toast.LENGTH_SHORT).show()
                        setContent { PetWalkTheme {  WalkResultScreen(Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888),walkViewModel) } }
                    }
                }
            }, 600)
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(Intent(this, ForegroundService::class.java), connection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if(isBind) {
            unbindService(connection)
            isBind = false
        }

    }

    private fun checkPermission(): Boolean {
        return locationPermission.all {
            ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun initMap() {
        if (isMapInitialized) return
        isMapInitialized = true

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}
            override fun onMapError(p0: Exception?) {
                Toast.makeText(this@WalkActivity, "지도 초기화 실패: ${p0?.message}", Toast.LENGTH_SHORT).show()
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map

                val initPosition = LatLng.from(0.0, -160.0)
                val style = LabelStyle.from(R.drawable.label_sample).setAnchorPoint(0.5f, 0.5f)
                val styles = kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(style))
                val options = LabelOptions.from(initPosition).setStyles(styles)
                centerLabel = kakaoMap.labelManager?.getLayer()?.addLabel(options)

                kakaoMap.moveCamera(
                    CameraUpdateFactory.newCenterPosition(initPosition),
                    CameraAnimation.from(500, true, true)
                )
            }

            override fun getPosition(): LatLng = centerLabel?.position ?: LatLng.from(0.0, -160.0)
            override fun getZoomLevel(): Int = 16
        })
    }
    private fun observeServiceFlows(){
        lifecycleScope.launch {
            walkViewModel.path.collect{path ->
                if(path.size >= 2){
                    updateRoute(path)
                    val lastPos = path.last()
                    kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(lastPos))
                }
            }
        }

        lifecycleScope.launch {
            walkViewModel.distance.collect{ distance ->
                binding.distance.text = String.format("%.1f", distance / 1000.0)
            }
        }
        lifecycleScope.launch {
            walkViewModel.time.collect{ time->
                val sec = time/1000
                binding.time.text = String.format("%02d:%02d:%02d", sec / 3600, (sec % 3600) / 60, sec % 60)
            }
        }
    }
    // 루트라인 갱신
    private fun updateRoute(path: List<LatLng>) {
        if (path.size < 2 || !::kakaoMap.isInitialized) return
        val styleSet = RouteLineStylesSet.from("orangeStyle", RouteLineStyles.from(RouteLineStyle.from(16f, Color.rgb(255, 171, 35))))
        val layer = kakaoMap.routeLineManager!!.getLayer()
        val segment = RouteLineSegment.from(path).setStyles(styleSet.getStyles(0))
        val options = RouteLineOptions.from(listOf(segment)).setStylesSet(styleSet)
        if (!::routeLine.isInitialized) routeLine = layer.addRouteLine(options)
        else routeLine.changeSegments(listOf(segment))
    }
    /* MapView 캡처 */
    private fun captureMap(onCaptured: (Bitmap?) -> Unit) {
        val surfaceView = mapView.getChildAt(0) as? GLSurfaceView ?: run {
            onCaptured(null)
            return
        }
        surfaceView.queueEvent {
            try {
                val width = surfaceView.width
                val height = surfaceView.height
                val bitmapBuffer = IntArray(width * height)
                val bitmapSource = IntArray(width * height)
                val intBuffer = IntBuffer.wrap(bitmapBuffer)
                intBuffer.position(0)

                val gl = (EGLContext.getEGL() as EGL10).eglGetCurrentContext().gl as GL10
                gl.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer)

                for (i in 0 until height) {
                    for (j in 0 until width) {
                        val pixel = bitmapBuffer[i * width + j]
                        val blue = (pixel shr 16) and 0xff
                        val red = (pixel shl 16) and 0x00ff0000
                        val corrected = (pixel and -0xff0100) or red or blue
                        bitmapSource[(height - i - 1) * width + j] = corrected
                    }
                }
                val bitmap = Bitmap.createBitmap(bitmapSource, width, height, Bitmap.Config.ARGB_8888)
                val targetWidth = width
                val targetHeight = (width * 3f / 4f).toInt()

                val offsetY = (height - targetHeight) / 2

                val cropped = Bitmap.createBitmap(bitmap, 0, offsetY, targetWidth, targetHeight)
                runOnUiThread { onCaptured(cropped) }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread { onCaptured(null) }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

