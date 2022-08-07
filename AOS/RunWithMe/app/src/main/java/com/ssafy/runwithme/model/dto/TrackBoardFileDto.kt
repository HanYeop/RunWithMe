package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class TrackBoardFileDto(
    @SerializedName("trackBoardDto") val trackBoardDto: TrackBoardDto,
    @SerializedName("trackBoardImageFileDto") val trackBoardImageFileDto: ImageFileDto,
    @SerializedName("userDto") val userDto: UserDto,
    @SerializedName("runRecordDto") val runRecordDto: RunRecordDto,
    @SerializedName("imageFileDto") val imageFileDto: ImageFileDto
)
