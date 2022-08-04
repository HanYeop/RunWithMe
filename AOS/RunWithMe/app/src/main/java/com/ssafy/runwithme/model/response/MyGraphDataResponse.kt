package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName

data class MyGraphDataResponse(
    @SerializedName("amount") val amount: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("day") val day: Int
) {
}