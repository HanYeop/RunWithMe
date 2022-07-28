package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class TrackBoardDto(
    @SerializedName("trackBoardSeq") val trackBoardSeq: Int,
    @SerializedName("runRecordSeq") val runRecordSeq: Int,
    @SerializedName("trackBoardHardPoint") val trackBoardHardPoint: Int,
    @SerializedName("trackBoardEnvironmentPoint") val trackBoardEnvironmentPoint: Int
)
