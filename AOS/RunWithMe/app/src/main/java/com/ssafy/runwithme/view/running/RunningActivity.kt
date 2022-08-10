package com.ssafy.runwithme.view.running

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityRunningBinding
import com.ssafy.runwithme.model.dto.CoordinateDto
import com.ssafy.runwithme.service.Polyline
import com.ssafy.runwithme.service.RunningService
import com.ssafy.runwithme.utils.*
import com.ssafy.runwithme.view.loading.LoadingDialog
import com.ssafy.runwithme.view.running.result.RunningResultActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import kotlin.math.round

@SuppressLint("MissingPermission")
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

    private var type: String = GOAL_TYPE_TIME

    private var goal = 60 * 1000L

    private var weight = 70
    private var caloriesBurned: Int = 0

    private var polyLineList = mutableListOf<LatLng>()

    private var job: Job = Job()

    private val runningViewModel by viewModels<RunningViewModel>()

    private lateinit var runningLoadingDialog: RunningLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        POLYLINE_COLOR = resources.getColor(R.color.main_blue)

        weight = sharedPref.getInt(USER_WEIGHT, 70)

        type = sharedPref.getString(RUN_GOAL_TYPE, GOAL_TYPE_TIME)!!

        binding.apply {
            mapView.onCreate(savedInstanceState)
            // 맵 불러오기
            mapView.getMapAsync {
                map = it

                // 알림 클릭 등으로 다시 생성되었을 때 경로 표시
                addAllPolylines()
                moveCameraToUser()

                // 스크랩한 경로 표시
                if(sharedPref.getInt(RUN_SCRAP_RECORD_SEQ, 0) != 0){
                    runningViewModel.getCoordinates(sharedPref.getInt(RUN_SCRAP_RECORD_SEQ, 0))
                }

                it.mapType = 1
                it.isMyLocationEnabled = true

                sumDistance = RunningService.sumDistance.value!!
                // 프로그래스바 진행도 변경
                if(sumDistance > 0 && type == GOAL_TYPE_DISTANCE){
                    binding.progressBarGoal.progress = if ((sumDistance / (goal / 100)).toInt() >= 100) 100 else (sumDistance / (goal / 100)).toInt()
                }
            }
//            tvCrewName.text = sharedPref.getString(RUN_RECORD_CREW_NAME,"크루")
        }
    }

    override fun init() {
        initClickListener()

        firstStart()

        initViewModelCallBack()

        // 위치 추적 여부 관찰하여 updateTracking 호출
        RunningService.isTracking.observe(this){
            updateTracking(it)
        }

        // 거리 텍스트 변경
        RunningService.sumDistance.observe(this){
            sumDistance = it
            changeDistanceText()

            // 프로그래스바 진행도 변경
            if(sumDistance > 0 && type == GOAL_TYPE_DISTANCE){
                binding.progressBarGoal.progress = if ((sumDistance / (goal / 100)).toInt() >= 100) 100 else (sumDistance / (goal / 100)).toInt()
            }
        }

        changeDistanceText()

        // 칼로리 텍스트 변경
        binding.tvCalorie.text = "$caloriesBurned"
        initLiveData()
    }

    private fun changeDistanceText(){
        binding.apply {
            tvDistance.text = "${TrackingUtility.getFormattedDistance(sumDistance)}"
        }
    }

    private fun changeTimeText(time: String){
        binding.apply {
            tvTime.text = time
            if(type == GOAL_TYPE_TIME) {
                tvGoalAmount.text = "${sharedPref.getInt(RUN_GOAL_AMOUNT,10) / 60}분"
                goal = (sharedPref.getInt(RUN_GOAL_AMOUNT,10) * 1000).toLong()

            }else{
                tvGoalAmount.text = "${sharedPref.getInt(RUN_GOAL_AMOUNT,1000) / 1000}km"
                goal = sharedPref.getInt(RUN_GOAL_AMOUNT,1000).toLong()
            }
        }
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

    // 처음에 지도 위치 이동
    private fun defaultMoveCameraToUser(latLng: LatLng) {
        map?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                MAP_ZOOM
            )
        )

        if(binding.mapView.visibility == View.INVISIBLE){
            binding.mapView.visibility = View.VISIBLE
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

            if(binding.mapView.visibility == View.INVISIBLE){
                binding.mapView.visibility = View.VISIBLE
            }
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

    private fun initViewModelCallBack(){
        lifecycleScope.launch {
            runningViewModel.getCoordinates.collectLatest {
                polyLineList = mutableListOf<LatLng>()
                initPolyLine(it)
            }
        }
    }

    private fun initPolyLine(list: List<CoordinateDto>){
        Log.d(TAG, "initPolyLine: $list")
        if(list.isNotEmpty()) {
            if(map != null){
                map?.clear()
            }
            for (i in list) {
                polyLineList.add(LatLng(i.latitude, i.longitude))
            }
            addAllScrapLines()
        }
    }

    // 스크랩한 경로 전부 표시
    private fun addAllScrapLines() {
        job = lifecycleScope.launch {
            for(i in 1 until polyLineList.size){
                val polylineOptions = PolylineOptions()
                    .color(Color.RED)
                    .width(POLYLINE_WIDTH)
                    .add(polyLineList[i])
                    .add(polyLineList[i - 1])
                map?.addPolyline(polylineOptions)
            }
        }
    }

    private fun initLiveData(){
        // 경로 변경 관찰
        RunningService.pathPoints.observe(this) {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()

            // 칼로리 소모량 체크
            caloriesBurned = round((sumDistance / 1000f) * weight).toInt()
            // 칼로리 텍스트 변경
            binding.tvCalorie.text = "$caloriesBurned"

            changeDistanceText()
        }

        // 시간(타이머) 경과 관찰
        RunningService.timeRunInMillis.observe(this) {
            currentTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTimeSummery(it)

            changeTimeText(formattedTime)

            // 프로그래스바 진행도 변경
            if(it > 0 && type == GOAL_TYPE_TIME) binding.progressBarGoal.progress = if ((it / (goal / 100)).toInt() >= 100) 100 else (it / (goal / 100)).toInt()
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
        }
    }

    private fun firstStart(){
        if(!RunningService.isFirstRun){
            runningLoadingDialog = RunningLoadingDialog(this)
            runningLoadingDialog.show()
            RunningService.defaultLatLng.observe(this){
                defaultMoveCameraToUser(it)
            }
            CoroutineScope(Dispatchers.Main).launch {
                changeDistanceText()
                changeTimeText("00:00")
                sendCommandToService(ACTION_SHOW_RUNNING_ACTIVITY)
                delay(3000L)
                sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
                delay(1000L)
                runningLoadingDialog.dismiss()
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
        }
    }

    // 뒤로가기 버튼 눌렀을 때
    override fun onBackPressed() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("달리기를 종료할까요? 10초 이하의 기록은 저장되지 않습니다.")
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
        if(map != null){
            map?.isMyLocationEnabled = false
            map?.clear()
            addAllPolylines()
        }

        if(currentTimeInMillis < 10000) {
            showToast("10초 이하의 기록은 저장되지 않습니다.")
            finish()
        } else {
            zoomToWholeTrack()
            endRunAndSaveToDB()
        }
    }

    // 달리기 기록 저장
    private fun endRunAndSaveToDB() {
        val dialog = LoadingDialog(this@RunningActivity)

        CoroutineScope(Dispatchers.Main).launch {
            runRecordEndTime = System.currentTimeMillis()
            runRecordRunningAvgSpeed = (round((sumDistance / 1000f) / (currentTimeInMillis / 1000f / 60 / 60) * 10) / 10f)
            runRecordRunningCalorie = caloriesBurned
            runRecordRunningDistance = sumDistance
            runRecordRunningLat = pathPoints.first()[0].latitude
            runRecordRunningLng = pathPoints.first()[0].longitude
            runRecordRunningTime = currentTimeInMillis
            runPathPoints = pathPoints

            dialog.show()
            delay(1000)
            dialog.dismiss()

            map?.snapshot { bmp ->
                image = bmp!!
            }

            dialog.show()
            delay(1000)
            dialog.dismiss()

            startActivity(Intent(this@RunningActivity, RunningResultActivity::class.java))
            finish()
        }
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

        if(map != null){
            map!!.isMyLocationEnabled = true
        }

        // 칼로리 소모량 체크
        caloriesBurned = round((sumDistance / 1000f) * weight).toInt()
        // 칼로리 텍스트 변경
        binding.tvCalorie.text = "$caloriesBurned"
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onPause() {
        super.onPause()

        if(map != null){
            map!!.isMyLocationEnabled = false
        }

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

    companion object{
        lateinit var image: Bitmap

        // 러닝 종료될 때 받아야 함.
        var runRecordEndTime: Long = 0L
        var runRecordRunningAvgSpeed : Float = 0f
        var runRecordRunningCalorie: Int = 0
        var runRecordRunningDistance: Float = 0f
        var runRecordRunningLat: Double = 0.0
        var runRecordRunningLng: Double = 0.0
        var runRecordRunningTime: Long = 0
        var runPathPoints: List<Polyline> = listOf()
    }
}