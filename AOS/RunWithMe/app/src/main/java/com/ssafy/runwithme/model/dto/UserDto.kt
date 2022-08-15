package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("email") val email: String,
    @SerializedName("point") val point: Int,
    @SerializedName("competitionResult") val competitionResult: String?
)
