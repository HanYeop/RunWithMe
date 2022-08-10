package com.ssafy.runwithme.api

import com.ssafy.runwithme.R
import com.ssafy.runwithme.model.dto.Weather
import com.ssafy.runwithme.utils.weatherApiKey
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("getVilageFcst?serviceKey=${weatherApiKey}")
    suspend fun getWeather(
        @Query("dataType") dataType : String,
        @Query("numOfRows") numOfRows : Int,
        @Query("pageNo") pageNo : Int,
        @Query("base_date") baseDate : Int,
        @Query("base_time") baseTime : Int,
        @Query("nx") nx : String,
        @Query("ny") ny : String
    ) : Weather
}