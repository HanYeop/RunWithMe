package com.ssafy.runwithme.model.dto

import com.google.gson.annotations.SerializedName

data class ImageFileDto(
    @SerializedName("imgSeq") val imgSeq: Int,
    @SerializedName("imgOriginalName") val imgOriginalName: String,
    @SerializedName("imgSavedName") val imgSavedName: String,
    @SerializedName("imgSavedPath") val imgSavedPath: String,
)
