package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName

data class CrewBoardResponse(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("user_nickname") val userNickname: String,
    @SerializedName("img_id") val imgId: Int,
    @SerializedName("content") val content: String
)
