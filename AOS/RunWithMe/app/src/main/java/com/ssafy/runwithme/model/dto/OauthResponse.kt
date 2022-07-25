package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class OauthResponse(
    @SerializedName("msg") val msg: String = "",
    @SerializedName("isRegistered") val isRegistered: Boolean = false,
    @SerializedName("email") val email: String = "",
    @SerializedName("JWT-AUTHENTICATION") val jwtToken: String = ""
    )
