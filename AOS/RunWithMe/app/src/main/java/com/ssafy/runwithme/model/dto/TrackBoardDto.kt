package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class TrackBoardDto(
    @SerializedName("trackBoardSeq") val trackBoardSeq: Int,
    @SerializedName("runRecordSeq") val runRecordSeq: Int,
    @SerializedName("content") val content: String,
    @SerializedName("hardPoint") val hardPoint: Int,
    @SerializedName("environmentPoint") val environmentPoint: Int
)
