package com.ssafy.runwithme.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ssafy.runwithme.api.MyActivityApi
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.datasource.MyActivityRemoteDataSource
import com.ssafy.runwithme.datasource.paging.GetMyBoardsPagingSource
import com.ssafy.runwithme.model.response.MyProfileResponse
import com.ssafy.runwithme.model.response.MyRunRecordResponse
import com.ssafy.runwithme.model.response.MyTotalBoardsResponse
import com.ssafy.runwithme.model.response.MyTotalRecordResponse
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MyActivityRepository @Inject constructor(
    private val myActivityRemoteDataSource: MyActivityRemoteDataSource,
    private val myActivityApi: MyActivityApi
){
    fun getMyRunRecord(month : Int, year : Int) : Flow<Result<BaseResponse<List<MyRunRecordResponse>>>> = flow {
        emit(Result.Loading)
        myActivityRemoteDataSource.getMyRunRecord(month, year).collect {
            if(it.success){
                emit(Result.Success(it))
            } else {
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }


    fun getMyProfile(): Flow<Result<BaseResponse<MyProfileResponse>>> = flow {
        emit(Result.Loading)
        myActivityRemoteDataSource.getMyProfile().collect {
            if(it.success){
                emit(Result.Success(it))
            }else {
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun editMyProfile(profileEditDto: RequestBody, imgFile : MultipartBody.Part?
    ): Flow<Result<BaseResponse<MyProfileResponse>>> = flow {
        emit(Result.Loading)
        myActivityRemoteDataSource.editMyProfile(profileEditDto, imgFile).collect {
            Log.d(TAG, "받아온 결과 : $it")
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

    fun getMyTotalRecord(): Flow<Result<BaseResponse<MyTotalRecordResponse>>> = flow {
        emit(Result.Loading)
        myActivityRemoteDataSource.getMyTotalRecord().collect {
            if(it.success){
                emit(Result.Success(it))
            }else {
                emit(Result.Empty)
            }
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun getMyBoards(size : Int) =
        Pager(
            config = PagingConfig(
                pageSize = (size * 2),
                maxSize = (size * 10),
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetMyBoardsPagingSource(myActivityApi, size) }
        ).flow
}