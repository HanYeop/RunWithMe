package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class ScrapInfoDto(
    @SerializedName("scrapSeq") val scrapSeq: Int,
    @SerializedName("title") val title: String,
    @SerializedName("trackBoardFileDto") val trackBoardFileDto: TrackBoardFileDto
)
