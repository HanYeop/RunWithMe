package com.ssafy.runwithme.utils

// 본인 주소
const val BASE_URL = "http://192.168.25.17:8080/api/"

const val JWT = "JWT-AUTHENTICATION"

const val TAG = "test5"

/**
 * 서비스 상태(action) 상수
 */
const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
const val ACTION_SHOW_RUNNING_ACTIVITY = "ACTION_SHOW_RUNNING_ACTIVITY"
const val ACTION_SHOW_TRACKING_ACTIVITY = "ACTION_SHOW_TRACKING_ACTIVITY"


/**
 * Notification 속성
 */
const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
const val NOTIFICATION_CHANNEL_NAME = "Tracking"
const val NOTIFICATION_ID = 1 // 채널 ID는 0이면 안됨

/**
 * Tracking 옵션
 */
const val LOCATION_UPDATE_INTERVAL = 5000L
const val FASTEST_LOCATION_UPDATE_INTERVAL = 2000L

/**
 * 경로 표시 옵션
 */
const val POLYLINE_WIDTH = 10f
const val MAP_ZOOM = 15f

/**
 * 타이머 갱신 주기
  */
const val TIMER_UPDATE_INTERVAL = 50L

/**
 * 러닝 진행 페이지 가기 전 저장할 값들
 * => 크루 아이디, 달리기 시작시간,
 */
const val CREW_ID = "crew_id"
const val RUN_RECORD_START_TIME = "run_record_start_time"

/**
 * 러닝 진행 페이지에서 저장할 값들
 * => 이미지, 달리기 종료시간, 평균 속도, 소비 칼로리, 목표달성 여부(??), 달린 거리, 시작점 위도, 시작점 경도, 달린 시간,
 */