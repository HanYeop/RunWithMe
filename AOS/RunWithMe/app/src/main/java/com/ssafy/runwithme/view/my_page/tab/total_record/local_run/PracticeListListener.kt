package com.ssafy.runwithme.view.my_page.tab.total_record.local_run

import com.ssafy.runwithme.model.entity.RunRecordEntity

interface PracticeListListener {

    fun onDeleteClick(run : RunRecordEntity)
}