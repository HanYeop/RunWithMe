package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName
import com.ssafy.runwithme.model.dto.ImageFileDto
import com.ssafy.runwithme.model.dto.UserDto

data class MyProfileResponse(
    @SerializedName("user") val userDto: UserDto,
    @SerializedName("imageFileDto") val imageFileDto: ImageFileDto
)



