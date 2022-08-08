package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.RecommendApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.TrackBoardDto
import com.ssafy.runwithme.model.dto.TrackBoardFileDto
import com.ssafy.runwithme.model.response.RecommendResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RecommendRemoteDataSource @Inject constructor(
    private val recommendApi: RecommendApi
){
    fun createRecommend(trackBoardDto: RequestBody, img: MultipartBody.Part): Flow<BaseResponse<TrackBoardDto>> = flow {
        emit(recommendApi.createRecommend(trackBoardDto, img))
    }

    fun getRecommends(leftLng: Double, lowerLat: Double, rightLng: Double, upperLat: Double): Flow<BaseResponse<List<TrackBoardFileDto>>> = flow {
        emit(recommendApi.getRecommends(leftLng, lowerLat, rightLng, upperLat))
    }
}