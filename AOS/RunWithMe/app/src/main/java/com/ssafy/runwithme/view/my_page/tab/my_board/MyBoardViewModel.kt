package com.ssafy.runwithme.view.my_page.tab.my_board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ssafy.runwithme.model.dto.CrewBoardDto
import com.ssafy.runwithme.repository.CrewActivityRepository
import com.ssafy.runwithme.repository.MyActivityRepository
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyBoardViewModel @Inject constructor(
    private val myActivityRepository: MyActivityRepository,
    private val crewActivityRepository: CrewActivityRepository,
) : ViewModel(){

    private val _successMsgEvent = SingleLiveEvent<String>()
    val successMsgEvent get() = _successMsgEvent

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    private val _failMsgEvent = SingleLiveEvent<String>()
    val failMsgEvent get() = _failMsgEvent


    fun getMyBoards(size: Int) : Flow<PagingData<CrewBoardDto>> {
        return myActivityRepository.getMyBoards(size)
    }

    fun deleteCrewBoard(boardSeq: Int){
        viewModelScope.launch (Dispatchers.IO){
            crewActivityRepository.deleteCrewBoard(boardSeq).collectLatest {
                if(it is Result.Success){
                    _successMsgEvent.postValue("삭제 완료했습니다.")
                }else if(it is Result.Error){
                    _errorMsgEvent.postValue("오류가 발생했습니다.")
                }else if(it is Result.Fail){
                    _failMsgEvent.postValue(it.data.msg)
                }
            }
        }
    }

}