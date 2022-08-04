package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName

data class CrewMyTotalRecordDataResponse(
    @SerializedName("totalTime") val totalTime: Int,
    @SerializedName("totalDistance") val totalDistance: Int
) {
}