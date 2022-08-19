package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ProfileEditDto(
    @SerializedName("nickName") val nickName: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int
)
