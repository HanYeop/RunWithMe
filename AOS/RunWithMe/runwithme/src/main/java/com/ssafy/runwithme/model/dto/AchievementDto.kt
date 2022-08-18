package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class AchievementDto(
    @SerializedName("achieveSeq") val achieveSeq: Int,
    @SerializedName("achieveName") val achieveName: String,
    @SerializedName("achieveType") val achieveType: String,
    @SerializedName("achieveValue") val achieveValue: Double,
    @SerializedName("achieveRegTime") val achieveRegTime: String
)
