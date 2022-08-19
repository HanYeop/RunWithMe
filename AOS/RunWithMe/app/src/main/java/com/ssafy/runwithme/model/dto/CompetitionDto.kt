package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class CompetitionDto(
    @SerializedName("competitionDateStart") val competitionDateStart: String,
    @SerializedName("competitionDateEnd") val competitionDateEnd: String,
    @SerializedName("competitionSeq") val competitionSeq: Int
)
