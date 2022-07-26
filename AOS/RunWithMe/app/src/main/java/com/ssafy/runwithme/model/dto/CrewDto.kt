package com.ssafy.runwithme.model.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrewDto(
    @SerializedName("crewSeq") val crewSeq: Int,
    @SerializedName("crewName") val crewName: String,
    @SerializedName("crewDescription") val crewDescription: String,
    @SerializedName("crewGoalDays") val crewGoalDays: Int,
    @SerializedName("crewGoalType") val crewGoalType: String,
    @SerializedName("crewGoalAmount") val crewGoalAmount: Int,
    @SerializedName("crewDateStart") val crewDateStart: String,
    @SerializedName("crewDateEnd") val crewDateEnd: String,
    @SerializedName("crewTimeStart") val crewTimeStart: String,
    @SerializedName("crewTimeEnd") val crewTimeEnd: String,
    @SerializedName("crewPassword") val crewPassword: String?,
    @SerializedName("crewCost") val crewCost: Int,
    @SerializedName("crewMaxMember") val crewMaxMember: Int,
    @SerializedName("crewManagerNickName") val crewManagerNickName: String,
    @SerializedName("crewManagerSeq") val crewManagerSeq: Int
): Parcelable
