package com.ssafy.runwithme.repository

import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.RecommendRemoteDataSource
import com.ssafy.runwithme.model.dto.TrackBoardDto
import com.ssafy.runwithme.model.response.RecommendResponse
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RecommendRepository @Inject constructor(
    private val recommendRemoteDataSource: RecommendRemoteDataSource
){
    fun createRecommend(trackBoardDto: RequestBody, img: MultipartBody.Part): Flow<Result<BaseResponse<TrackBoardDto>>> = flow {
        emit(Result.Loading)
        recommendRemoteDataSource.createRecommend(trackBoardDto, img).collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }
        }
    }.catch { e->
        emit(Result.Error(e))
    }

    fun getRecommends(leftLng: Double, lowerLat: Double, rightLng: Double, upperLat: Double): Flow<Result<BaseResponse<List<RecommendResponse>>>> = flow {
        emit(Result.Loading)
        recommendRemoteDataSource.getRecommends(leftLng, lowerLat, rightLng, upperLat).collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }
}