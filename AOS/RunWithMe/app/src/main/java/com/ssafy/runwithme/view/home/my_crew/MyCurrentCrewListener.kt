package com.ssafy.runwithme.view.home.my_crew

import com.ssafy.runwithme.model.response.MyCurrentCrewResponse

interface MyCurrentCrewListener {
    fun onItemClick(myCurrentCrewResponse : MyCurrentCrewResponse)

    fun onBtnStartClick(myCurrentCrewResponse : MyCurrentCrewResponse)
}