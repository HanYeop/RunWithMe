package com.ssafy.runwithme.view.home.my_crew

import com.ssafy.runwithme.model.dto.MyCurrentCrewResponse

interface MyCurrentCrewListener {
    fun onItemClick(myCurrentCrewResponse : MyCurrentCrewResponse)
}