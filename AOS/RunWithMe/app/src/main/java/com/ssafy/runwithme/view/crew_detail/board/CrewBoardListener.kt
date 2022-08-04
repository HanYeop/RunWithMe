package com.ssafy.runwithme.view.crew_detail.board

import com.ssafy.runwithme.model.response.CrewBoardResponse

interface CrewBoardListener {
    fun onDeleteClick(crewBoardResponse: CrewBoardResponse)

    fun onReportClick(boardSeq : Int)
}