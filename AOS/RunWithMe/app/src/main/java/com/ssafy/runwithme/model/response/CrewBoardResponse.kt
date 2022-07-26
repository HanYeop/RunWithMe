package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName
import com.ssafy.runwithme.model.dto.CrewBoardDto
import com.ssafy.runwithme.model.dto.ImageFileDto

data class CrewBoardResponse(
    @SerializedName("crewBoardDto") val crewBoardDto: CrewBoardDto,
    @SerializedName("imageFileDto") val imageFileDto: ImageFileDto,
)
