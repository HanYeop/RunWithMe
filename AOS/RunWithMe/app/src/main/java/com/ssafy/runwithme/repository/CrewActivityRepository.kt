package com.ssafy.runwithme.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ssafy.runwithme.api.CrewActivityApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.CrewBoardsDataSource
import com.ssafy.runwithme.datasource.CrewRecordsDataSource
import com.ssafy.runwithme.datasource.paging.GetCrewBoardsPagingSource
import com.ssafy.runwithme.datasource.paging.GetCrewRecordsPagingSource
import com.ssafy.runwithme.model.dto.CreateCrewBoardDto
import com.ssafy.runwithme.model.dto.CrewBoardDto
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.response.CrewBoardResponse
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CrewActivityRepository @Inject constructor(
    private val crewActivityApi: CrewActivityApi,
    private val crewRunRecordsDataSource: CrewRecordsDataSource,
    private val crewBoardsDataSource: CrewBoardsDataSource
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

    fun getCrewBoardsTop3(crewSeq: Int, size: Int): Flow<Result<BaseResponse<List<CrewBoardResponse>>>> = flow {
        emit(Result.Loading)
        crewBoardsDataSource.getCrewBoardsTop3(crewSeq, size).collect {
            if(it.data.isEmpty()){
                emit(Result.Empty)
            }else if(it.success){
                emit(Result.Success(it))
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun getCrewRecords(crewSeq: String, size: Int) =
        Pager(
            config = PagingConfig(
                pageSize = size * 2,
                maxSize = size * 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetCrewRecordsPagingSource(crewActivityApi = crewActivityApi, crewSeq = crewSeq,size = size)}
        ).flow

    fun getCrewRecordsTop3(crewSeq: String, size: Int): Flow<Result<BaseResponse<List<RunRecordDto>>>> = flow {
        emit(Result.Loading)
        crewRunRecordsDataSource.getCrewRecordsTop3(crewSeq, size).collect {
            if(it.data.isEmpty()){
                emit(Result.Empty)
            }else if(it.success){
                emit(Result.Success(it))
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun createCrewBoard(crewSeq: Int, crewBoardDto: CreateCrewBoardDto): Flow<Result<BaseResponse<CrewBoardResponse>>> = flow {
        emit(Result.Loading)
        crewBoardsDataSource.createCrewBoard(crewSeq, crewBoardDto).collect {
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
        crewBoardsDataSource.deleteCrewBoard(crewSeq, boardSeq).collect {
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