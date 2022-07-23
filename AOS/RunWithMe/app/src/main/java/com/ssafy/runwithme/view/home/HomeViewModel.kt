package com.ssafy.runwithme.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.annotations.SerializedName
import com.ssafy.runwithme.model.dto.MyCurrentCrewResponse
import com.ssafy.runwithme.repository.CrewManagerRepository
import com.ssafy.runwithme.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val crewManagerRepository: CrewManagerRepository
) : ViewModel(){

    private val _myCurrentCrewList: MutableStateFlow<Result<List<MyCurrentCrewResponse>>>
        = MutableStateFlow(Result.Uninitialized)
    val myCurrentCrewList get() = _myCurrentCrewList.asStateFlow()

    fun getMyCurrentCrew(){
//        viewModelScope.launch(Dispatchers.IO) {
//            crewManagerRepository.getMyCurrentCrew().collect{
//                _myCurrentCrewList.value = it
//            }
//        }

        // TEST
        _myCurrentCrewList.value = Result.Success(
            listOf(
                MyCurrentCrewResponse(1, "test", 1, 6, "시간", 20, "09 : 00", "10 : 00", false)
                ,MyCurrentCrewResponse(1, "test", 1, 6, "시간", 20, "09 : 00", "10 : 00", false)
        ))
    }
}
