package com.ssafy.runwithme.repository

import com.ssafy.runwithme.datasource.Oauth2RemoteDataSource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Oauth2Repository @Inject constructor(
    private val oauth2RemoteDataSource: Oauth2RemoteDataSource
){
    fun googleLogin() = flow<String> {
        oauth2RemoteDataSource.googleLogin().collect {
            emit(it)
        }
    }.catch { e ->
        emit(e.toString())
    }
}