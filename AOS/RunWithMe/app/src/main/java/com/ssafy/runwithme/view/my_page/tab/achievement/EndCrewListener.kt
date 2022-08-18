package com.ssafy.runwithme.view.my_page.tab.achievement

import com.ssafy.runwithme.model.dto.CrewDto
import com.ssafy.runwithme.model.dto.ImageFileDto

interface EndCrewListener {
    fun onItemClick(crewDto: CrewDto, imgDto : ImageFileDto)
}