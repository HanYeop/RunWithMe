package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class CrewBoardDto(
    @SerializedName("crewBoardSeq") val crewBoardSeq: Int,
    @SerializedName("crewBoardContent") val crewBoardContent: String,
)
