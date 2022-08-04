package com.ssafy.runwithme.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrewMyTotalRecordDataResponse(
    @SerializedName("totalTime") val totalTime: Int,
    @SerializedName("totalDistance") val totalDistance: Int,
    @SerializedName("totalAvgSpeed") val totalAvgSpeed: Double,
    @SerializedName("totalCalorie") val totalCalorie: Double
): Parcelable