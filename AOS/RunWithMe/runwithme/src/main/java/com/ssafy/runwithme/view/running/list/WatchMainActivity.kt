package com.ssafy.runwithme.view.running.list

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.wear.widget.SwipeDismissFrameLayout
import androidx.wear.widget.WearableLinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityMainBinding
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.service.DataLayerListenerService
import com.ssafy.runwithme.utils.*
import com.ssafy.runwithme.view.login.WatchLoginActivity
import com.ssafy.runwithme.view.running.RunningActivity
import com.ssafy.runwithme.view.running.RunningViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class WatchMainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), GoogleMap.OnMarkerClickListener {

    // 처음 여부 (true = 아직 처음)
    private var first: Boolean = true
    // 구글맵 선언
    private var map: GoogleMap? = null
    private var myLocation = Location("MyLocation")
    private lateinit var currentPosition: LatLng

    private val runningViewModel by viewModels<RunningViewModel>()
    private lateinit var runningListAdapter: RunningListAdapter
    private lateinit var myCurrentInfo : MyCurrentCrewResponse

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            mapViewRunningList.onCreate(savedInstanceState)
            // 맵 불러오기
            mapViewRunningList.getMapAsync {
                map = it
                it.mapType = 1
                updateLocation()

                it.setOnMarkerClickListener(this@WatchMainActivity)
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


    override fun init() {
        initClickListener()

        initLoginCallback()

        runningListAdapter = RunningListAdapter(listener)

        binding.apply {
            recyclerRunningList.apply {
                adapter = runningListAdapter
                layoutManager = WearableLinearLayoutManager(this@WatchMainActivity)
            }

            layoutSwipe.addCallback(callback)

        }

        initViewModelCallBack()

        runningViewModel.getMyCurrentCrew()
    }

    private val callback = object : SwipeDismissFrameLayout.Callback() {
        override fun onDismissed(layout: SwipeDismissFrameLayout?) {
            super.onDismissed(layout)
            finish()
        }
    }

    private fun initViewModelCallBack(){
        runningViewModel.errorMsgEvent.observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        runningViewModel.runAbleEvent.observe(this){
            runningStart(sharedPreferences, myCurrentInfo.crewDto.crewSeq, myCurrentInfo.crewDto.crewName
                ,myCurrentInfo.crewDto.crewGoalType, myCurrentInfo.crewDto.crewGoalAmount)
            startActivity(Intent(this, RunningActivity::class.java))
            finish()
        }

        lifecycleScope.launch {
            runningViewModel.runningCrewList.collectLatest {
//                Log.d(TAG, "initViewModelCallBack: $it")
//                runningListAdapter.submitList(it)
                runningListAdapter.setData(it)
            }
        }
    }

    // 실제 크루
    val listener = object : RunningListListener {
        override fun onItemClick(myCurrentCrewResponse: MyCurrentCrewResponse) {
            myCurrentInfo = myCurrentCrewResponse
            Log.d("test5", "onItemClick: ${myCurrentCrewResponse.crewDto.crewSeq}")
            runningViewModel.runAbleToday(myCurrentCrewResponse.crewDto.crewSeq)
        }
    }


    // 위치 정보 요청하기
    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        Log.d(TAG, "updateLocation: ${TrackingUtility.hasLocationPermissions(this)}")
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
            Log.d(TAG, "onLocationResult: dd")
            result.locations.let {
                if(it.size > 0){
                    if(first) {
                        val location = it[it.size - 1]
                        currentPosition = LatLng(location.latitude, location.longitude)

                        myLocation.apply {
                            latitude = location.latitude; longitude = location.longitude
                        }

                        Log.d(TAG, "onLocationResult: $currentPosition")

                        moveCamera(currentPosition)

                        first = false
                    }
                }
            }
        }
    }

    // 지도 카메라 움직이기
    private fun moveCamera(latLng: LatLng) {
        Log.d(TAG, "moveCamera: $latLng")
        map?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, 16.5f)
        )
    }


    private fun initClickListener(){
        binding.apply {
            animationStartBtn.setOnClickListener {
//                val dialog = RunningBottomSheet(this@WatchMainActivity, sharedPreferences)
//                dialog.show(supportFragmentManager,dialog.tag)
                recyclerRunningList.visibility = View.VISIBLE
                mapViewRunningList.visibility = View.GONE
                animationStartBtn.visibility = View.GONE
            }

        }
    }

    /**
     * 라이프 사이클에 맞게 맵뷰를 처리해줌
     */
    override fun onResume() {
        binding.mapViewRunningList.onResume()
        binding.mapViewRunningList.visibility = View.VISIBLE
        binding.animationStartBtn.visibility = View.VISIBLE
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapViewRunningList.onStart()
    }

    override fun onPause() {
        super.onPause()
        binding.mapViewRunningList.onPause()
        binding.recyclerRunningList.visibility = View.GONE
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

    private fun initLoginCallback(){
        DataLayerListenerService.changeMsgEvent.observe(this){
            val intent = Intent(this, WatchLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        return true
    }


}