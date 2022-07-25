package com.ssafy.runwithme.repository

import com.ssafy.runwithme.datasource.Oauth2RemoteDataSource
import com.ssafy.runwithme.model.dto.OauthResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Oauth2Repository @Inject constructor(
    private val oauth2RemoteDataSource: Oauth2RemoteDataSource
){
    fun googleLogin(code: String) = flow<OauthResponse> {
        oauth2RemoteDataSource.googleLogin(code).collect {

            if(it.isSuccessful){
                emit(it.body()!!)
            }
        }
    }.catch { e ->
//
    }
}