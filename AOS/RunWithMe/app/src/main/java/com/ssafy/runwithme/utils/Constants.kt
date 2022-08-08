package com.ssafy.runwithme.utils

const val BASE_URL = "https://i7d101.p.ssafy.io:443/api/"

const val JWT = "JWT-AUTHENTICATION"

const val USER = "user_seq"
const val USER_WEIGHT = "user_weight"
const val USER_NAME = "run_user_name"

const val PERMISSION_OK = "permission_ok"

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


const val FCM_CHANNEL_ID = "FCM_channel"
const val FCM_CHANNEL_NAME = "FCM"
const val FCM_ID = 2 // 채널 ID는 0이면 안됨

/**
 * Tracking 옵션
 */
const val LOCATION_UPDATE_INTERVAL = 10000L
const val FASTEST_LOCATION_UPDATE_INTERVAL = 10000L

/**
 * 경로 표시 옵션
 */
const val POLYLINE_WIDTH = 12f
const val MAP_ZOOM = 17f
const val POLYLINE_DRAW_TIME = 20L

/**
 * 타이머 갱신 주기
  */
const val TIMER_UPDATE_INTERVAL = 50L

/**
 * 러닝 진행 페이지 가기 전 저장할 값들
 * => 크루 아이디, 크루 이름, 달리기 시작시간, 목표 타입, 목표량, 스크랩 여부
 */
const val RUN_RECORD_CREW_ID = "run_record_crew_id"
const val RUN_RECORD_CREW_NAME = "run_record_crew_name"
const val RUN_RECORD_START_TIME = "run_record_start_time"
const val RUN_GOAL_TYPE = "run_goal_type"
const val RUN_GOAL_AMOUNT = "run_goal_amount"
const val RUN_SCRAP_RECORD_SEQ = "run_scrap_record_seq"

/**
 * 목표 타입
 */
const val GOAL_TYPE_TIME = "time"
const val GOAL_TYPE_DISTANCE = "distance"

val achieve_list = listOf(
    "runcount1",
    "runcount10",
    "runcount100",
    "runcount1000",
    "totaltime36000",
    "totaltime360000",
    "totaltime3600000",
    "totaltime36000000",
    "totaldistance10000",
    "totaldistance100000",
    "totaldistance1000000",
    "totaldistance10000000",
)