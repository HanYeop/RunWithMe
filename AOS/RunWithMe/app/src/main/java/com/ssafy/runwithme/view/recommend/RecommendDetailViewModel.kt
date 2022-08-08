package com.ssafy.runwithme.view.recommend

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.ScrapInfoDto
import com.ssafy.runwithme.repository.ScrapRepository
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendDetailViewModel @Inject constructor(
    private val scrapRepository: ScrapRepository
): ViewModel() {

    private val _successMsgEvent = SingleLiveEvent<String>()
    val successMsgEvent get() = _successMsgEvent

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    private val _scrapList : MutableStateFlow<Result<BaseResponse<List<ScrapInfoDto>>>>
            = MutableStateFlow(Result.Uninitialized)
    val scrapList get() = _scrapList.asStateFlow()

    private val _currentScrapSeq = SingleLiveEvent<Int>()
    val currentScrapSeq get() = _currentScrapSeq

    private val _isScrapped = SingleLiveEvent<Int>()
    val isScrapped get() = _isScrapped

    fun addMyScrap(trackBoardSeq : Int, title : String) {
        viewModelScope.launch(Dispatchers.IO) {
            scrapRepository.addMyScrap(trackBoardSeq, title).collectLatest {
                Log.d("test5", "addMyScrap: $it")
                if (it is Result.Success) {
                    _successMsgEvent.postValue(it.data.msg)
                    _currentScrapSeq.postValue(it.data.data.scrapSeq)
                } else if (it is Result.Fail) {
                    _errorMsgEvent.postValue(it.data.msg)
                }
            }
        }
    }

    fun getMyScrap(trackBoardSeq: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            scrapRepository.getMyScrap().collectLatest {
                Log.d("test5", "getMyScrap: $it")
                if (it is Result.Success) {
                    _scrapList.value = it

                    if(trackBoardSeq != 0){
                        for(i : Int in 0..it.data.data.size){
                            var item = it.data.data
                            if(item[i].trackBoardFileDto.trackBoardDto.trackBoardSeq == trackBoardSeq){ // 이미 내가 스크랩한 경우
                                _currentScrapSeq.postValue(item[i].scrapSeq)
                                _isScrapped.postValue(1)
                                break
                            }
                        }
                    }
                } else if (it is Result.Fail) {
                    _errorMsgEvent.postValue(it.data.msg)
                }
            }
        }
    }

    fun deleteMyScrap(scrapSeq : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            scrapRepository.deleteMyScrap(scrapSeq).collectLatest {
                Log.d("test5", "deleteMyScrap: $it")
                if (it is Result.Success) {
                    _successMsgEvent.postValue(it.data.msg)
                } else if (it is Result.Fail) {
                    _errorMsgEvent.postValue(it.data.msg)
                }
            }
        }
    }

}