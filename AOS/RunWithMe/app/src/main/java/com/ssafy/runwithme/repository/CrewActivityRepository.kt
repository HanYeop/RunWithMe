package com.ssafy.runwithme.repository

import android.service.notification.NotificationListenerService
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ssafy.runwithme.api.CrewActivityApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.CrewActivityRemoteDataSource
import com.ssafy.runwithme.datasource.paging.GetCrewBoardsPagingSource
import com.ssafy.runwithme.datasource.paging.GetCrewRecordsPagingSource
import com.ssafy.runwithme.model.dto.CreateCrewBoardDto
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.response.CrewBoardResponse
import com.ssafy.runwithme.model.response.CrewMyTotalRecordDataResponse
import com.ssafy.runwithme.model.response.MyGraphDataResponse
import com.ssafy.runwithme.model.response.RankingResponse
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CrewActivityRepository @Inject constructor(
    private val crewActivityApi: CrewActivityApi,
    private val crewActivityRemoteDataSource: CrewActivityRemoteDataSource
) {
    fun getCrewBoards(crewSeq: Int, size: Int) =
        Pager(
            config = PagingConfig(
                pageSize = size * 2,
                maxSize = size * 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetCrewBoardsPagingSource(crewActivityApi = crewActivityApi, crewSeq = crewSeq, size = size)}
        ).flow


    fun getCrewRecords(crewSeq: Int, size: Int) =
        Pager(
            config = PagingConfig(
                pageSize = size * 2,
                maxSize = size * 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetCrewRecordsPagingSource(crewActivityApi = crewActivityApi, crewSeq = crewSeq,size = size)}
        ).flow


    fun createCrewBoard(crewSeq: Int, crewBoardDto: CreateCrewBoardDto): Flow<Result<BaseResponse<CrewBoardResponse>>> = flow {
        emit(Result.Loading)
        crewActivityRemoteDataSource.createCrewBoard(crewSeq, crewBoardDto).collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun deleteCrewBoard(crewSeq: Int, boardSeq: Int): Flow<Result<BaseResponse<Boolean>>> = flow {
        emit(Result.Loading)
        crewActivityRemoteDataSource.deleteCrewBoard(crewSeq, boardSeq).collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }


    fun getCrewRanking(crewSeq: Int, type: String): Flow<Result<BaseResponse<List<RankingResponse>>>> = flow {
        emit(Result.Loading)
        crewActivityRemoteDataSource.getCrewRanking(crewSeq, type).collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }else{
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun getMyGraphData(crewSeq: Int, goalType: String): Flow<Result<BaseResponse<List<MyGraphDataResponse>>>> = flow {
        emit(Result.Loading)
        crewActivityRemoteDataSource.getMyGraphData(crewSeq, goalType).collect {
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }else{
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun getMyTotalRecordData(crewSeq: Int): Flow<Result<BaseResponse<CrewMyTotalRecordDataResponse>>> = flow {
        emit(Result.Loading)
        crewActivityRemoteDataSource.getMyTotalRecordData(crewSeq).collect {
            Log.d(TAG, "getMyTotalRecordData: repository , $it")
            if(it.success){
                emit(Result.Success(it))
            }else if(!it.success){
                emit(Result.Fail(it))
            }else{
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        Log.d(TAG, "getMyTotalRecordData:repos, $e")
        emit(Result.Error(e))
    }

}