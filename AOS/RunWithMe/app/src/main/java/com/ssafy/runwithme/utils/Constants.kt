package com.ssafy.runwithme.utils

// 본인 주소
const val BASE_URL = "http://192.168.25.62:8080/api/"

const val JWT = "jwt"

/**
 * 서비스 상태(action) 상수
 */
const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
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

// 타이머 갱신 주기
const val TIMER_UPDATE_INTERVAL = 50L