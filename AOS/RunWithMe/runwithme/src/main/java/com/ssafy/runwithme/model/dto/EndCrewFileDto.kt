package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName


class EndCrewFileDto(
    @SerializedName("crewDto") val crewDto : CrewDto,
    @SerializedName("processInfo") val processInfo : ProcessInfoDto,
    @SerializedName("imageFileDto") val imageFileDto : ImageFileDto
)