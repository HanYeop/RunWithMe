package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class MyCurrentCrewResponse(
    @SerializedName ("crew_id") val crewId: Int,
    @SerializedName ("name") val name: String,
    @SerializedName ("image_id") val imageId: Int,
    @SerializedName ("goal_days") val goalDays: Int,
    @SerializedName ("goal_type") val goalType: String,
    @SerializedName ("goal_amount") val goalAmount: Int,
    @SerializedName ("time_start") val timeStart: String,
    @SerializedName ("time_end") val timeEnd: String,
    @SerializedName ("today_complete") val todayComplete: Boolean
)
