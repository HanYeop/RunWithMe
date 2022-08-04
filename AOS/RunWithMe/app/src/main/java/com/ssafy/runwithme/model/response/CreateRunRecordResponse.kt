package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName
import com.ssafy.runwithme.model.dto.AchievementDto
import com.ssafy.runwithme.model.dto.RunRecordDto

data class CreateRunRecordResponse(
    @SerializedName("runRecord") val runRecordDto: RunRecordDto,
    @SerializedName("achievements") val achievements: List<AchievementDto>
)
