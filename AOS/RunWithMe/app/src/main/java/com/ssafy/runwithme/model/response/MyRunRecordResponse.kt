package com.ssafy.runwithme.model.response

import com.google.gson.annotations.SerializedName

data class MyRunRecordResponse(
    @SerializedName("runRecordSeq") val runRecordSeq: Int,
    @SerializedName("runImageSeq") val runImageSeq: Int,
    @SerializedName("runRecordStartTime") val runRecordStartTime: String,
    @SerializedName("runRecordEndTime") val runRecordEndTime: String,
    @SerializedName("runRecordRunningTime") val totalLongestDistance: Int,
    @SerializedName("runRecordRunningDistance") val totalAvgSpeed: Int,
    @SerializedName("runRecordRunningAvgSpeed") val totalCalorie: Double,
    @SerializedName("runRecordRunningCalorie") val runRecordRunningCalorie: Double,
    @SerializedName("runRecordRunningLat") val runRecordRunningLat: Double,
    @SerializedName("runRecordRunningLng") val runRecordRunningLng: Double,
    @SerializedName("runRecordRunningCompleteYN") val runRecordRunningCompleteYN: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("userSeq") val userSeq: Int,
    @SerializedName("crewName") val crewName: String,
    @SerializedName("crewSeq") val crewSeq: Int,
)



