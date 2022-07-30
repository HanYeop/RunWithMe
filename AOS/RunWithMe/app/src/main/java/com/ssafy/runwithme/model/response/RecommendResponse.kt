package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName
import com.ssafy.runwithme.model.dto.ImageFileDto
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.dto.TrackBoardDto
import com.ssafy.runwithme.model.dto.UserDto

data class RecommendResponse (
    @SerializedName("trackBoardDto") val trackBoardDto: TrackBoardDto,
    @SerializedName("userDto") val userDto: UserDto,
    @SerializedName("runRecordDto") val runRecordDto: RunRecordDto,
    @SerializedName("imageFileDto") val imageFileDto: ImageFileDto
)