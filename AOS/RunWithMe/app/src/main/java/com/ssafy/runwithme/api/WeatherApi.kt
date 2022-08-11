package com.ssafy.runwithme.api

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
        @Query("base_date") baseDate : String,
        @Query("base_time") baseTime : String,
        @Query("nx") nx : Int,
        @Query("ny") ny : Int
    ) : Weather
}