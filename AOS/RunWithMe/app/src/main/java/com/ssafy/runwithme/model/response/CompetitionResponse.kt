package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName
import com.ssafy.runwithme.model.dto.CompetitionDto
import com.ssafy.runwithme.model.dto.CompetitionImageFileDto

data class CompetitionResponse (
    @SerializedName("competitionDto") val competitionDto: CompetitionDto,
    @SerializedName("competitionImageFileDto") val competitionImageFileDto: CompetitionImageFileDto,
    )