package com.ssafy.runwithme.datasource

import com.ssafy.runwithme.api.Oauth2Api
import com.ssafy.runwithme.model.dto.OauthResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class Oauth2RemoteDataSource @Inject constructor(
    private val oauth2Api: Oauth2Api
){
    fun googleLogin(code: String): Flow<Response<OauthResponse>> = flow {
        emit(oauth2Api.googleLogin(code))
    }
}