package com.ssafy.runwithme.view.home

import com.ssafy.runwithme.model.response.MyCurrentCrewResponse

interface HomeMyCurrentCrewListener {
    fun onItemClick(myCurrentCrewResponse : MyCurrentCrewResponse)

    fun onBtnStartClick(myCurrentCrewResponse : MyCurrentCrewResponse)
}