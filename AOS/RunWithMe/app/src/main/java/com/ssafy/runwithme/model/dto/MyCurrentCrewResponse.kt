package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class MyCurrentCrewResponse(
    @SerializedName ("crew_id") val crewId: Int,
    @SerializedName ("crew_name") val crewName: String,
    @SerializedName ("manager_name") val managerName: String,
    @SerializedName ("crew_description") val description: String,
    @SerializedName ("image_id") val imageId: Int,
    @SerializedName ("goal_days") val goalDays: Int,
    @SerializedName ("goal_type") val goalType: String,
    @SerializedName ("goal_amount") val goalAmount: Int,
    @SerializedName ("time_start") val timeStart: String,
    @SerializedName ("time_end") val timeEnd: String,
    @SerializedName ("day_start") val dayStart: String,
    @SerializedName ("day_end") val dayEnd: String,
    @SerializedName ("today_complete") val todayComplete: Boolean
)
