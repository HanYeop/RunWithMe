package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class RunRecordDto(
    @SerializedName("runRecordSeq") val runRecordSeq: Long,
    @SerializedName("runImageSeq") val runImageSeq: Long,
    @SerializedName("runRecordStartTime") val runRecordStartTime: String,
    @SerializedName("runRecordEndTime") val runRecordEndTime: String,
    @SerializedName("runRecordRunningTime") val runRecordRunningTime: Int,
    @SerializedName("runRecordRunningDistance") val runRecordRunningDistance: Int,
    @SerializedName("runRecordRunningAvgSpeed") val runRecordRunningAvgSpeed: Double,
    @SerializedName("runRecordRunningCalorie") val runRecordRunningCalorie: Int,
    @SerializedName("runRecordRunningLat") val runRecordRunningLat: Double,
    @SerializedName("runRecordRunningLng") val runRecordRunningLng: Double,
    @SerializedName("runRecordRunningCompleteYN") val runRecordRunningCompleteYN: String
)
