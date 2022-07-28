package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName

class MyTotalBoardsResponse(
    @SerializedName("crewBoardSeq") val crewBoardSeq : Int,
    @SerializedName("crewBoardContent") val crewBoardContent : String
)