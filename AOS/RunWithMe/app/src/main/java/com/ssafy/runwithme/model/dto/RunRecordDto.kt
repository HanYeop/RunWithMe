package com.ssafy.runwithme.model.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RunRecordDto(
    @SerializedName("runRecordSeq") val runRecordSeq: Int,
    @SerializedName("runImageSeq") val runImageSeq: Int,
    @SerializedName("runRecordStartTime") val runRecordStartTime: String,
    @SerializedName("runRecordEndTime") val runRecordEndTime: String,
    @SerializedName("runRecordRunningTime") val runRecordRunningTime: Int,
    @SerializedName("runRecordRunningDistance") val runRecordRunningDistance: Int,
    @SerializedName("runRecordRunningAvgSpeed") val runRecordRunningAvgSpeed: Double,
    @SerializedName("runRecordRunningCalorie") val runRecordRunningCalorie: Int,
    @SerializedName("runRecordRunningLat") val runRecordRunningLat: Double,
    @SerializedName("runRecordRunningLng") val runRecordRunningLng: Double,
    @SerializedName("runRecordRunningCompleteYN") val runRecordRunningCompleteYN: String,
    @SerializedName("userName") val userName: String = "",
    @SerializedName("userSeq") val userSeq: Int = 0,
    @SerializedName("crewName") val crewName: String = "",
    @SerializedName("crewSeq") val crewSeq: Int = 0,
) : Parcelable

