package com.ssafy.runwithme.view.running

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityRunningBinding
import com.ssafy.runwithme.service.Polyline
import com.ssafy.runwithme.service.RunningService
import com.ssafy.runwithme.utils.*
import com.ssafy.runwithme.view.running.result.RunningResultActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RunningActivity : BaseActivity<ActivityRunningBinding>(R.layout.activity_running) {

    // 라이브 데이터를 받아온 값들
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private var currentTimeInMillis = 0L

    // SharedPreferences 주입
    @Inject
    lateinit var sharedPref: SharedPreferences

    // 구글맵 선언
    private var map: GoogleMap? = null

    // 총 이동거리
    private var sumDistance = 0f

    private var POLYLINE_COLOR = Color.RED

    // TEST : 1분
    private val goal = 60 * 1000L

    private lateinit var runningLoadingDialog: RunningLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            mapView.onCreate(savedInstanceState)
            // 맵 불러오기
            mapView.getMapAsync {
                map = it
                // 알림 클릭 등으로 다시 생성되었을 때 경로 표시
                addAllPolylines()
                moveCameraToUser()
            }
        }
    }

    override fun init() {
        initClickListener()

        firstStart()

        // 위치 추적 여부 관찰하여 updateTracking 호출
        RunningService.isTracking.observe(this){
            updateTracking(it)
        }

        sumDistance = updateDistance()
        binding.tvDistance.text = "${TrackingUtility.getFormattedDistance(sumDistance)}Km"

        initLiveData()
    }

    // 경로 전부 표시
    private fun addAllPolylines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    // 지도 위치 이동
    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }

    // 스냅샷 찍기 위하여 전체 경로가 다 보이게 줌
    private fun zoomToWholeTrack() {
        val bounds = LatLngBounds.Builder()
        for (polyline in pathPoints) {
            for (point in polyline) {
                bounds.include(point)
            }
        }
        val width = binding.mapView.width
        val height = binding.mapView.height
        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                width,
                height,
                (height * 0.05f).toInt()
            )
        )
    }

    private fun initLiveData(){
        // 경로 변경 관찰
        RunningService.pathPoints.observe(this) {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()

            // 거리 텍스트 변경
            binding.tvDistance.text = "${TrackingUtility.getFormattedDistance(sumDistance)}Km"
            Log.d("test5", "distance: ${TrackingUtility.getFormattedDistance(sumDistance)}")
        }

        // 시간(타이머) 경과 관찰
        RunningService.timeRunInMillis.observe(this) {
            currentTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(it, true)
            binding.tvCurrentTime.text = formattedTime

            // 프로그래스바 진행도 변경
            if(it > 0) binding.progressBar.progress = (it / (goal / 100)).toInt()
        }
    }

    // 경로 표시 (마지막 전, 마지막 경로 연결)
    private fun addLatestPolyline(){
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1){
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2] // 마지막 전 경로
            val lastLatLng = pathPoints.last().last() // 마지막 경로

            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)

            map?.addPolyline(polylineOptions)

            // 이동거리 계산
            val result = FloatArray(1)
            Location.distanceBetween(
                preLastLatLng.latitude,
                preLastLatLng.longitude,
                lastLatLng.latitude,
                lastLatLng.longitude,
                result
            )
            sumDistance += result[0]
        }
    }

    // 총 이동거리
    private fun updateDistance() : Float{
        var distanceInMeters = 0f
        for (polyline in pathPoints) {
            distanceInMeters += calculatePolylineLength(polyline)
        }
        return distanceInMeters
    }

    // 총 이동거리 계산
    private fun calculatePolylineLength(polyline: Polyline): Float {
        var distance = 0f
        // 두 경로 사이마다 거리를 계산하여 합함
        for (i in 0 until polyline.size - 1) {
            val pos1 = polyline[i]
            val pos2 = polyline[i + 1]
            val result = FloatArray(1)
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
        }
        return distance
    }

    private fun firstStart(){
        if(!RunningService.isFirstRun){
            runningLoadingDialog = RunningLoadingDialog(this)
            runningLoadingDialog.show()
            CoroutineScope(Dispatchers.Main).launch {
                binding.tvCurrentTime.text = "00 : 00"
                delay(3000L)
                sendCommandToService(ACTION_SHOW_RUNNING_ACTIVITY)
                delay(500L)
                runningLoadingDialog.dismiss()
                sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            }
        }
    }
    // 위치 추적 상태에 따른 레이아웃 변경
    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        binding.apply {
            if (!isTracking) {
                imgPause.visibility = View.INVISIBLE
                imgStart.visibility = View.VISIBLE
                imgStop.visibility = View.VISIBLE
            }
            else if (isTracking) {
                imgPause.visibility = View.VISIBLE
                imgStart.visibility = View.GONE
                imgStop.visibility = View.GONE
            }
        }
    }

    private fun initClickListener(){
        // 스타트 버튼 클릭 시 서비스를 시작함
        binding.apply {
            imgPause.setOnClickListener {
                // 이미 실행 중이면 일시 정지
                if(isTracking) {
                    sendCommandToService(ACTION_PAUSE_SERVICE)
                }
            }
            imgStart.setOnClickListener {
                sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            }
            imgStop.setOnClickListener {
                stopRun()
            }
            tvTabGoal.setOnClickListener {
                layoutMap.visibility = View.GONE
                layoutGoal.visibility = View.VISIBLE
                tvTabGoal.setTextColor(resources.getColor(R.color.mainColor))
                tvTabMap.setTextColor(resources.getColor(R.color.black_high_emphasis))
            }
            tvTabMap.setOnClickListener {
                layoutMap.visibility = View.VISIBLE
                layoutGoal.visibility = View.GONE
                tvTabGoal.setTextColor(resources.getColor(R.color.black_high_emphasis))
                tvTabMap.setTextColor(resources.getColor(R.color.mainColor))
            }
        }
    }

    // 뒤로가기 버튼 눌렀을 때
    override fun onBackPressed() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("달리기를 취소할까요? 기록은 저장되지 않습니다.")
            .setPositiveButton("네"){ _,_ ->
                // TODO : 달리기 종료시킴
                stopRun()
            }
            .setNegativeButton("아니오"){_,_ ->
                // 다시 시작
            }.create()
        builder.show()
    }

    // 달리기 종료
    private fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
        startActivity(Intent(this,RunningResultActivity::class.java))
        finish()
    }

    // 서비스에게 명령을 전달함
    private fun sendCommandToService(action : String) =
        Intent(this,RunningService::class.java).also {
            it.action = action
            this.startService(it)
        }

    /**
     * 라이프 사이클에 맞게 맵뷰를 처리해줌
     */
    override fun onResume() {
        binding.mapView.onResume()
        // 백그라운드 상태에서 돌아왔을 때 경로 표시
        addAllPolylines()
        moveCameraToUser()

        // 거리 텍스트 동기화
        sumDistance = updateDistance()
        binding.tvDistance.text = "${TrackingUtility.getFormattedDistance(sumDistance)}Km"
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}