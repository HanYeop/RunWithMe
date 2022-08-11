package com.ssafy.runwithme.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Build
import android.os.Looper
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.ssafy.runwithme.R
import com.ssafy.runwithme.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

private const val TAG = "test5"
@AndroidEntryPoint
class RunningService : LifecycleService() {

    private var tts: TextToSpeech? = null

    // 서비스 종료 여부
    private var serviceKilled = false

    // FusedLocationProviderClient 주입
    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // NotificationCompat.Builder 주입
    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    // NotificationCompat.Builder 수정하기 위함
    lateinit var currentNotificationBuilder : NotificationCompat.Builder

    // 알림창에 표시될 시간
    private val timeRunInSeconds = MutableLiveData<Long>()

    private var isTimerEnabled = false // 타이머 실행 여부
    private var lapTime = 0L // 시작 후 측정한 시간
    private var totalTime = 0L // 정지 시 저장되는 시간
    private var timeStarted = 0L // 측정 시작된 시간
    private var lastSecondTimestamp = 0L // 1초 단위 체크를 위함
    
    // 러닝을 시작하거나 다시 시작한 시간
    private var startTime = 0L

    private var pauseLast = false

    companion object{
        val isTracking = MutableLiveData<Boolean>() // 위치 추적 상태 여부
        val pathPoints = MutableLiveData<Polylines>() // LatLng = 위도,경도
        val timeRunInMillis = MutableLiveData<Long>() // 뷰에 표시될 시간
        var isFirstRun = false // 처음 실행 여부 (false = 실행되지않음)
        val sumDistance = MutableLiveData<Float>(0f)
        val defaultLatLng = MutableLiveData<LatLng>()
    }

    private fun initTextToSpeech() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Toast.makeText(this, "음성 안내를 지원하지 않는 버전입니다.", Toast.LENGTH_SHORT).show()
            return
        }
        tts = TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.KOREAN)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "음성 안내를 지원하지 않는 언어입니다.", Toast.LENGTH_SHORT).show()
                    return@TextToSpeech
                }
