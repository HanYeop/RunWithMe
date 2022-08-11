package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class PasswordDto(
    @SerializedName("password") val password: String
) {
}