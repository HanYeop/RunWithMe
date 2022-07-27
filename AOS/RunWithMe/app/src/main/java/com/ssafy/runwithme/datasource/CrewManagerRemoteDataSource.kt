package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.CrewManagerApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.CrewDto
import com.ssafy.runwithme.model.response.CreateCrewResponse
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CrewManagerRemoteDataSource @Inject constructor(
    private val crewManagerApi: CrewManagerApi
){
    fun getMyCurrentCrew(): Flow<BaseResponse<List<MyCurrentCrewResponse>>> = flow {
        emit(crewManagerApi.getMyCurrentCrew())
    }

    fun createCrew(crewDto: RequestBody, imgFile: MultipartBody.Part?): Flow<BaseResponse<CreateCrewResponse>> = flow {
        if(imgFile == null){
            emit(crewManagerApi.createCrew(crewDto))
        }else {
            emit(crewManagerApi.createCrew(crewDto, imgFile))
        }
    }


}