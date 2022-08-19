package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName
import com.ssafy.runwithme.model.dto.AchievementDto

data class MyAchieveResponse(
    @SerializedName("achievementDto") val achievementDto: AchievementDto,
    @SerializedName("regTime") val regTime: String
)