//                Toast.makeText(this, "TTS 세팅이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
//                Toast.makeText(this, "TTS 세팅 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ttsSpeak(strTTS: String){
        if(sharedPreferences.getBoolean(TTS_ACTIVE, true)) {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(1000) // 1초간 진동
            tts?.speak(strTTS, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    // 초기화
    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
        sumDistance.postValue(0f)
    }

    override fun onCreate() {
        super.onCreate()
        currentNotificationBuilder = baseNotificationBuilder
        postInitialValues()

        initTextToSpeech()

        // 위치 추적 상태가 되면 업데이트 호출
        isTracking.observe(this){
            updateLocation(it)
            updateNotificationTrackingState(it)
        }

        distanceTTS()
    }

    // 서비스가 종료 되었을 때
    private fun killService(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        serviceKilled = true
        isFirstRun = false
        pauseService()
        startTime = 0L
        pauseLastLatLng = LatLng(0.0,0.0)
        stopLastLatLng = LatLng(0.0,0.0)
        pauseLast = false
        count = 1

        postInitialValues()
        stopForeground(true)
        stopSelf()

    }

    // 알림창 버튼 생성, 액션 추가
    private fun updateNotificationTrackingState(isTracking: Boolean) {
        val notificationActionText = if (isTracking) "일시정지" else "다시 시작하기"
        // 정지 or 시작 버튼 클릭 시 그에 맞는 액션 전달함
        val pendingIntent = if (isTracking) {
            val pauseIntent = Intent(this, RunningService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_MUTABLE)
        } else {
            val resumeIntent = Intent(this, RunningService::class.java).apply {
                action = ACTION_START_OR_RESUME_SERVICE
            }
            PendingIntent.getService(this, 2, resumeIntent, PendingIntent.FLAG_MUTABLE)
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        currentNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(currentNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }

        // 서비스 종료상태가 아닐 때
        if(!serviceKilled) {
            currentNotificationBuilder = baseNotificationBuilder
                .addAction(
                    R.drawable.img,
                    notificationActionText,
                    pendingIntent
                )
            notificationManager.notify(NOTIFICATION_ID, currentNotificationBuilder.build())
        }
    }

    // 타이머 시작
    private fun startTimer(){
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true

        CoroutineScope(Dispatchers.Main).launch {
            // 위치 추적 상태일 때
            while (isTracking.value!!){
                // 현재 시간 - 시작 시간 => 경과한 시간
                lapTime = System.currentTimeMillis() - timeStarted
                // 총시간 (일시정지시 저장된 시간) + 경과시간 전달
                timeRunInMillis.postValue(totalTime + lapTime)
                // 알림창에 표시될 시간 초 단위로 계산함
                if(timeRunInMillis.value!! >= lastSecondTimestamp + 1000L){
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    lastSecondTimestamp += 1000L
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
            // 위치 추적이 종료(정지) 되었을 때 총시간 저장
            totalTime += lapTime
        }
    }

    // 빈 polyline 추가
    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf())) // null 이라면 초기화

    // 위치정보 추가
    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
                distancePolyline()
            }
        }
    }



    // 멈추기 직전 위치
    private var pauseLastLatLng = LatLng(0.0,0.0)
    // 멈추고 난 후 마지막 위치
    private var stopLastLatLng = LatLng(0.0,0.0)

    // TEST : 러닝 거리 테스트
    private var count = 1
    private fun distanceTTS(){
        sumDistance.observe(this){
            if(it / 1000 > count){
                ttsSpeak("${(it / 1000).toInt()} km")
                count++
            }
        }
    }

    // 거리 표시 (마지막 전, 마지막 경로 차이 비교)
    private fun distancePolyline(){
        if(pathPoints.value!!.isNotEmpty() && pathPoints.value!!.last().size > 1){
            val preLastLatLng = pathPoints.value!!.last()[pathPoints.value!!.last().size - 2] // 마지막 전 경로
            val lastLatLng = pathPoints.value!!.last().last() // 마지막 경로

            // 이동거리 계산
            val result = FloatArray(1)
            Location.distanceBetween(
                preLastLatLng.latitude,
                preLastLatLng.longitude,
                lastLatLng.latitude,
                lastLatLng.longitude,
                result
            )

            // 비동기
            sumDistance.postValue(sumDistance.value!!.plus(result[0]))

            Log.d(TAG, "distancePolyline: ${result[0]}")

            // 5초 이상 이동했는데 이동거리가 2.5m 이하인 경우 정지하고, 마지막 위치를 기록함
            if(result[0] < 2.5f && (System.currentTimeMillis() - startTime) > 5000L) {

                Toast.makeText(this, "이동이 없어 러닝이 일시 중지되었습니다.", Toast.LENGTH_SHORT).show()
                ttsSpeak("이동이 없어 러닝이 일시 중지되었습니다.")
                pauseLastLatLng = lastLatLng
                pauseLast = true
                pauseService()
            }

            // 5초 이상 이동했는데 이동거리가 150m 이상인 경우 정지
            if(result[0] > 150f && (System.currentTimeMillis() - startTime) > 5000L) {
                Toast.makeText(this, "비정상적인 이동이 감지되어 러닝이 일시 중지되었습니다.", Toast.LENGTH_SHORT).show()
                ttsSpeak("비정상적인 이동이 감지되어 러닝이 일시 중지되었습니다.")
                pauseService()
            }
        }
    }

    private fun resumeRunning(){
        val result = FloatArray(1)
        Location.distanceBetween(
            pauseLastLatLng.latitude,
            pauseLastLatLng.longitude,
            stopLastLatLng.latitude,
            stopLastLatLng.longitude,
            result
        )

        // 이동이 없어 중지 상태일 때, 8m 이동하면 다시 시작 시킴
        if(result[0] > 8f && (System.currentTimeMillis() - startTime) > 5000L){
//            Log.d(TAG, "resumeRunning: ${System.currentTimeMillis()} ${startTime}")
//            Log.d(TAG, "resumeRunning: ${isTracking.value}")
//            Log.d(TAG, "resumeRunning: ${result}")
//            Log.d(TAG, "resumeRunning: ${pauseLastLatLng} ${stopLastLatLng}")

            if(!isTracking.value!! && pauseLast){
                Toast.makeText(this, "이동이 감지되어 러닝을 다시 시작합니다.", Toast.LENGTH_SHORT).show()
                ttsSpeak("이동이 감지되어 러닝을 다시 시작합니다.")
                startTimer()
                startTime = System.currentTimeMillis()
                pauseLast = false
            }
        }
    }

    // 위치정보 수신하여 addPathPoint 로 추가
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if(isTracking.value!!) {
                result?.locations?.let { locations ->
                    for(location in locations) {
                        addPathPoint(location)
                        Log.d(TAG, "저장됨 : ${location.latitude} , ${location.longitude}")
                    }
                }
            }else{
                result?.locations?.let { locations ->
                    for(location in locations) {
                        // 처음 시작 때 위치 초기화
                        if(!isFirstRun) {
                            defaultLatLng.postValue(LatLng(location.latitude, location.longitude))
                            pauseLastLatLng = LatLng(location.latitude, location.longitude)
                        }
                        stopLastLatLng = LatLng(location.latitude, location.longitude)
                        resumeRunning()
                    }
                }
            }
        }
    }

    // 위치 정보 요청하기
    @SuppressLint("MissingPermission")
    private fun updateLocation(isTracking: Boolean) {
        if (isTracking) {
            if (TrackingUtility.hasLocationPermissions(this)) {
                val request = LocationRequest.create().apply {
                    interval = LOCATION_UPDATE_INTERVAL // 위치 업데이트 주기
                    fastestInterval = FASTEST_LOCATION_UPDATE_INTERVAL // 가장 빠른 위치 업데이트 주기
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 배터리소모를 고려하지 않으며 정확도를 최우선으로 고려
                    maxWaitTime = LOCATION_UPDATE_INTERVAL // 최대 대기시간
                }
                fusedLocationProviderClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
            }
        } else {
//            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }


    // 서비스가 호출 되었을 때
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let{
            when(it.action){
                // 시작, 재개 되었을 때
                ACTION_START_OR_RESUME_SERVICE ->{
                    if(!isFirstRun){
                        startForegroundService()
                        isFirstRun = true
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(1000)
                            ttsSpeak("러닝을 시작합니다.")
                        }
                    }else{
                        startTimer()
                        ttsSpeak("러닝을 다시 시작합니다.")
                    }
                    startTime = System.currentTimeMillis()
                }
                // 중지 되었을 때
                ACTION_PAUSE_SERVICE ->{
                    ttsSpeak("러닝이 일시 중지되었습니다.")
                    pauseService()
                }
                // 종료 되었을 때
                ACTION_STOP_SERVICE ->{
                    ttsSpeak("러닝이 종료되었습니다.")
                    killService()
                }
                // 처음 화면 켰을 때
                ACTION_SHOW_RUNNING_ACTIVITY ->{
                    updateLocation(true)
                }
                else -> null
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    // 서비스 정지
    private fun pauseService(){
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    // Notification 등록, 서비스 시작
    private fun startForegroundService(){
//        addEmptyPolyline()
//        isTracking.postValue(true)
        startTimer()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        startForeground(NOTIFICATION_ID,baseNotificationBuilder.build())

        // 초가 흐를때마다 알림창의 시간 갱신
        timeRunInSeconds.observe(this) {
            // 서비스 종료상태가 아닐 때
            if(!serviceKilled) {
                val notification = currentNotificationBuilder
                    .setContentText(TrackingUtility.getFormattedStopWatchTime(it * 1000L))
                notificationManager.notify(NOTIFICATION_ID, notification.build())
            }
        }
    }

    // 채널 만들기
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW // 알림음 없음
        )
        notificationManager.createNotificationChannel(channel)
    }
}