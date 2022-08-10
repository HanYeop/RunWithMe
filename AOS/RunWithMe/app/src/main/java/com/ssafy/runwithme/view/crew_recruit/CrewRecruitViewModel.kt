package com.ssafy.runwithme.view.crew_recruit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.RecruitCrewResponse
import com.ssafy.runwithme.repository.CrewManagerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.SingleLiveEvent
import com.ssafy.runwithme.utils.TAG
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CrewRecruitViewModel @Inject constructor(
    private val crewManagerRepository: CrewManagerRepository
) : ViewModel(){

    private val _recruitPreviewList : MutableStateFlow<Result<BaseResponse<List<RecruitCrewResponse>>>>
        = MutableStateFlow(Result.Uninitialized)
    val recruitPreviewList get() = _recruitPreviewList

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    fun getRecruitCrew(size: Int) : Flow<PagingData<RecruitCrewResponse>> {
        return crewManagerRepository.getRecruitCrew(size).cachedIn(viewModelScope)
    }

    fun getRecruitCrewPreView(size: Int) {
        Log.d("getRecruitCrewPreView", "getRecruitCrewPreView: $this")
        viewModelScope.launch(Dispatchers.IO){
            crewManagerRepository.getRecruitCrewPreView(size).collectLatest {
                Log.d("getRecruitCrewPreView", "getRecruitCrewPreView: $it")
                if(it is Result.Success){
                    _recruitPreviewList.value = it
                } else if(it is Result.Error){
                    _errorMsgEvent.postValue("모집 중인 크루 미리보기 불러오는 중 에러발생")
                }
            }
        }
    }

}