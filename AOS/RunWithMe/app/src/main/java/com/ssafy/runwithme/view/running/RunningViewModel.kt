package com.ssafy.runwithme.view.running

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ssafy.runwithme.model.dto.*
import com.ssafy.runwithme.model.entity.RunRecordEntity
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.repository.*
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.SingleLiveEvent
import com.ssafy.runwithme.utils.USER_NAME
import com.ssafy.runwithme.utils.USER_WEIGHT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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
    private val runRepository: RunRepository,
    private val scrapRepository: ScrapRepository,
    private val weatherRepository: WeatherRepository
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

    private val _runAbleEvent = SingleLiveEvent<String>()
    val runAbleEvent get() = _runAbleEvent

    private val _coordinateSuccess = SingleLiveEvent<String>()
    val coordinateSuccess get() = _coordinateSuccess

    private val _getCoordinates : MutableStateFlow<List<CoordinateDto>> = MutableStateFlow(listOf())
    val getCoordinates get() = _getCoordinates

    private val _localRunList: MutableStateFlow<List<RunRecordEntity>>
            = MutableStateFlow(listOf())
    val localRunList get() = _localRunList.asStateFlow()

    private val _localTotalTimeInMillis: MutableStateFlow<Int>
            = MutableStateFlow(0)
    val localTotalTimeInMillis get() = _localTotalTimeInMillis.asStateFlow()

    private val _localTotalDistance: MutableStateFlow<Int>
            = MutableStateFlow(0)
    val localTotalDistance get() = _localTotalDistance.asStateFlow()

    private val _localTotalAvgSpeed: MutableStateFlow<Double>
            = MutableStateFlow(0.0)
    val localTotalAvgSpeed get() = _localTotalAvgSpeed.asStateFlow()

    private val _localTotalCaloriesBurned: MutableStateFlow<Int>
            = MutableStateFlow(0)
    val localTotalCaloriesBurned get() = _localTotalCaloriesBurned.asStateFlow()

    private val _nickName = MutableStateFlow("")
    val nickname get() = _nickName.asStateFlow()

    private val _goalComplete = MutableStateFlow("Y")
    val goalComplete get() = _goalComplete.asStateFlow()

    private val _scrapList : MutableStateFlow<List<ScrapInfoDto>>
            = MutableStateFlow(listOf())
    val scrapList get() = _scrapList.asStateFlow()

    private val _weatherResponse : MutableStateFlow<Result<Weather>> = MutableStateFlow(Result.Uninitialized)
    val weatherResponse get() = _weatherResponse.asStateFlow()

    fun createRunRecord(crewId: Int, imgFile: MultipartBody.Part, runRecordDto: RunRecordDto){
        val json = Gson().toJson(runRecordDto)
        val run = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)

        viewModelScope.launch(Dispatchers.IO) {
            crewRepository.createRunRecords(crewId, imgFile, run).collectLatest {
                Log.d("test5", "createRunRecord: $it")

                if(it is Result.Success){
                    _runRecordSeq.value = it.data.data.runRecordDto.runRecordSeq
                    _goalComplete.value = it.data.data.runRecordDto.runRecordRunningCompleteYN
                    _coordinateSuccess.postValue("경로 전송 요청")
                    // 업적 달성 여부
                    if(it.data.data.achievements.isNotEmpty()){
                        _achievementsList.value = it.data.data.achievements
                    }
                }
            }
        }
    }

    fun createCoordinates(recordSeq: Int, coordinates: List<CoordinateDto>){
        viewModelScope.launch(Dispatchers.IO) {
            crewRepository.createCoordinates(recordSeq, coordinates).collectLatest {
                Log.d("test5", "createCoordinates: $it")
                if(it is Result.Success){
                }
            }
        }
    }

    fun getCoordinates(recordSeq: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            crewRepository.getCoordinates(recordSeq).collectLatest {
                Log.d("test5", "getCoordinates: $it")
                if(it is Result.Success){
                    _getCoordinates.value = it.data.data
                }else if(it is Result.Fail){
                    _errorMsgEvent.postValue(it.data.msg)
                }
            }
        }
    }


    fun runAbleToday(crewSeq: Int){
        viewModelScope.launch(Dispatchers.IO) {
            myActivityRepository.runAbleToday(crewSeq).collectLatest {
                Log.d("test5", "runAbleToday: $it")
                if(it is Result.Success){
                    if(it.data.data) {
                        _runAbleEvent.postValue("러닝 가능")
                    }
                    else{
                        _errorMsgEvent.postValue(it.data.msg)
                    }
                } else if(it is Result.Fail){
                    _errorMsgEvent.postValue(it.data.msg)
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
                    _nickName.value = it.data.data.userDto.nickName
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


                        val sfTime = SimpleDateFormat("HH:mm:ss", Locale.KOREA)

                        var startTime = sfTime.parse(i.crewDto.crewTimeStart)
                        var endTime = sfTime.parse(i.crewDto.crewTimeEnd)

                        val now = System.currentTimeMillis()
                        val date = Date(now)
                        val nowString = sfTime.format(date)
                        val nowTime = sfTime.parse(nowString)

                        val startCalendar = Calendar.getInstance()
                        startCalendar.time = startTime

                        val endCalendar = Calendar.getInstance()
                        endCalendar.time = endTime

                        if (today.time.time - startDate.time > 0) {
                            if (today.time.time - endDate.time > 0) {
                                Log.d("test5", "getMyCurrentCrew_end: $i")
                            } else {
                                // 뛸 수 있는 크루
                                if (nowTime.time >= startTime.time && nowTime.time <= endTime.time) {
                                    Log.d("test5", "getMyCurrentCrew_start: $i")
                                    tmpList.add(i)
                                } else {
                                    Log.d("test5", "getMyCurrentCrew_end: $i")
                                }
                            }
                        } else {
                            Log.d("test5", "getMyCurrentCrew_await: $i")
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

    fun deleteRun(run: RunRecordEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            runRepository.deleteRun(run)
        }
    }

    fun getTotalTimeInMillis() {
        viewModelScope.launch(Dispatchers.IO) {
            runRepository.getTotalTimeInMillis().collectLatest {
                if(it is Result.Success){
                    _localTotalTimeInMillis.value = it.data
                }
            }
        }
    }

    fun getTotalDistance() {
        viewModelScope.launch(Dispatchers.IO) {
            runRepository.getTotalDistance().collectLatest {
                if(it is Result.Success){
                    _localTotalDistance.value = it.data
                }
            }
        }
    }


    fun getTotalAvgSpeed() {
        viewModelScope.launch(Dispatchers.IO) {
            runRepository.getTotalAvgSpeed().collectLatest {
                if(it is Result.Success){
                    _localTotalAvgSpeed.value = it.data
                }
            }
        }
    }


    fun getTotalCaloriesBurned() {
        viewModelScope.launch(Dispatchers.IO) {
            runRepository.getTotalCaloriesBurned().collectLatest {
                if(it is Result.Success){
                    _localTotalCaloriesBurned.value = it.data
                }
            }
        }
    }

    fun getMyScrap() {
        viewModelScope.launch(Dispatchers.IO) {
            scrapRepository.getMyScrap().collectLatest {
                Log.d("test5", "getMyScrap: $it")
                if (it is Result.Success) {
                    _scrapList.value = it.data.data
                } else if (it is Result.Fail) {
                    _errorMsgEvent.postValue(it.data.msg)
                }
            }
        }
    }

    fun emptyScrapList(){
        _scrapList.value = emptyList()
    }

    fun emptyCoordinates(){
        _getCoordinates.value = emptyList()
    }

    fun getWeather(dataType : String, numOfRows : Int, pageNo : Int,
                   baseDate : String, baseTime : String, nx : Int, ny : Int){
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getWeather(dataType, numOfRows, pageNo, baseDate, baseTime, nx, ny).collectLatest {
                Log.d("test5", "getWeather: $it")
                _weatherResponse.value = it
            }
        }
    }
}