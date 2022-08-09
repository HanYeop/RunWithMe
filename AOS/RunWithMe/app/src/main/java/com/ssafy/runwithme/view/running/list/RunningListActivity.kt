package com.ssafy.runwithme.view.running.list

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityRunningListBinding
import com.ssafy.runwithme.model.dto.CoordinateDto
import com.ssafy.runwithme.model.dto.ScrapInfoDto
import com.ssafy.runwithme.utils.*
import com.ssafy.runwithme.view.running.RunningViewModel
import com.ssafy.runwithme.view.running.list.sheet.RunningBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class RunningListActivity : BaseActivity<ActivityRunningListBinding>(R.layout.activity_running_list),
GoogleMap.OnMarkerClickListener{

    // 처음 여부 (true = 아직 처음)
    private var first: Boolean = true
    // 구글맵 선언
    private var map: GoogleMap? = null
    private var myLocation = Location("MyLocation")
    private lateinit var currentPosition: LatLng

    private val runningViewModel by viewModels<RunningViewModel>()

    private var polyLineList = mutableListOf<LatLng>()

    private var POLYLINE_COLOR = Color.RED

    private var scrapList = listOf<ScrapInfoDto>()

    private var job: Job = Job()

    private lateinit var scrapInfo: ScrapInfoDto

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            mapViewRunningList.onCreate(savedInstanceState)
            // 맵 불러오기
            mapViewRunningList.getMapAsync {
                map = it
                it.mapType = 1
                updateLocation()

                it.setOnMarkerClickListener(this@RunningListActivity)
                it.isMyLocationEnabled = true

                lifecycleScope.launch {
                    delay(1100)
                    if (binding.progressBarRunningList.visibility == View.VISIBLE) {
                        binding.progressBarRunningList.visibility = View.GONE
                    }
                    if (binding.layoutMap.visibility == View.INVISIBLE) {
                        binding.layoutMap.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    // 마커 클릭 시 경로를 보여준다.
    override fun onMarkerClick(p0: Marker): Boolean {
        if(job.isActive){
            job.cancel()
        }
        runningViewModel.emptyCoordinates()
        scrapInfo = p0.tag as ScrapInfoDto
        binding.scrap = scrapInfo

        binding.cardInfo.visibility = View.VISIBLE
        binding.animationStartBtn.visibility = View.GONE
        binding.btnScrap.visibility = View.GONE
        binding.btnTts.visibility = View.GONE

        runningViewModel.getCoordinates(scrapInfo.trackBoardFileDto.runRecordDto.runRecordSeq)
        return true
    }

    override fun init() {
        initClickListener()

        initViewModelCallBack()

        runningViewModel.getMyProfile()

        if(sharedPreferences.getBoolean(TTS_ACTIVE, true)){
            binding.btnTts.alpha = 1.0f
        }else{
            binding.btnTts.alpha = 0.5f
        }
    }

    private fun initViewModelCallBack(){
        lifecycleScope.launch {
            runningViewModel.nickname.collectLatest {
                binding.tvNickName.text = "$it!"
            }
        }
        lifecycleScope.launch {
            runningViewModel.scrapList.collectLatest {
                if(it.isNotEmpty()){
                    scrapList = it
                    scrapDraw(scrapList)
                }
            }
        }
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
            addAllPolylines()
            zoomToWholeTrack()
        }
    }

    // 경로 전부 표시
    private fun addAllPolylines() {
        job = lifecycleScope.launch {
            for(i in 1 until polyLineList.size){
                val polylineOptions = PolylineOptions()
                    .color(POLYLINE_COLOR)
                    .width(POLYLINE_WIDTH)
                    .add(polyLineList[i])
                    .add(polyLineList[i - 1])
                map?.addPolyline(polylineOptions)
                delay(POLYLINE_DRAW_TIME)
            }
            if(map != null){
                scrapDraw(scrapList)
            }
        }
    }

    // 전체 경로가 다 보이게 줌
    private fun zoomToWholeTrack() {
        val bounds = LatLngBounds.Builder()
        for (polyline in polyLineList) {
            bounds.include(polyline)
        }

        val width = binding.mapViewRunningList.width
        val height = binding.mapViewRunningList.height
        map?.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                width,
                height,
                (height * 0.05f).toInt()
            )
        )
    }

    // 내가 스크랩한 장소 마커 찍기
    private fun scrapDraw(list: List<ScrapInfoDto>){
        for(i in list){
            val latLng = LatLng(i.trackBoardFileDto.runRecordDto.runRecordRunningLat, i.trackBoardFileDto.runRecordDto.runRecordRunningLng)
            val title = "${i.title}"
            var markerSnippet = "이 경로로 러닝 시작하기"

            drawMarker(latLng, title, markerSnippet, i)
        }
    }

    // 마커 그리기
    private fun drawMarker(latLng: LatLng, markerTitle: String?, markerSnippet: String?, data: ScrapInfoDto): Marker? {
        val markerOptions = MarkerOptions().apply {
            position(latLng)
            title(markerTitle)
            snippet(markerSnippet)
            draggable(true)
            icon(
                BitmapDescriptorFactory.fromBitmap(
                    Bitmap.createScaledBitmap(
                        (ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.user_location,
                            this@RunningListActivity.theme
                        ) as BitmapDrawable).bitmap,
                        150, 150, false
                    )
                )
            )
        }

        val marker = map?.addMarker(markerOptions)
        marker?.tag = data
        return marker
    }

    // 위치 정보 요청하기
    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        if (TrackingUtility.hasLocationPermissions(this)) {
            val request = LocationRequest.create().apply {
                interval = LOCATION_UPDATE_INTERVAL // 위치 업데이트 주기
                fastestInterval = FASTEST_LOCATION_UPDATE_INTERVAL // 가장 빠른 위치 업데이트 주기
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 배터리소모를 고려하지 않으며 정확도를 최우선으로 고려
                maxWaitTime = LOCATION_UPDATE_INTERVAL // 최대 대기시간
            }
            Log.d(TAG, "updateLocation: ")
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)

            result.locations.let {
                if(it.size > 0){
                    if(first) {
                        val location = it[it.size - 1]
                        currentPosition = LatLng(location.latitude, location.longitude)

                        myLocation.apply {
                            latitude = location.latitude; longitude = location.longitude
                        }

                        moveCamera(currentPosition)

                        first = false
                    }
                }
            }
        }
    }

    // 지도 카메라 움직이기
    private fun moveCamera(latLng: LatLng) {
        map?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, 16.5f)
        )
    }

    private var scrapOn = false

    private fun initClickListener(){
        binding.apply {
            animationStartBtn.setOnClickListener {
                val dialog = RunningBottomSheet(this@RunningListActivity, sharedPreferences, 0)
                dialog.show(supportFragmentManager,dialog.tag)
            }
            // 스크랩 불러오기
            btnScrap.setOnClickListener {
                if(!scrapOn) {
                    runningViewModel.getMyScrap()
                    btnScrap.alpha = 1.0f
                    scrapOn = true
                }else{
                    if(map != null){
                        map?.clear()
                        if(job.isActive){
                            job.cancel()
                        }
                        runningViewModel.emptyScrapList()
                        btnScrap.alpha = 0.5f
                    }
                    scrapOn = false
                }
            }
            cardInfo.setOnClickListener {
                val dialog = RunningBottomSheet(this@RunningListActivity, sharedPreferences, scrapInfo.trackBoardFileDto.runRecordDto.runRecordSeq)
                dialog.show(supportFragmentManager,dialog.tag)
            }

            imgCancel.setOnClickListener {
                binding.cardInfo.visibility = View.GONE
                binding.animationStartBtn.visibility = View.VISIBLE
                binding.btnScrap.visibility = View.VISIBLE
                binding.btnTts.visibility = View.VISIBLE
            }

            btnTts.setOnClickListener {
                if(sharedPreferences.getBoolean(TTS_ACTIVE, true)){
                    sharedPreferences.edit().putBoolean(TTS_ACTIVE,false).apply()
                    btnTts.alpha = 0.5f
                }else{
                    sharedPreferences.edit().putBoolean(TTS_ACTIVE,true).apply()
                    btnTts.alpha = 1.0f
                }
            }
        }
    }

    /**
     * 라이프 사이클에 맞게 맵뷰를 처리해줌
     */
    override fun onResume() {
        binding.mapViewRunningList.onResume()
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapViewRunningList.onStart()
    }

    override fun onPause() {
        super.onPause()
        binding.mapViewRunningList.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapViewRunningList.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapViewRunningList.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapViewRunningList.onDestroy()
    }
}