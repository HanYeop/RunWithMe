package com.ssafy.runwithme.view.running

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.dto.AchievementDto
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.entity.RunRecordEntity
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.repository.CrewManagerRepository
import com.ssafy.runwithme.repository.CrewRepository
import com.ssafy.runwithme.repository.MyActivityRepository
import com.ssafy.runwithme.repository.RunRepository
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.SingleLiveEvent
import com.ssafy.runwithme.utils.USER_NAME
import com.ssafy.runwithme.utils.USER_WEIGHT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RunningViewModel @Inject constructor(
    private val crewRepository: CrewRepository,
    private val crewManagerRepository: CrewManagerRepository,
    private val sharedPreferences: SharedPreferences,
    private val myActivityRepository: MyActivityRepository,
    private val runRepository: RunRepository
): ViewModel(){

    private val _runRecordSeq = MutableStateFlow(0)
    val runRecordSeq get() = _runRecordSeq.asStateFlow()

    private val _runningCrewList: MutableStateFlow<List<MyCurrentCrewResponse>>
            = MutableStateFlow(listOf())
    val runningCrewList get() = _runningCrewList.asStateFlow()

    private val _achievementsList: MutableStateFlow<List<AchievementDto>>
            = MutableStateFlow(listOf())
    val achievementsList get() = _achievementsList.asStateFlow()

    private val _errorMsgEvent = SingleLiveEvent<String>()
    val errorMsgEvent get() = _errorMsgEvent

    private val _startRunEvent = SingleLiveEvent<String>()
    val startRunEvent get() = _startRunEvent

    private val _localRunList: MutableStateFlow<List<RunRecordEntity>>
            = MutableStateFlow(listOf())
    val localRunList get() = _localRunList.asStateFlow()

    fun createRunRecord(crewId: Int, imgFile: MultipartBody.Part, runRecordDto: RunRecordDto){
        val json = Gson().toJson(runRecordDto)
        val run = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)

        viewModelScope.launch(Dispatchers.IO) {
            crewRepository.createRunRecords(crewId, imgFile, run).collectLatest {
                Log.d("test5", "createRunRecord: $it")

                if(it is Result.Success){
                    _runRecordSeq.value = it.data.data.runRecordDto.runRecordSeq
                    // 업적 달성 여부
                    if(it.data.data.achievements.isNotEmpty()){
                        _achievementsList.value = it.data.data.achievements
                    }
                }
            }
        }
    }

    fun getMyProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            myActivityRepository.getMyProfile().collectLatest {
                if(it is Result.Success){
                    sharedPreferences.edit().putInt(USER_WEIGHT,it.data.data.userDto.weight).apply()
                    sharedPreferences.edit().putString(USER_NAME, it.data.data.userDto.nickName).apply()
                    _startRunEvent.postValue("러닝 시작")
                } else if(it is Result.Error){

                }
            }
        }
    }

    fun getMyCurrentCrew(){
        viewModelScope.launch(Dispatchers.IO) {
            crewManagerRepository.getMyCurrentCrew().collectLatest{
                if(it is Result.Success){
                    val tmpList = arrayListOf<MyCurrentCrewResponse>()
                    for(i in it.data.data) {
                        val today = Calendar.getInstance()
                        val sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                        var startDate = sf.parse(i.crewDto.crewDateStart)
                        var endDate = sf.parse(i.crewDto.crewDateEnd)


                        val sfTime = SimpleDateFormat("HH:mm:ss")

                        var startTime = sfTime.parse(i.crewDto.crewTimeStart)
                        var endTime = sfTime.parse(i.crewDto.crewTimeEnd)

                        val now = System.currentTimeMillis()
                        val date = Date(now)

                        if (today.time.time - startDate.time > 0) {
                            if (today.time.time - endDate.time > 0) {
                                Log.d("test5", "getMyCurrentCrew1: $i")
//                                _crewState.value = "end"
                            } else {
                                if (date.time >= startTime.time && date.time <= endTime.time) {
                                    Log.d("test5", "getMyCurrentCrew2: $i")
                                    tmpList.add(i)
                                } else {
//                                    _crewState.value = "end"
                                    Log.d("test5", "getMyCurrentCrew3: $i")
                                }
                            }
                        } else {
                            Log.d("test5", "getMyCurrentCrew4: $i")
//                            _crewState.value = "await"
                        }
                    }
                    _runningCrewList.value = tmpList
                }else if(it is Result.Error){
                    _errorMsgEvent.postValue("내 크루 불러오기 중 오류가 발생했습니다.")
                }
            }
        }
    }

    fun insertRun(run: RunRecordEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            runRepository.insertRun(run)
        }
    }

    fun getAllRunsSortedByDate() {
        viewModelScope.launch(Dispatchers.IO) {
            runRepository.getAllRunsSortedByDate().collectLatest {
                if(it is Result.Success){
                    _localRunList.value = it.data
                }
            }
        }
    }
}