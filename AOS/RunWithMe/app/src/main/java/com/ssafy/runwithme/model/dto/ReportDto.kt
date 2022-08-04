package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class ReportDto(
    @SerializedName("reportSeq") val reportSeq: Int,
    @SerializedName("reportContent") val reportContent: String,
    @SerializedName("reportStatus") val reportStatus: String,
    @SerializedName("reportCrewBoardSeq") val reportCrewBoardSeq: Int
)
