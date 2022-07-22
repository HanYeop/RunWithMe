package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class UserAuthorization(
    @SerializedName("location")
    val location: String
)
