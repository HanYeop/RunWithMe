package com.ssafy.runwithme.view.my_page.tab.total_record

import com.ssafy.runwithme.model.dto.RunRecordDto

interface MyTotalRunRecordListener {
    fun onItemClick(runRecordDto: RunRecordDto)
}