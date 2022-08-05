package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

class ProcessInfoDto(
    @SerializedName("totalGoals") val totalGoals : Int,
    @SerializedName("myGoals") val myGoals : Int
)