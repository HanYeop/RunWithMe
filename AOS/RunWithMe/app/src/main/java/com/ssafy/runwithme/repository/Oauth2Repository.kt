package com.ssafy.runwithme.repository

import android.util.Log
import com.ssafy.runwithme.datasource.Oauth2RemoteDataSource
import com.ssafy.runwithme.model.response.OauthResponse
import com.ssafy.runwithme.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Oauth2Repository @Inject constructor(
    private val oauth2RemoteDataSource: Oauth2RemoteDataSource
){
    fun googleLogin(code: String): Flow<Result<OauthResponse>> = flow {
        emit(Result.Loading)
        oauth2RemoteDataSource.googleLogin(code).collect {
            emit(Result.Success(it))
        }
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun naverLogin(code: String): Flow<Result<OauthResponse>> = flow {
        emit(Result.Loading)
        oauth2RemoteDataSource.naverLogin(code).collect {
            emit(Result.Success(it))
        }
    }.catch { e ->
        Log.d("test5", "naverLogin: $e")
        emit(Result.Error(e))
    }

    fun kakaoLogin(code: String): Flow<Result<OauthResponse>> = flow {
        emit(Result.Loading)
        oauth2RemoteDataSource.kakaoLogin(code).collect {
            emit(Result.Success(it))
        }
    }.catch { e ->
        Log.d("test5", "kakaoLogin: $e")
        emit(Result.Error(e))
    }
}