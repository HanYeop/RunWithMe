package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName

data class JoinResponse(
    @SerializedName("JWT-AUTHENTICATION") val jwtToken: String = ""
)
