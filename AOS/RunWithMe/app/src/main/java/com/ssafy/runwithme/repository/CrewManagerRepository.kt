package com.ssafy.runwithme.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ssafy.runwithme.api.CrewManagerApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.CrewManagerRemoteDataSource
import com.ssafy.runwithme.datasource.paging.GetCrewBoardsPagingSource
import com.ssafy.runwithme.datasource.paging.GetRecruitCrewPagingSource
import com.ssafy.runwithme.datasource.paging.GetSearchResultCrewPagingSource
import com.ssafy.runwithme.model.dto.CrewDto
import com.ssafy.runwithme.model.response.CreateCrewResponse
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CrewManagerRepository @Inject constructor(
    private val crewManagerRemoteDataSource: CrewManagerRemoteDataSource,
    private val crewManagerApi: CrewManagerApi
){
    fun getMyCurrentCrew(): Flow<Result<BaseResponse<List<MyCurrentCrewResponse>>>> = flow {
        emit(Result.Loading)
        crewManagerRemoteDataSource.getMyCurrentCrew().collect {
            if(it.data.isEmpty()){
                emit(Result.Empty)
            }else if(it.success){
                emit(Result.Success(it))
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun createCrew(crewDto: RequestBody, imgFile: MultipartBody.Part?): Flow<Result<BaseResponse<CreateCrewResponse>>> = flow {
        emit(Result.Loading)
        crewManagerRemoteDataSource.createCrew(crewDto, imgFile).collect{
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }
            else{
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun checkCrewMember(crewSeq: Int): Flow<Result<BaseResponse<Boolean>>> = flow {
        emit(Result.Loading)
        crewManagerRemoteDataSource.checkCrewMember(crewSeq).collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }
            else{
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun getRecruitCrew(size: Int) =
        Pager(
            config = PagingConfig(
                pageSize = size * 2,
                maxSize = size * 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetRecruitCrewPagingSource(crewManagerApi = crewManagerApi, size = size)}
        ).flow

    fun getSearchResultCrew(size: Int, crewName: String?,
                            startDay: String?,
                            endDay: String?,
                            startTime: String?,
                            endTime: String?,
                            minCost: Int,
                            maxCost: Int,
                            purposeMinValue: Int,
                            purposeMaxValue: Int,
                            goalMinDay: Int,
                            goalMaxDay: Int,
                            purposeType: String?) =
        Pager(
            config = PagingConfig(
                pageSize = size * 2,
                maxSize = size * 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetSearchResultCrewPagingSource(crewManagerApi = crewManagerApi, size = size,
                crewName = crewName, startDay = startDay, endDay = endDay, startTime = startTime, endTime = endTime,
                minCost = minCost, maxCost = maxCost, purposeMinValue = purposeMinValue, purposeMaxValue = purposeMaxValue,
                goalMinDay = goalMinDay, goalMaxDay = goalMaxDay, purposeType = purposeType)}
        ).flow



}