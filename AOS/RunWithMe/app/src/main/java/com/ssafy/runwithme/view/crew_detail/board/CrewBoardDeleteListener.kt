package com.ssafy.runwithme.view.crew_detail.board

import com.ssafy.runwithme.model.response.CrewBoardResponse

interface CrewBoardDeleteListener {
    fun onItemClick(crewBoardResponse: CrewBoardResponse)
}