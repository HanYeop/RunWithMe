package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName

class MyTotalBoardsResponse(
    @SerializedName("crewBoardSeq") val crewBoardSeq : Int,
    @SerializedName("crewBoardContent") val crewBoardContent : String,
    @SerializedName("crewBoardRegTime") val crewBoardRegTime : String,
    @SerializedName("userNickName") val userNickName : String,
    @SerializedName("userSeq") val userSeq : Int,
    @SerializedName("crewName") val crewName : String
)