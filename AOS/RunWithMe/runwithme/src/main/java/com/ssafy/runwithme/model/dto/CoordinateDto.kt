package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class CoordinateDto(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
)
