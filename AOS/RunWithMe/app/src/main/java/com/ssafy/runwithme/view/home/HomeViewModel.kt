package com.ssafy.runwithme.view.home

import androidx.lifecycle.ViewModel
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.repository.CrewManagerRepository
import com.ssafy.runwithme.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                MyCurrentCrewResponse(1, "감크루", "감스트", "안녕하세요 감크루입니다", 1, 6, "시간", 20, "09 : 00", "10 : 00", "2022/07/01", "2022/08/19",false)
                ,
                MyCurrentCrewResponse(1, "철크루", "철구", "안녕하세요 철크루입니다",1, 6, "시간", 20, "09 : 00", "10 : 00", "2022/07/15", "2022/08/30", false)
        ))
    }
}
