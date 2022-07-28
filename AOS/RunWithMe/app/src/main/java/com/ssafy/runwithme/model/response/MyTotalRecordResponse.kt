package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName
import com.ssafy.runwithme.model.dto.ImageFileDto
import com.ssafy.runwithme.model.dto.UserDto

data class MyTotalRecordResponse(
    @SerializedName("totalRecordSeq") val totalRecordSeq: Long,
    @SerializedName("totalTime") val totalTime: Int,
    @SerializedName("totalDistance") val totalDistance: Int,
    @SerializedName("totalLongestTime") val totalLongestTime: Int,
    @SerializedName("totalLongestDistance") val totalLongestDistance: Int,
    @SerializedName("totalAvgSpeed") val totalAvgSpeed: Double,
    @SerializedName("totalCalorie") val totalCalorie: Double
)



