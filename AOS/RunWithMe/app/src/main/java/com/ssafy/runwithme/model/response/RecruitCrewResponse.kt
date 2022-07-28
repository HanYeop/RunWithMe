package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName
import com.ssafy.runwithme.model.dto.CrewDto
import com.ssafy.runwithme.model.dto.ImageFileDto

data class RecruitCrewResponse(
    @SerializedName("crewDto") val crewDto: CrewDto,
    @SerializedName("imageFileDto") val imageFileDto: ImageFileDto
)