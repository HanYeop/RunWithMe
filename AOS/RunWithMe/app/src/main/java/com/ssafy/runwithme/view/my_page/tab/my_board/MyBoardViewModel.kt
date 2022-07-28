package com.ssafy.runwithme.view.my_page.tab.my_board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.PagingData
import com.ssafy.runwithme.model.response.MyTotalBoardsResponse
import com.ssafy.runwithme.repository.MyActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyBoardViewModel @Inject constructor(
    private val myActivityRepository: MyActivityRepository
) : ViewModel(){

    fun getMyBoards(size: Int) : Flow<PagingData<MyTotalBoardsResponse>> {
        return myActivityRepository.getMyBoards(size).cachedIn(viewModelScope)
    }

}