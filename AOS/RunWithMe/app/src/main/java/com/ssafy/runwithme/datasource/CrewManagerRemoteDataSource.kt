package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.CrewManagerApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.EndCrewFileDto
import com.ssafy.runwithme.model.response.CreateCrewResponse
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.model.response.RecruitCrewResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrewManagerRemoteDataSource @Inject constructor(
    private val crewManagerApi: CrewManagerApi
){
    fun getMyCurrentCrew(): Flow<BaseResponse<List<MyCurrentCrewResponse>>> = flow {
        emit(crewManagerApi.getMyCurrentCrew())
    }

    fun getMyEndCrew() : Flow<BaseResponse<List<EndCrewFileDto>>> = flow {
        emit(crewManagerApi.getMyEndCrew())
    }

    fun getRecruitCrewPreView(size : Int) : Flow<BaseResponse<List<RecruitCrewResponse>>> = flow {
        emit(crewManagerApi.getRecruitCrewPreView(size))
    }

    fun createCrew(crewDto: RequestBody, imgFile: MultipartBody.Part?): Flow<BaseResponse<CreateCrewResponse>> = flow {
        if(imgFile == null){
            emit(crewManagerApi.createCrew(crewDto))
        }else {
            emit(crewManagerApi.createCrew(crewDto, imgFile))
        }
    }

    fun checkCrewMember(crewSeq: Int) : Flow<BaseResponse<Boolean>> = flow {
        emit(crewManagerApi.checkCrewMember(crewSeq))
    }

    fun deleteCrew(crewSeq: Int): Flow<BaseResponse<Boolean>> = flow {
        emit(crewManagerApi.deleteCrew(crewSeq))
    }

    fun resignCrew(crewSeq: Int): Flow<BaseResponse<Int>> = flow {
        emit(crewManagerApi.resignCrew(crewSeq))
    }

}