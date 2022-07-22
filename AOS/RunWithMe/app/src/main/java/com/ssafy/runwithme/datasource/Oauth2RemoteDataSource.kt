package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.Oauth2Api
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Oauth2RemoteDataSource @Inject constructor(
    private val oauth2Api: Oauth2Api
){
    fun googleLogin(token: String): Flow<String> = flow {
        emit(oauth2Api.googleLogin(token,"ttta").toString())
    }
}