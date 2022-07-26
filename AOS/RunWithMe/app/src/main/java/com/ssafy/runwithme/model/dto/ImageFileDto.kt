package com.ssafy.runwithme.model.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageFileDto(
    @SerializedName("imgSeq") val imgSeq: Int,
    @SerializedName("imgOriginalName") val imgOriginalName: String,
    @SerializedName("imgSavedName") val imgSavedName: String,
    @SerializedName("imgSavedPath") val imgSavedPath: String,
): Parcelable
