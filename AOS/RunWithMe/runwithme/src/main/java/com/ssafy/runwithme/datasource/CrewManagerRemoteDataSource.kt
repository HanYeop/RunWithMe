package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.CrewManagerApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.CrewDto
import com.ssafy.runwithme.model.dto.EndCrewFileDto
import com.ssafy.runwithme.model.response.CreateCrewResponse
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject

class CrewManagerRemoteDataSource @Inject constructor(
    private val crewManagerApi: CrewManagerApi
){
    fun getMyCurrentCrew(): Flow<BaseResponse<List<MyCurrentCrewResponse>>> = flow {
        emit(crewManagerApi.getMyCurrentCrew())
    }

}