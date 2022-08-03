package com.ssafy.runwithme.view.crew_detail.user_record

import com.ssafy.runwithme.model.dto.RunRecordDto

interface CrewUserRunRecordListener {
    fun onItemClick(runRecord : RunRecordDto)
}