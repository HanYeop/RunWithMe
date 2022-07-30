package com.ssafy.runwithme.view.recommend

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.RankingResponse
import com.ssafy.runwithme.model.response.RecommendResponse
import com.ssafy.runwithme.repository.RecommendRepository
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository
): ViewModel() {

    private val _successMsgEvent = SingleLiveEvent<String>()
    val successMsgEvent get() = _successMsgEvent

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    private val _recommendList : MutableStateFlow<List<RecommendResponse>>
            = MutableStateFlow(listOf())
    val recommendList get() = _recommendList.asStateFlow()

    fun createRecommend(environmentPoint: Int, hardPoint: Int, RunRecordSeq: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            recommendRepository.createRecommend(environmentPoint, hardPoint, RunRecordSeq)
                .collectLatest {
                    Log.d("test5", "createRecommend: $it")
                    if (it is Result.Success) {
                        _successMsgEvent.postValue(it.data.msg)
                    } else if (it is Result.Fail) {
                        _errorMsgEvent.postValue(it.data.msg)
                    }
                }
        }
    }

    fun getRecommends(leftLng: Double, lowerLat: Double, rightLng: Double, upperLat: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            recommendRepository.getRecommends(leftLng, lowerLat, rightLng, upperLat).collectLatest {
                if (it is Result.Success) {
                    _recommendList.value = it.data.data
                } else if (it is Result.Fail) {
                    _errorMsgEvent.postValue(it.data.msg)
                }
            }
        }
    }
}