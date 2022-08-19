package com.ssafy.runwithme.view.my_page.recommend_scrap

import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.dto.TrackBoardDto

interface MyRecommendScrapListener {
    fun onItemClick(runRecordDto: RunRecordDto, trackBoardDto: TrackBoardDto, imgSeq : Int)
}