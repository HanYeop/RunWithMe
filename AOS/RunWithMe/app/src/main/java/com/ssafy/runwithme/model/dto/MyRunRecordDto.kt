package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class MyRunRecordDto(
    @SerializedName("userSeq") val userSeq: Int = 0,
    @SerializedName("crewSeq") val crewSeq: Int = 0,
    @SerializedName("maxRunRecordSeq") val maxRunRecordSeq: Int = 0,
    @SerializedName("year") val year: Int = 0,
    @SerializedName("month") val month: Int = 0,
)
