package com.ssafy.runwithme.model.dto

data class UserAuthorization(
    val msg: String,
    val isRegistered: Boolean,
    val email: String,
    val jwtToken: String
)
