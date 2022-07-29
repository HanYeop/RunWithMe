package com.ssafy.runwithme.view.crew_detail.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ssafy.runwithme.model.dto.CreateCrewBoardDto
import com.ssafy.runwithme.model.dto.CrewBoardDto
import com.ssafy.runwithme.model.dto.ImageFileDto
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.response.CrewBoardResponse
import com.ssafy.runwithme.repository.CrewActivityRepository
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrewBoardViewModel @Inject constructor(
    private val crewActivityRepository: CrewActivityRepository
) : ViewModel(){


    private val _crewBoardsList: MutableStateFlow<List<CrewBoardResponse>>
            = MutableStateFlow(listOf(CrewBoardResponse(CrewBoardDto(0, "게시글이 없습니다.", "2022-01-01", "관리자", 0, ""), ImageFileDto(0, "", "", ""))))
    val crewBoardsList get() = _crewBoardsList.asStateFlow()

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    private val _failMsgEvent = SingleLiveEvent<String>()
    val failMsgEvent get() = _failMsgEvent

    private val _createSuccessMsgEvent = SingleLiveEvent<String>()
    val createSuccessMsgEvent get() = _createSuccessMsgEvent

    fun getCrewBoards(crewSeq: Int, size: Int): Flow<PagingData<CrewBoardResponse>> {
        return crewActivityRepository.getCrewBoards(crewSeq, size).cachedIn(viewModelScope)
    }

    fun createCrewBoard(crewSeq: Int, crewBoardDto: CreateCrewBoardDto) {
        viewModelScope.launch(Dispatchers.IO) {
            crewActivityRepository.createCrewBoard(crewSeq, crewBoardDto).collectLatest {
                if(it is Result.Success){
                    _createSuccessMsgEvent.postValue("글을 등록했습니다.")
                }else if(it is Result.Error){
                    _errorMsgEvent.postValue("오류가 발생했습니다.")
                }else if(it is Result.Fail){
                    _failMsgEvent.postValue(it.data.msg)
                }
            }
        }
    }



}