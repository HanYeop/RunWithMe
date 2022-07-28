package com.ssafy.runwithme.view.crew_recruit

import com.ssafy.runwithme.model.response.RecruitCrewResponse


interface CrewRecruitListener {
    fun onItemClick(recruitCrewResponse: RecruitCrewResponse)
}