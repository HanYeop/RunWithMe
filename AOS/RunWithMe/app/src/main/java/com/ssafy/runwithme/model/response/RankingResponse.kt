package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName

data class RankingResponse(
    @SerializedName("userName") val userName: String,
    @SerializedName("userSeq") val userSeq: Int,
    @SerializedName("rankingIndex") val rankingIndex: Int,
    @SerializedName("rankingValue") val rankingValue: Int,
    @SerializedName("imgSeq") val imgSeq: Int
)
