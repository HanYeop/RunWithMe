package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class CrewBoardDto(
    @SerializedName("crewBoardSeq") val crewBoardSeq: Int,
    @SerializedName("crewBoardContent") val crewBoardContent: String,
    @SerializedName("crewBoardRegTime") val crewBoardRegTime: String,
    @SerializedName("userNickName") val userNickName: String,
    @SerializedName("userSeq") val userSeq: Int,
    @SerializedName("userImgSeq") val userImgSeq: Int,
    @SerializedName("crewName") val crewName: String
)
