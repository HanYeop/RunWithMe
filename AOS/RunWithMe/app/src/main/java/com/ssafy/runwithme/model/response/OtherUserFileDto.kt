package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName
import com.ssafy.runwithme.model.dto.ImageFileDto
import com.ssafy.runwithme.model.dto.UserDto

data class OtherUserFileDto(
    @SerializedName("user") var userDto : UserDto,
    @SerializedName("imgFile") var imgFileDto: ImageFileDto,
    @SerializedName("totalRecord") var totalRecord: CrewMyTotalRecordDataResponse,
    @SerializedName("achieveList") var achieveList: List<MyAchieveResponse>
)