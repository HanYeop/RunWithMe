package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.WeatherApi
import com.ssafy.runwithme.model.dto.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRemoteDataSource @Inject constructor(
    private val weatherApi: WeatherApi
) {
   fun getWeather(dataType : String, numOfRows : Int, pageNo : Int,
        baseDate : String, baseTime : String, nx : Int, ny : Int
    ): Flow<Weather> = flow {
        emit(weatherApi.getWeather(dataType, numOfRows, pageNo, baseDate, baseTime, nx, ny))
   }
}