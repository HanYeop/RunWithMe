package com.ssafy.runwithme.view.running.list

import com.ssafy.runwithme.model.response.MyCurrentCrewResponse

interface RunningListListener {
    fun onItemClick(myCurrentCrewResponse : MyCurrentCrewResponse)
}