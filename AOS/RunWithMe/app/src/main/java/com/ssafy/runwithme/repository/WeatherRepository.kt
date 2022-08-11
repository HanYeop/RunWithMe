package com.ssafy.runwithme.repository

import com.ssafy.runwithme.datasource.WeatherRemoteDataSource
import com.ssafy.runwithme.model.dto.Weather
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) {
    fun getWeather(dataType : String, numOfRows : Int, pageNo : Int,
                   baseDate : String, baseTime : String, nx : Int, ny : Int
    ): Flow<Result<Weather>> = flow {
        emit(Result.Loading)
        weatherRemoteDataSource.getWeather(dataType, numOfRows, pageNo, baseDate, baseTime, nx, ny).collect {
            emit(Result.Success(it))
        }
    }.catch { e ->
        emit(Result.Error(e))
    }
}
