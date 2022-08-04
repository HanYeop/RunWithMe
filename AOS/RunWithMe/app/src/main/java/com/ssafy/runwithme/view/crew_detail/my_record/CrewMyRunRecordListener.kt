package com.ssafy.runwithme.view.crew_detail.my_record

import com.ssafy.runwithme.model.dto.RunRecordDto

interface CrewMyRunRecordListener {
    fun onItemClick(runRecordDto: RunRecordDto)
}